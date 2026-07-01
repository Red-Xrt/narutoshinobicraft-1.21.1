package narutoshinobicraft.common.spatial.query.ray;

import java.util.function.Predicate;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import javax.annotation.Nullable;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings({"null"})
public class RayQueries {
    public static HitResult raytraceBlock(LivingEntity entity, double range, ClipContext.Fluid fluid){
        Vec3 vector1 = entity.getEyePosition(1f);
        Vec3 vector2 = vector1.add(entity.getViewVector(0).scale(range));
        return entity.level().clip(new ClipContext(vector1, vector2, ClipContext.Block.COLLIDER, fluid, entity));
    }

	public static HitResult objectEntityLookingAt(LivingEntity entity, double range) {
		return objectEntityLookingAt(entity, range, false, (Predicate<LivingEntity>)null);
	}

	public static HitResult objectEntityLookingAt(LivingEntity entity, double range, double bbGrow) {
		return objectEntityLookingAt(entity, range, bbGrow, false, (Predicate<LivingEntity>)null);
	}

	public static HitResult objectEntityLookingAt(LivingEntity entity, double range, @Nullable LivingEntity excludeEntity) {
		return objectEntityLookingAt(entity, range, false, excludeEntity);
	}

	public static HitResult objectEntityLookingAt(LivingEntity entity, double range, boolean stopOnLiquid) {
		return objectEntityLookingAt(entity, range, stopOnLiquid, (Predicate<LivingEntity>)null);
	}

	public static HitResult objectEntityLookingAt(LivingEntity entity, double range, boolean stopOnLiquid, @Nullable LivingEntity excludeEntity) {
		return objectEntityLookingAt(entity, range, stopOnLiquid, entityParam -> entityParam != null && !entityParam.equals(excludeEntity) && entityParam.isPickable());
	}

	public static HitResult objectEntityLookingAt(LivingEntity entity, double range, boolean stopOnLiquid, @Nullable Predicate<LivingEntity> filter) {
		return objectEntityLookingAt(entity, range, 0.0d, stopOnLiquid, filter);
	}

	public static HitResult objectEntityLookingAt(LivingEntity entity, double range, double hitboxGrow, boolean stopOnLiquid, @Nullable Predicate<LivingEntity> filter) {
		ClipContext.Fluid fluid = stopOnLiquid ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE;
		return objectEntityLookingAt(entity, range, hitboxGrow, fluid, filter);
	}

    public static HitResult objectEntityLookingAt(LivingEntity entity, double range, double hitboxGrow, ClipContext.Fluid fluid, @Nullable Predicate<LivingEntity> filter){
        Vec3 start = entity.getEyePosition(1f);
        Vec3 end = start.add(entity.getLookAngle().scale(range));
        EntityHitResult targetEntity = null;
        HitResult objecHitResult = raytraceBlock(entity, range, fluid);
        Vec3 searchEnd = objecHitResult.getType() != HitResult.Type.MISS ? objecHitResult.getLocation() : end;
        AABB searchBox = entity.getBoundingBox().expandTowards(searchEnd.subtract(start)).inflate(hitboxGrow);
        
        Predicate<Entity> minecraftFilter = e -> {
            if (!(e instanceof LivingEntity)) {
                return false; 
            }
            
            LivingEntity livingTarget = (LivingEntity) e;
            
            if (filter == null) {
                return livingTarget.isPickable();
            }
            
            return filter.test(livingTarget);
        };

        targetEntity = ProjectileUtil.getEntityHitResult(entity.level(), entity, start, searchEnd, searchBox, minecraftFilter, (float) hitboxGrow);
        return targetEntity != null ? targetEntity : objecHitResult;
    }
}
