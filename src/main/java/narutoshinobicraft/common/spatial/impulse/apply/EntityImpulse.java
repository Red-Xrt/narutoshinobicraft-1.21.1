package narutoshinobicraft.common.spatial.impulse.apply;

import java.util.Optional;

import narutoshinobicraft.common.spatial.impulse.math.ImpulseMath;
import narutoshinobicraft.common.spatial.impulse.spec.ImpulseSpec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class EntityImpulse {
    private EntityImpulse() {}

    public static boolean pushAway(Vec3 origin, Entity target, ImpulseSpec spec) {
        if (target == null || !target.isPickable()) {
            return false;
        }
        Optional<Vec3> velocity = ImpulseMath.computePushAway(
            origin,
            target.position(),
            resolveSizeFactor(target),
            target.onGround(),
            EntityMotionOps.getMotion(target),
            spec
        );
        velocity.ifPresent(motion -> EntityMotionOps.setVelocity(target, motion));
        return velocity.isPresent();
    }

    public static boolean pullToward(Vec3 origin, Entity target, ImpulseSpec spec) {
        if (target == null || !target.isPickable()) {
            return false;
        }
        Vec3 velocity = ImpulseMath.computePullToward(origin, target.position(), spec);
        if (velocity.lengthSqr() < 1.0E-8) {
            return false;
        }
        EntityMotionOps.setVelocity(target, velocity);
        return true;
    }

    private static double resolveSizeFactor(Entity target) {
        if (target instanceof LivingEntity living) {
            return living.getBbHeight();
        }
        AABB box = target.getBoundingBox();
        return (box.getXsize() + box.getYsize() + box.getZsize()) / 3.0d;
    }
}
