package narutoshinobicraft.common.jutsu.projectile.motion.math;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@SuppressWarnings("null")
public final class DirectionMath {
    private static final double EPSILON = 1.0E-6D;

    private DirectionMath() {}

    public static Vec3 slerpDirection(Vec3 from, Vec3 to, float t) {
        t = Mth.clamp(t, 0.0F, 1.0F);
        if (from.lengthSqr() < EPSILON) {
            return to.lengthSqr() < EPSILON ? from : to.normalize();
        }
        if (to.lengthSqr() < EPSILON) {
            return from.normalize();
        }

        Vector3f start = new Vector3f((float) from.x, (float) from.y, (float) from.z).normalize();
        Vector3f end = new Vector3f((float) to.x, (float) to.y, (float) to.z).normalize();
        if (start.dot(end) < 0.0F) {
            end.negate();
        }

        if (t <= 0.0F) {
            return vec3(start);
        }
        if (t >= 1.0F) {
            return vec3(end);
        }

        Quaternionf rotation = new Quaternionf().rotationTo(start, end);
        Quaternionf partial = new Quaternionf().identity().slerp(rotation, t);
        Vector3f result = new Vector3f(start).rotate(partial);
        if (result.lengthSquared() < 1.0E-12F) {
            return nlerpDirection(from, to, t);
        }
        result.normalize();
        return vec3(result);
    }

    public static Vec3 nlerpDirection(Vec3 from, Vec3 to, float t) {
        t = Mth.clamp(t, 0.0F, 1.0F);
        Vec3 blended = new Vec3(
            from.x + (to.x - from.x) * t,
            from.y + (to.y - from.y) * t,
            from.z + (to.z - from.z) * t
        );
        return blended.lengthSqr() < EPSILON ? from : blended.normalize();
    }

    private static Vec3 vec3(Vector3f vector) {
        return new Vec3(vector.x, vector.y, vector.z);
    }
}
