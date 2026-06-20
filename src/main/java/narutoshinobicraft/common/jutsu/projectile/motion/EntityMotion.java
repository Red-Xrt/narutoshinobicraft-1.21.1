package narutoshinobicraft.common.jutsu.projectile.motion;

import com.mojang.serialization.MapCodec;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;

/**
 * Trục 1 — DI CHUYỂN. A motion is the top-level strategy that decides where the projectile is each
 * tick. It is server-authoritative: it mutates the entity's delta movement / position and vanilla
 * entity tracking syncs the result to clients (no custom packets needed).
 *
 * <p>Composition over inheritance: a projectile holds ONE {@code EntityMotion} (assembled from data),
 * never a {@code FireballMotion extends ProjectileMotion} hierarchy. The most reusable implementation
 * is {@link PhysicsMotion}, which is itself composed of a stack of pure-math
 * {@link narutoshinobicraft.common.jutsu.projectile.motion.force.Force Force}s.
 */
public interface EntityMotion {
    /** Called once when the projectile spawns (e.g. to set the initial velocity from the caster's aim). */
    default void start(JutsuProjectile projectile) {}

    /** Called every server tick to advance the projectile. */
    void tick(JutsuProjectile projectile);

    /** Variant codec, for the dispatch registry. */
    MapCodec<? extends EntityMotion> codec();
}
