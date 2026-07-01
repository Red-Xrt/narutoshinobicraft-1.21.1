package narutoshinobicraft.common.spatial.impulse.math;

import java.util.Optional;

import narutoshinobicraft.common.spatial.impulse.spec.ImpulseSpec;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class ImpulseMath {
    private static final double MIN_DIRECTION_LENGTH_SQR = 1.0E-8;

    private ImpulseMath() {}

    public static Optional<Vec3> computePushAway(
        Vec3 origin,
        Vec3 targetPosition,
        double sizeFactor,
        boolean onGround,
        Vec3 currentMotion,
        ImpulseSpec spec
    ) {
        Vec3 away = targetPosition.subtract(origin);
        double distance = away.length();
        if (distance > spec.range()) {
            return Optional.empty();
        }

        float scaledStrength = spec.strength() * (float) ((spec.range() - distance) * spec.distanceScale());
        double baseSpeed = Math.sqrt(2.0d / Math.max(sizeFactor, 1.0E-4));

        Vec3 direction = away.normalize().scale(baseSpeed);
        if (onGround && spec.liftOnGround() && direction.y < baseSpeed * 0.6d) {
            direction = direction.add(0.0d, baseSpeed * 0.6d, 0.0d);
        }

        Vec3 impulse = direction.scale(scaledStrength);
        if (spec.additive()) {
            impulse = impulse.add(currentMotion);
        }
        return Optional.of(impulse);
    }

    public static Vec3 computePullToward(Vec3 origin, Vec3 targetPosition, ImpulseSpec spec) {
        Vec3 toward = origin.subtract(targetPosition);
        double distance = toward.length();
        if (distance * distance < MIN_DIRECTION_LENGTH_SQR) {
            return Vec3.ZERO;
        }

        float scaledStrength = spec.strength() * (float) (distance * spec.distanceScale());
        return toward.normalize().scale(scaledStrength);
    }
}
