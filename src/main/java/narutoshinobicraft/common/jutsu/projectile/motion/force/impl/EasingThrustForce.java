package narutoshinobicraft.common.jutsu.projectile.motion.force.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import narutoshinobicraft.common.jutsu.projectile.motion.force.api.Force;
import narutoshinobicraft.common.jutsu.projectile.motion.math.EasingCurves;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class EasingThrustForce implements Force {
    public static final MapCodec<EasingThrustForce> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.DOUBLE.optionalFieldOf("acceleration", 0.1D).forGetter(f -> f.acceleration),
        Codec.DOUBLE.optionalFieldOf("max_speed", 2.0D).forGetter(f -> f.maxSpeed),
        Codec.INT.optionalFieldOf("duration_ticks", 0).forGetter(f -> f.durationTicks),
        EasingCurves.CODEC.optionalFieldOf("curve", EasingCurves.EASE_IN_OUT_CUBIC).forGetter(f -> f.curve)
    ).apply(inst, EasingThrustForce::new));

    private final double acceleration;
    private final double maxSpeed;
    private final int durationTicks;
    private final EasingCurves curve;

    public EasingThrustForce(double acceleration, double maxSpeed, int durationTicks, EasingCurves curve) {
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.durationTicks = durationTicks;
        this.curve = curve == null ? EasingCurves.EASE_IN_OUT_CUBIC : curve;
    }

    @Override
    public Vec3 apply(JutsuProjectile projectile, Vec3 velocity) {
        Vec3 heading = velocity.lengthSqr() > 1.0E-6D ? velocity.normalize() : projectile.getShootVector();
        int duration = this.durationTicks > 0 ? this.durationTicks : projectile.getLifetimeTicks();
        double progress = duration <= 0 ? 1.0D : Mth.clamp((double) projectile.getProjectileAge() / duration, 0.0D, 1.0D);
        double scaledAcceleration = this.acceleration * this.curve.apply(progress);

        Vec3 next = velocity.add(heading.scale(scaledAcceleration));
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
