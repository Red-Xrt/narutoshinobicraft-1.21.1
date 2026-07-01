package narutoshinobicraft.common.spatial.query.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("null")
public final class EntityVisibilityQueries {
    private static final double FOV_HALF_ANGLE_DEGREES = 85.0d;

    private EntityVisibilityQueries() {}

    public static boolean isEntityInFOV(LivingEntity looker, Entity target) {
        if (looker == null || target == null) {
            return false;
        }
        double yaw = -Mth.atan2(target.getX() - looker.getX(), target.getZ() - looker.getZ()) * Mth.RAD_TO_DEG;
        return Math.abs(Mth.wrapDegrees(yaw - looker.getYHeadRot())) < FOV_HALF_ANGLE_DEGREES
            && looker.hasLineOfSight(target);
    }
}
