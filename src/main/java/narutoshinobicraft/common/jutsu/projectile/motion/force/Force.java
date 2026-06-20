package narutoshinobicraft.common.jutsu.projectile.motion.force;

import com.mojang.serialization.MapCodec;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import net.minecraft.world.phys.Vec3;

/**
 * The smallest Lego brick of the movement system: a pure-math contributor to a projectile's velocity.
 *
 * <p>This is the "thuần túy toán" foundation the Core layer ships (gravity, drag, thrust, homing...).
 * Addons remix these with different params from JSON, or register brand-new forces in Java — then
 * everyone can reference them by id. Forces are stacked inside {@link PhysicsMotion} and applied in
 * order each tick.
 *
 * <p>Implementations must be pure: take the current velocity, return the new velocity. No side effects
 * on world state (that's what behaviors/effects are for).
 */
public interface Force {
    /**
     * @param projectile the moving entity (for context: position, owner, age...)
     * @param velocity   the current velocity this tick
     * @return the new velocity after this force is applied
     */
    Vec3 apply(JutsuProjectile projectile, Vec3 velocity);

    MapCodec<? extends Force> codec();
}
