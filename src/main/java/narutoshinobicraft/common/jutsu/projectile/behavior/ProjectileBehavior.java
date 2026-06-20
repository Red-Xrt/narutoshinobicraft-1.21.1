package narutoshinobicraft.common.jutsu.projectile.behavior;

import com.mojang.serialization.MapCodec;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import net.minecraft.world.phys.HitResult;

/**
 * Trục custom — the addon playground. Where {@link narutoshinobicraft.common.jutsu.projectile.motion.force.Force}
 * is the curated pure-math foundation, a behavior is the open-ended overlay that makes two physically
 * similar projectiles look/feel completely different: spiral wobble, grow-over-time, leave a trail,
 * pulse, spawn sub-particles, conditional logic...
 *
 * <p>IMPORTANT: behaviors are assembled from data and a single instance may be shared across many live
 * projectiles, so implementations MUST be stateless — derive everything from the projectile's own state
 * (age, position, velocity), never from mutable fields on the behavior.
 */
public interface ProjectileBehavior {
    default void start(JutsuProjectile projectile) {}

    /** Called every server tick, after the motion has moved the projectile. */
    void tick(JutsuProjectile projectile);

    /** Called when the projectile impacts something, before the on-hit effects resolve. */
    default void onHit(JutsuProjectile projectile, HitResult hit) {}

    MapCodec<? extends ProjectileBehavior> codec();
}
