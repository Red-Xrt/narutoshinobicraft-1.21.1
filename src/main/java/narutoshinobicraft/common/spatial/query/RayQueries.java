package narutoshinobicraft.common.spatial.query;

import java.util.function.Predicate;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public class RayQueries {
    public static HitResult raytraceBlock(Entity entity, double range, ClipContext.Fluid fluid){
        Vec3 vector1 = entity.getEyePosition(1f);
        Vec3 vector2 = vector1.add(entity.getViewVector(0).scale(range));
        return entity.level().clip(new ClipContext(vector1, vector2, ClipContext.Block.COLLIDER, fluid, entity));
    }

    public static HitResult objectEntityLookingAt(Entity entity, double range, double hitboxGrow, ClipContext.Fluid fluid, Predicate<Entity> filter){
        Vec3 start = entity.getEyePosition(1f);
        Vec3 end = start.add(entity.getViewVector(0).scale(range));
        EntityHitResult  targetEntity = null;

        HitResult objecHitResult = raytraceBlock(entity, range, fluid);

        Vec3 searchEnd = objecHitResult.getType() != HitResult.Type.MISS ? objecHitResult.getLocation() : end;

        AABB searchBox = entity.getBoundingBox().expandTowards(searchEnd.subtract(start)).inflate(hitboxGrow);
        targetEntity = ProjectileUtil.getEntityHitResult(entity.level(), entity, start, searchEnd, searchBox, filter, (float) hitboxGrow);

        return targetEntity != null ? targetEntity : objecHitResult;
    }
}
