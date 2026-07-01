package narutoshinobicraft.common.spatial.query.entity;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@SuppressWarnings("null")
public final class EntityVolumeQueries {
    private EntityVolumeQueries() {}

    @Nullable
    public static <T extends Entity> T findNearestWithinAabb(
        Level level,
        Class<T> entityClass,
        AABB bounds,
        Entity origin,
        @Nullable Predicate<? super T> filter
    ) {
        if (level == null || entityClass == null || bounds == null || origin == null) {
            return null;
        }

        T nearest = null;
        double bestDistanceSq = Double.MAX_VALUE;

        for (T candidate : level.getEntitiesOfClass(entityClass, bounds, filter == null ? entity -> true : filter)) {
            if (candidate == origin || candidate.isSpectator()) {
                continue;
            }
            double distanceSq = origin.distanceToSqr(candidate);
            if (distanceSq < bestDistanceSq) {
                bestDistanceSq = distanceSq;
                nearest = candidate;
            }
        }

        return nearest;
    }

    @Nullable
    public static LivingEntity findNearestSensibleLiving(Level level, AABB bounds, LivingEntity origin) {
        return findNearestWithinAabb(
            level,
            LivingEntity.class,
            bounds,
            origin,
            target -> target.isAlive() && target.isPickable() && origin.hasLineOfSight(target)
        );
    }
}
