package narutoshinobicraft.common.jutsu.cast;

import java.util.Optional;

import narutoshinobicraft.common.data.attachments.PlayersChakra;
import narutoshinobicraft.common.data.component.JutsuContext;
import narutoshinobicraft.common.data.component.JutsuStackOps;
import narutoshinobicraft.common.jutsu.api.JutsuDefinition;
import narutoshinobicraft.common.jutsu.helpers.JutsuChargeModifiers;
import narutoshinobicraft.common.jutsu.helpers.JutsuPowerCalculator;
import narutoshinobicraft.common.jutsu.support.JutsuScrollSupport;
import narutoshinobicraft.common.network.payloads.JutsuCastSuccessPayload;
import narutoshinobicraft.common.registry.AttachmentRegistry;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

@SuppressWarnings("null")
public final class JutsuCastExecutor {
    private JutsuCastExecutor() {}

    public static float getPower(ItemStack stack, LivingEntity entity, int timeLeft) {
        return JutsuRegistry.findCurrentEntry(stack)
            .map(entry -> JutsuPowerCalculator.calculatePower(
                timeLeft,
                entry.definition().resolvedMaxChargeTicks(),
                entry.definition().basePower(),
                entry.definition().powerupDelay(),
                getModifier(stack, entity, JutsuStackOps.getCurrentJutsuId(stack)),
                getMaxPower(entity, entry.definition())
            ))
            .orElse(0.0f);
    }

    public static float resolvePower(ItemStack stack, LivingEntity entity, int timeLeft) {
        return JutsuRegistry.findCurrentEntry(stack)
            .flatMap(entry -> {
                float storedPower = entry.action().getPower(stack);
                return storedPower > 0.0f
                    ? Optional.of(storedPower)
                    : Optional.empty();
            })
            .orElseGet(() -> getPower(stack, entity, timeLeft));
    }

    public static CastOutcome execute(ItemStack stack, LivingEntity entity, float power) {
        if (!(entity instanceof Player player)) {
            return CastOutcome.INVALID_STATE;
        }
        if (!JutsuCastValidator.validateRelease(stack, player, player.level())) {
            return CastOutcome.NOT_READY;
        }

        ResourceLocation jutsuId = JutsuStackOps.getCurrentJutsuId(stack);
        if (jutsuId == null || !JutsuStackOps.getJutsuIds(stack).contains(jutsuId)) {
            return CastOutcome.INVALID_STATE;
        }
        var entry = JutsuRegistry.findEntry(jutsuId).orElse(null);
        if (entry == null) {
            return CastOutcome.INVALID_STATE;
        }

        float storedPower = entry.action().getPower(stack);
        float effectivePower = storedPower > 0.0f ? storedPower : power;
        if (effectivePower <= 0.0f) {
            return CastOutcome.NO_POWER;
        }

        JutsuDefinition definition = entry.definition();
        boolean creative = player.getAbilities().instabuild;
        PlayersChakra chakra = player.getData(AttachmentRegistry.PLAYER_CHAKRA);
        double chakraCost = definition.chakraCost() * effectivePower;
        if (!creative && chakra.getCurrentChakra() < chakraCost) {
            return CastOutcome.INSUFFICIENT_CHAKRA;
        }

        long gameTime = player.level().getGameTime();
        long cooldownBefore = JutsuStackOps.getCooldownEnd(stack, jutsuId);

        JutsuContext context = new JutsuContext(
            player.level(),
            player,
            stack,
            JutsuStackOps.getState(stack),
            effectivePower,
            jutsuId
        );
        if (!entry.action().execute(context)) {
            return CastOutcome.ACTION_FAILED;
        }

        if (!creative) {
            chakra.consumeChakra(chakraCost);
        }

        long cooldownAfter = JutsuStackOps.getCooldownEnd(stack, jutsuId);
        if (cooldownAfter <= gameTime && cooldownAfter == cooldownBefore) {
            JutsuScrollSupport.applyDefinitionCooldown(stack, player, definition);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                serverPlayer,
                new JutsuCastSuccessPayload(jutsuId, player.getId())
            );
        }
        return CastOutcome.SUCCESS;
    }

    public static double getMaxPower(LivingEntity entity, double jutsuChakraUsage) {
        if (!(entity instanceof Player player)) {
            return 0.0d;
        }
        PlayersChakra chakra = player.getData(AttachmentRegistry.PLAYER_CHAKRA);
        return JutsuPowerCalculator.resolveMaxPower(chakra.getCurrentChakra(), jutsuChakraUsage);
    }

    private static float getModifier(ItemStack stack, LivingEntity entity, ResourceLocation jutsuId) {
        double ninjaLevel = 0.0d;
        boolean creativeLike = true;
        if (entity instanceof Player player) {
            ninjaLevel = player.getData(AttachmentRegistry.PLAYER_PROCESS).getNinjaLevel();
            creativeLike = player.getAbilities().instabuild;
        }
        int requiredXp = JutsuCastValidator.getRequiredXp(stack, jutsuId);
        return JutsuChargeModifiers.chargeModifier(
            ninjaLevel,
            JutsuStackOps.getState(stack),
            jutsuId,
            requiredXp,
            creativeLike
        );
    }

    private static float getMaxPower(LivingEntity entity, JutsuDefinition definition) {
        if (!(entity instanceof Player player)) {
            return 0.0f;
        }
        if (player.getAbilities().instabuild) {
            return definition.maxPowerCap() > 0.0f
                ? definition.maxPowerCap()
                : JutsuPowerCalculator.resolveMaxPower(player.getData(AttachmentRegistry.PLAYER_CHAKRA), definition);
        }
        PlayersChakra chakra = player.getData(AttachmentRegistry.PLAYER_CHAKRA);
        return JutsuPowerCalculator.resolveMaxPower(chakra, definition);
    }
}
