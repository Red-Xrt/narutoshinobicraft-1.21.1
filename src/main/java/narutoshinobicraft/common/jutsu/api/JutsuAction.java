package narutoshinobicraft.common.jutsu.api;

import narutoshinobicraft.common.jutsu.stack.JutsuContext;

import net.minecraft.world.item.ItemStack;

public interface JutsuAction {
    boolean execute(JutsuContext context);

    default boolean isActivated(ItemStack stack) {
        return false;
    }

    default float getPower(ItemStack stack) {
        return 0.0f;
    }
}
