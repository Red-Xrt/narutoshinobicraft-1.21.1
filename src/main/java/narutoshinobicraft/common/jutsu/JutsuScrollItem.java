package narutoshinobicraft.common.jutsu;

import java.util.List;

import narutoshinobicraft.common.data.component.JutsuStackOps;
import narutoshinobicraft.common.jutsu.cast.CastOutcome;
import narutoshinobicraft.common.jutsu.cast.JutsuCastExecutor;
import narutoshinobicraft.common.jutsu.cast.JutsuCastValidator;
import narutoshinobicraft.common.jutsu.cast.JutsuCastValidator.UseGate;
import narutoshinobicraft.common.jutsu.helpers.JutsuChargeEffects;
import narutoshinobicraft.common.jutsu.helpers.JutsuPowerCalculator;
import narutoshinobicraft.common.jutsu.support.JutsuScrollTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("null")
public class JutsuScrollItem extends Item {
    public JutsuScrollItem(Properties properties) {
        super(properties);
    }

    public static Properties defaultProperties() {
        return new Properties().stacksTo(1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        UseGate gate = JutsuCastValidator.validateUse(stack, player, level);
        return switch (gate) {
            case FAIL -> {
                if (!level.isClientSide()) {
                    JutsuCastValidator.notifyUseDeny(player, stack);
                }
                yield InteractionResultHolder.fail(stack);
            }
            case PASS -> InteractionResultHolder.pass(stack);
            case ALLOW -> {
                player.startUsingItem(hand);
                yield InteractionResultHolder.consume(stack);
            }
        };
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int timeLeft) {
        if (!level.isClientSide() && entity instanceof Player player) {
            float power = JutsuCastExecutor.resolvePower(stack, entity, timeLeft);
            if (!player.getAbilities().instabuild && power <= 0.0f) {
                player.stopUsingItem();
                return;
            }
            player.displayClientMessage(Component.literal(String.format("%.1f", power)), true);
        }
        JutsuChargeEffects.tick(level, entity, stack, timeLeft);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (level.isClientSide()) {
            return;
        }
        float power = JutsuCastExecutor.resolvePower(stack, entity, timeLeft);
        CastOutcome outcome = JutsuCastExecutor.execute(stack, entity, power);
        if (outcome.succeeded()) {
            JutsuStackOps.addCurrentJutsuXp(stack, 1);
        } else if (entity instanceof Player player) {
            outcome.notifyFailure(player);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return JutsuPowerCalculator.DEFAULT_MAX_USE_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack stack, Player player) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (!(entity instanceof Player player) || level.isClientSide()) {
            return;
        }
        JutsuStackOps.claimOwnerIfAbsent(stack, player);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        JutsuScrollTooltip.append(stack, tooltip, Minecraft.getInstance().player, flag);
    }
}
