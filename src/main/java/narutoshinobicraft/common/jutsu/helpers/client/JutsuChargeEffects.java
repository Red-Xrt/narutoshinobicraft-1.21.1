package narutoshinobicraft.common.jutsu.helpers.client;

import narutoshinobicraft.common.registry.SoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SuppressWarnings("null")
public final class JutsuChargeEffects {

    private JutsuChargeEffects() {}

    public static void tick(Level level, LivingEntity entity, ItemStack stack, int timeLeft) {
        if (level.isClientSide()) {
            JutsuClientEffects.playScrollAura(entity, ChakraVisualResolver.scrollAuraFor(entity));
        } else if (timeLeft % 10 == 0) {
            level.playSound(
                null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                SoundRegistry.CHARGING_CHAKRA.get(),
                SoundSource.PLAYERS,
                0.05F,
                level.random.nextFloat() + 0.5F
            );
        }
    }
}
