package narutoshinobicraft.common.jutsu.projectile.registry;

import com.mojang.serialization.Codec;

import narutoshinobicraft.common.jutsu.projectile.motion.EntityMotion;
import narutoshinobicraft.common.jutsu.projectile.motion.PhysicsMotion;
import narutoshinobicraft.common.registry.DispatchRegistry;

/**
 * Tier-1 registry of {@link EntityMotion} strategies. {@code physics} (force-stack based) covers most
 * archetypes; bespoke strategies (beam, sky-dive, orbit-owner) get registered here later, each viewable
 * from JSON by id.
 */
@SuppressWarnings("null")
public final class EntityMotionRegistry {
    private static final DispatchRegistry<EntityMotion> REGISTRY =
        new DispatchRegistry<>("entity_motion", EntityMotion::codec);

    static {
        REGISTRY.register("narutoshinobicraft:physics", PhysicsMotion.CODEC);
    }

    /** JSON dispatch codec for any motion, keyed on the "type" field. */
    public static final Codec<EntityMotion> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private EntityMotionRegistry() {}
}
