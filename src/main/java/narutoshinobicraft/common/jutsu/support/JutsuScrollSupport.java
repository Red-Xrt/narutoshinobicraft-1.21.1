package narutoshinobicraft.common.jutsu.support;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.data.component.JutsuStackOps;
import narutoshinobicraft.common.jutsu.JutsuScrollItem;
import narutoshinobicraft.common.jutsu.api.JutsuDefinition;
import narutoshinobicraft.common.jutsu.cast.JutsuCastValidator;
import narutoshinobicraft.common.jutsu.helpers.JutsuCooldownHelper;
import narutoshinobicraft.common.jutsu.helpers.JutsuNames;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SuppressWarnings("null")
public final class JutsuScrollSupport {
    private JutsuScrollSupport() {}

    public static boolean isJutsuScroll(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof JutsuScrollItem;
    }

    public static void applyDefinitionCooldown(ItemStack stack, Player player, JutsuDefinition definition) {
        if (!isJutsuScroll(stack) || definition == null || definition.defaultCooldownTicks() <= 0L) {
            return;
        }
        setCurrentJutsuCooldown(stack, player, definition.defaultCooldownTicks());
    }

    public static void setCurrentJutsuCooldown(ItemStack stack, Player player, long cooldownTicks) {
        if (!isJutsuScroll(stack) || player == null) {
            return;
        }
        ResourceLocation currentId = JutsuStackOps.getCurrentJutsuId(stack);
        if (currentId == null) {
            return;
        }
        long modified = (long) JutsuCooldownHelper.modifiedCooldown((double) cooldownTicks, player);
        JutsuStackOps.setCooldownFromNow(stack, currentId, player.level(), modified);
    }

    public static void setCurrentJutsuCooldown(ItemStack stack, Level level, long cooldownTicks) {
        if (!isJutsuScroll(stack)) {
            return;
        }
        ResourceLocation currentId = JutsuStackOps.getCurrentJutsuId(stack);
        if (currentId != null) {
            JutsuStackOps.setCooldownFromNow(stack, currentId, level, cooldownTicks);
        }
    }

    public static void logBattleXp(Player player) {
        ItemStack stack = findOwnedHeldScroll(player);
        if (stack.isEmpty()) {
            return;
        }
        ResourceLocation currentId = JutsuStackOps.getCurrentJutsuId(stack);
        if (JutsuStackOps.getCurrentJutsuXp(stack) < JutsuCastValidator.getRequiredXp(stack, currentId)) {
            JutsuStackOps.addCurrentJutsuXp(stack, 1);
        }
    }

    public static void addBattleXp(Player player, int xp) {
        ItemStack stack = findOwnedHeldScroll(player);
        if (!stack.isEmpty()) {
            JutsuStackOps.addCurrentJutsuXp(stack, xp);
        }
    }

    public static @Nullable ResourceLocation getCurrentJutsuId(ItemStack stack) {
        return JutsuStackOps.getCurrentJutsuId(stack);
    }

    public static List<ResourceLocation> getActivatedJutsuIds(ItemStack stack) {
        List<ResourceLocation> activated = new ArrayList<>();
        for (ResourceLocation id : JutsuStackOps.getJutsuIds(stack)) {
            JutsuRegistry.JutsuEntry entry = JutsuRegistry.getJutsu(id);
            if (entry != null && entry.action().isActivated(stack)) {
                activated.add(id);
            }
        }
        return activated;
    }

    public static boolean switchNextJutsu(ItemStack stack, LivingEntity entity) {
        boolean switched = JutsuStackOps.switchNextUsableJutsu(
            stack,
            (scroll, jutsuId) -> JutsuCastValidator.canUseJutsu(scroll, jutsuId, entity)
        );

        if (switched && entity instanceof Player player && !entity.level().isClientSide()) {
            ResourceLocation currentId = JutsuStackOps.getCurrentJutsuId(stack);
            if (currentId != null) {
                player.displayClientMessage(JutsuNames.display(currentId), true);
            }
        }
        return switched;
    }

    public static ItemStack findHeldScroll(Player player) {
        ItemStack main = player.getMainHandItem();
        if (isJutsuScroll(main)) {
            return main;
        }

        ItemStack off = player.getOffhandItem();
        if (isJutsuScroll(off)) {
            return off;
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack findOwnedHeldScroll(Player player) {
        ItemStack stack = findHeldScroll(player);

        if (stack.isEmpty() || !JutsuStackOps.matchesOwner(stack, player)) {
            return ItemStack.EMPTY;
        }

        return stack;
    }

    public static void enableJutsu(ItemStack stack, ResourceLocation jutsuId, boolean enabled) {
        if (!JutsuStackOps.getJutsuIds(stack).contains(jutsuId)) {
            NarutoShinobiCraft.LOGGER.warn("Jutsu [{}] does not belong on this scroll", jutsuId);
            return;
        }

        JutsuStackOps.enableJutsu(stack, jutsuId, enabled);
    }
}