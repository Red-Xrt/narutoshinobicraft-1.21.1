package narutoshinobicraft.common.jutsu.helpers;

import narutoshinobicraft.client.particle.vfx.JutsuVfxSpawner;
import narutoshinobicraft.common.registry.SoundRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@SuppressWarnings("null")
public final class JutsuChargeEffects {
    private static final ResourceLocation CHAKRA_CHARGE_ID = ResourceLocation.parse("narutoshinobicraft:chakra_charge");

    private JutsuChargeEffects() {}

    public static void tick(Level level, LivingEntity entity, int timeLeft) {
        if (level.isClientSide()) {
            // Client only: resolve the charge VFX id locally and spawn it. JutsuVfxSpawner is a
            // client class but is never referenced on the server because this branch never runs there,
            // so there is no sidedness hazard (no @OnlyIn member is called from common code).
            JutsuVfxSpawner.spawnChargeVfx(entity, CHAKRA_CHARGE_ID);
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
