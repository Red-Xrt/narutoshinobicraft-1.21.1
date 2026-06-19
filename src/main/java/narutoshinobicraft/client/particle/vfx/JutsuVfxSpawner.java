package narutoshinobicraft.client.particle.vfx;

import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Client-side bridge between the Jutsu Registry (logic) and the VFX Library (visuals).
 *
 * <p>Principle 3: the network only carries a String ID. This class is the local lookup step that
 * turns that ID into a fully-assembled {@link JutsuParticleOptions} (already composed from
 * motion + behaviors by the resource pack) and spawns it on the client. No particle data ever
 * crosses the wire, so there is nothing to desync.
 */
@SuppressWarnings("null")
public final class JutsuVfxSpawner {
    private static final int DEFAULT_BURST = 24;
    // Charge aura tuning, mirrored from the original mod's onUsingTick + Particles.Smoke:
    // a dense, short-lived cloud hugging the body. Density here, lifetime/drag live in the VFX JSON.
    private static final int CHARGE_BURST = 30;
    private static final double CHARGE_RADIUS = 0.2d;
    private static final double CHARGE_RISE = 0.5d;

    private JutsuVfxSpawner() {}

    public static void spawnCastVfx(LivingEntity caster, ResourceLocation vfxId) {
        if (caster == null || vfxId == null) {
            return;
        }
        JutsuParticleOptions vfx = VfxManager.VFX_LIBRARY.get(vfxId);
        if (vfx == null) {
            // ID not present in the loaded resource pack(s): a designer typo or a pack mismatch.
            // Fail soft so a missing visual never breaks gameplay.
            return;
        }

        Level level = caster.level();
        RandomSource random = level.random;
        Vec3 look = caster.getLookAngle();
        double originX = caster.getX();
        double originY = caster.getY() + caster.getBbHeight() * 0.6d;
        double originZ = caster.getZ();

        for (int i = 0; i < DEFAULT_BURST; i++) {
            double spread = 0.25d;
            double vx = look.x * 0.3d + (random.nextDouble() - 0.5d) * spread;
            double vy = look.y * 0.3d + (random.nextDouble() - 0.5d) * spread;
            double vz = look.z * 0.3d + (random.nextDouble() - 0.5d) * spread;

            level.addParticle(
                vfx,
                originX + (random.nextDouble() - 0.5d) * 0.4d,
                originY + (random.nextDouble() - 0.5d) * 0.4d,
                originZ + (random.nextDouble() - 0.5d) * 0.4d,
                vx, vy, vz
            );
        }
    }

    /**
     * Per-tick "charging up" aura: a small cloud of particles drifting upward around the caster.
     * Same fail-soft contract as {@link #spawnCastVfx}: a missing VFX id never breaks the charge tick.
     */
    public static void spawnChargeVfx(LivingEntity caster, ResourceLocation vfxId) {
        if (caster == null || vfxId == null) {
            return;
        }
        JutsuParticleOptions vfx = VfxManager.VFX_LIBRARY.get(vfxId);
        if (vfx == null) {
            return;
        }

        Level level = caster.level();
        RandomSource random = level.random;
        double baseX = caster.getX();
        double baseY = caster.getY();
        double baseZ = caster.getZ();

        for (int i = 0; i < CHARGE_BURST; i++) {
            // Gaussian horizontal scatter = a soft round cloud (denser at the core, thinning out),
            // seeded at the feet. They cover the body as the upward motion carries them up.
            double offsetX = random.nextGaussian() * CHARGE_RADIUS;
            double offsetZ = random.nextGaussian() * CHARGE_RADIUS;
            // Per-particle rise jitter (+-15%) so the aura shimmers instead of moving as one sheet.
            double upward = CHARGE_RISE * (0.85d + random.nextDouble() * 0.3d);

            level.addParticle(
                vfx,
                baseX + offsetX,
                baseY,
                baseZ + offsetZ,
                0.0d, upward, 0.0d
            );
        }
    }
}
