package narutoshinobicraft.common.jutsu.projectile.motion.force.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import narutoshinobicraft.common.jutsu.projectile.motion.force.api.Force;
import net.minecraft.world.phys.Vec3;

/**
 * Self-propulsion: accelerates the projectile along its current heading up to a max speed. Mirrors the
 * original mod's no-gravity branch where acceleration was added along the aim each tick
 * ({@code accel = dir * 0.1; motion += accel}). If the projectile is (nearly) still, it falls back to
 * the owner's aim so a freshly spawned projectile still launches forward.
 */
@SuppressWarnings("null")
public final class ThrustForce implements Force {
    public static final MapCodec<ThrustForce> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.DOUBLE.optionalFieldOf("acceleration", 0.1D).forGetter(f -> f.acceleration),
        Codec.DOUBLE.optionalFieldOf("max_speed", 2.0D).forGetter(f -> f.maxSpeed)
    ).apply(inst, ThrustForce::new));

    private final double acceleration;
    private final double maxSpeed;

    public ThrustForce(double acceleration, double maxSpeed) {
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public Vec3 apply(JutsuProjectile projectile, Vec3 velocity) {
        Vec3 heading = velocity.lengthSqr() > 1.0E-6D ? velocity.normalize() : projectile.getShootVector();
        Vec3 next = velocity.add(heading.scale(this.acceleration));
        if (next.length() > this.maxSpeed) {
            next = next.normalize().scale(this.maxSpeed);
        }
        return next;
    }

    @Override
    public MapCodec<? extends Force> codec() {
        return CODEC;
    }
}
