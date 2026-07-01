package narutoshinobicraft.common.spatial.impulse.apply;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class EntityMotionOps {
    private EntityMotionOps() {}

    public static Vec3 getDeltaMotion(Entity entity) {
        return entity.getDeltaMovement();
    }

    public static Vec3 getMotion(Entity entity) {
        return getDeltaMotion(entity);
    }

    public static void setDeltaMotion(Entity entity, Vec3 motion) {
        entity.setDeltaMovement(motion);
        entity.hurtMarked = true;
    }

    public static void setVelocity(Entity entity, double motionX, double motionY, double motionZ) {
        setDeltaMotion(entity, new Vec3(motionX, motionY, motionZ));
    }

    public static void setVelocity(Entity entity, Vec3 motion) {
        setDeltaMotion(entity, motion);
    }
}
