package narutoshinobicraft.common.jutsu.projectile.motion.force.impl;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import narutoshinobicraft.common.jutsu.projectile.motion.force.api.Force;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class HomingForce implements Force {
    public static final MapCodec<HomingForce> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.DOUBLE.optionalFieldOf("turn_rate", 0.15D).forGetter(f -> f.turnRate),
        Codec.DOUBLE.optionalFieldOf("range", 16.0D).forGetter(f -> f.range),
        Codec.DOUBLE.optionalFieldOf("cone_degrees", 40.0D).forGetter(f -> f.coneDegrees)
    ).apply(inst, HomingForce::new));

    private final double turnRate;
    private final double range;
    private final double coneDegrees;

    public HomingForce(double turnRate, double range, double coneDegrees) {
        this.turnRate = turnRate;
        this.range = range;
        this.coneDegrees = coneDegrees;
    }

    @Override
    public Vec3 apply(JutsuProjectile projectile, Vec3 velocity) {
        double speed = velocity.length();
        if (speed < 1.0E-6D) {
            return velocity;
        }
        Vec3 current = velocity.normalize();
        LivingEntity target = findTarget(projectile, current);
        if (target == null) {
            return velocity;
        }
        Vec3 desired = target.getBoundingBox().getCenter().subtract(projectile.position()).normalize();
        double t = Math.max(0.0D, Math.min(1.0D, this.turnRate));
        Vec3 blended = new Vec3(
            current.x + (desired.x - current.x) * t,
            current.y + (desired.y - current.y) * t,
            current.z + (desired.z - current.z) * t
        );
        if (blended.lengthSqr() < 1.0E-6D) {
            return velocity;
        }
        return blended.normalize().scale(speed);
    }

    private LivingEntity findTarget(JutsuProjectile projectile, Vec3 heading) {
        Entity owner = projectile.getOwner();
        AABB box = projectile.getBoundingBox().inflate(this.range);
        List<LivingEntity> candidates = projectile.level().getEntitiesOfClass(
            LivingEntity.class, box, e -> e.isAlive() && e != owner);
        double cosLimit = Math.cos(Math.toRadians(this.coneDegrees));
        Vec3 origin = projectile.position();
        LivingEntity nearest = null;
        double bestSq = Double.MAX_VALUE;
        for (LivingEntity candidate : candidates) {
            Vec3 toTarget = candidate.getBoundingBox().getCenter().subtract(origin);
            double distSq = toTarget.lengthSqr();
            if (distSq < 1.0E-6D) {
                continue;
            }
            if (toTarget.scale(1.0D / Math.sqrt(distSq)).dot(heading) < cosLimit) {
                continue;
            }
            if (distSq < bestSq) {
                bestSq = distSq;
                nearest = candidate;
            }
        }
        return nearest;
    }

    @Override
    public MapCodec<? extends Force> codec() {
        return CODEC;
    }
}

