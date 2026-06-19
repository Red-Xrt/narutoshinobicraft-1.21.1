package narutoshinobicraft.common.jutsu.cast;

import javax.annotation.Nullable;

import narutoshinobicraft.common.data.component.JutsuStackOps;
import narutoshinobicraft.common.registry.AttachmentRegistry;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SuppressWarnings("null")
public final class JutsuCastValidator {
    public enum UseGate {
        FAIL,
        PASS,
        ALLOW
    }

    public enum UseDenyReason {
        INVALID_JUTSU("chattext.jutsu.invalid"),
        NOT_OWNER("chattext.jutsu.not_owner"),
        DISABLED("chattext.jutsu.disabled"),
        INSUFFICIENT_XP("chattext.jutsu.insufficient_xp"),
        NOT_NINJA("chattext.jutsu.not_ninja");

        private final String translationKey;

        UseDenyReason(String translationKey) {
            this.translationKey = translationKey;
        }

        public Component message() {
            return Component.translatable(translationKey);
        }
    }

    private JutsuCastValidator() {}

    public static boolean isRegistered(@Nullable ResourceLocation jutsuId) {
        return jutsuId != null && JutsuRegistry.getJutsu(jutsuId) != null;
    }

    public static int getRequiredXp(ItemStack stack, @Nullable ResourceLocation jutsuId) {
        if (jutsuId == null || !JutsuStackOps.getJutsuIds(stack).contains(jutsuId)) {
            return Integer.MAX_VALUE;
        }
        JutsuRegistry.JutsuEntry entry = JutsuRegistry.getJutsu(jutsuId);
        if (entry == null) {
            return Integer.MAX_VALUE;
        }
        int requiredXp = entry.definition().requiredXp();
        return JutsuStackOps.hasAffinity(stack) ? requiredXp : requiredXp * 2;
    }

    public static int getCurrentRequiredXp(ItemStack stack) {
        return getRequiredXp(stack, JutsuStackOps.getCurrentJutsuId(stack));
    }

    public static boolean canUseJutsu(ItemStack stack, ResourceLocation jutsuId, @Nullable LivingEntity entity) {
        if (!JutsuStackOps.getJutsuIds(stack).contains(jutsuId)) {
            return false;
        }
        if (entity instanceof Player player) {
            return (JutsuStackOps.matchesOwner(stack, player) && JutsuStackOps.isJutsuEnabled(stack, jutsuId))
                || player.getAbilities().instabuild;
        }
        return false;
    }

    public static boolean canUseCurrentJutsu(ItemStack stack, @Nullable LivingEntity entity) {
        ResourceLocation currentId = JutsuStackOps.getCurrentJutsuId(stack);
        return currentId != null && canUseJutsu(stack, currentId, entity);
    }

    public static boolean canUseAnyJutsu(ItemStack stack) {
        for (ResourceLocation id : JutsuStackOps.getJutsuIds(stack)) {
            if (JutsuStackOps.getXp(stack, id) >= getRequiredXp(stack, id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Note: this intentionally does NOT bypass on instabuild — callers apply the creative bypass
     * themselves, because some of them (e.g. {@link #canActivateJutsu}) still require a real,
     * registered jutsu even in creative.
     */
    @Nullable
    private static UseDenyReason firstDenyReason(ItemStack stack, @Nullable ResourceLocation jutsuId, Player player) {
        if (jutsuId == null || !isRegistered(jutsuId) || !JutsuStackOps.getJutsuIds(stack).contains(jutsuId)) {
            return UseDenyReason.INVALID_JUTSU;
        }
        if (!JutsuStackOps.matchesOwner(stack, player)) {
            return UseDenyReason.NOT_OWNER;
        }
        if (!JutsuStackOps.isJutsuEnabled(stack, jutsuId)) {
            return UseDenyReason.DISABLED;
        }
        if (!player.getData(AttachmentRegistry.PLAYER_PROCESS).isNinja()) {
            return UseDenyReason.NOT_NINJA;
        }
        if (JutsuStackOps.getXp(stack, jutsuId) < getRequiredXp(stack, jutsuId)) {
            return UseDenyReason.INSUFFICIENT_XP;
        }
        return null;
    }

    public static UseGate validateUse(ItemStack stack, Player player, Level level) {
        if (!level.isClientSide()) {
            JutsuStackOps.claimOwnerIfAbsent(stack, player);
        }
        ResourceLocation currentId = JutsuStackOps.getCurrentJutsuId(stack);
        if (currentId == null || !isRegistered(currentId)) {
            return UseGate.FAIL;
        }
        if (player.getAbilities().instabuild) {
            return UseGate.ALLOW;
        }
        if (firstDenyReason(stack, currentId, player) != null) {
            return UseGate.FAIL;
        }
        return validateCooldown(stack, currentId, player, level.getGameTime());
    }

    @Nullable
    public static UseDenyReason diagnoseUseDeny(ItemStack stack, Player player) {
        if (player.getAbilities().instabuild) {
            return null;
        }
        return firstDenyReason(stack, JutsuStackOps.getCurrentJutsuId(stack), player);
    }

    public static void notifyUseDeny(Player player, ItemStack stack) {
        if (player == null || player.level().isClientSide()) {
            return;
        }
        UseDenyReason reason = diagnoseUseDeny(stack, player);
        if (reason != null) {
            player.displayClientMessage(reason.message(), true);
        }
    }

    public static boolean validateRelease(ItemStack stack, Player player, Level level) {
        ResourceLocation currentId = JutsuStackOps.getCurrentJutsuId(stack);
        if (currentId == null || !isRegistered(currentId)) {
            return false;
        }
        if (player.getAbilities().instabuild) {
            return true;
        }
        if (firstDenyReason(stack, currentId, player) != null) {
            return false;
        }
        return validateCooldown(stack, currentId, player, level.getGameTime()) == UseGate.ALLOW;
    }

    public static InteractionResult canActivateJutsu(ItemStack stack, ResourceLocation jutsuId, Player player) {
        if (!player.level().isClientSide()) {
            JutsuStackOps.claimOwnerIfAbsent(stack, player);
        }
        UseDenyReason reason = firstDenyReason(stack, jutsuId, player);
        if (player.getAbilities().instabuild) {
            return reason == UseDenyReason.INVALID_JUTSU ? InteractionResult.FAIL : InteractionResult.SUCCESS;
        }
        if (reason != null) {
            return InteractionResult.FAIL;
        }
        UseGate gate = validateCooldown(stack, jutsuId, player, player.level().getGameTime());
        return switch (gate) {
            case ALLOW -> InteractionResult.SUCCESS;
            case PASS -> InteractionResult.PASS;
            case FAIL -> InteractionResult.FAIL;
        };
    }

    private static UseGate validateCooldown(ItemStack stack, ResourceLocation jutsuId, Player player, long gameTime) {
        long cooldownEnd = JutsuStackOps.getCooldownEnd(stack, jutsuId);
        if (cooldownEnd > gameTime) {
            if (!player.level().isClientSide()) {
                player.displayClientMessage(
                    Component.translatable("chattext.cooldown.formatted", (cooldownEnd - gameTime) / 20.0d),
                    true
                );
            }
            return UseGate.PASS;
        }
        if (cooldownEnd < 0L) {
            return UseGate.FAIL;
        }
        return UseGate.ALLOW;
    }
}
