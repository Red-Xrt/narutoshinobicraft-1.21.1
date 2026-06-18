package narutoshinobicraft.common.jutsu.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface JutsuRender {
    public default void onCast(Level level, LivingEntity entity, ItemStack stack) {};
}
