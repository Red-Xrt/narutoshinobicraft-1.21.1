package narutoshinobicraft.common.jutsu.helpers;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import narutoshinobicraft.common.registry.SoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("null")
public final class JutsuChargeEffects {
    private static final int PARTICLE_COUNT = 12;
    private JutsuChargeEffects() {}

    public static void tick(Level level, LivingEntity entity, int timeLeft) {
        if (level.isClientSide()) {
            spawnChargingParticles(level, entity);
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

    @OnlyIn(Dist.CLIENT)
    private static void spawnChargingParticles(Level level, LivingEntity entity) {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double offsetX = (level.random.nextDouble() - 0.5d) * 0.4d;
            double offsetY = level.random.nextDouble() * 0.5d;
            double offsetZ = (level.random.nextDouble() - 0.5d) * 0.4d;

            level.addParticle(
                ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0x6AD1FF),
                entity.getX() + offsetX,
                entity.getY() + offsetY,
                entity.getZ() + offsetZ,
                0.0d,
                0.5d,
                0.0d
            );
        }
    }
}