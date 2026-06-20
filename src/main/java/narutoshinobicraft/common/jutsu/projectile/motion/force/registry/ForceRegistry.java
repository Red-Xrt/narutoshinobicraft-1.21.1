package narutoshinobicraft.common.jutsu.projectile.motion.force.registry;

import com.mojang.serialization.Codec;

import narutoshinobicraft.common.jutsu.projectile.motion.force.impl.DragForce;
import narutoshinobicraft.common.jutsu.projectile.motion.force.api.Force;
import narutoshinobicraft.common.jutsu.projectile.motion.force.impl.EasingThrustForce;
import narutoshinobicraft.common.jutsu.projectile.motion.force.impl.GravityForce;
import narutoshinobicraft.common.jutsu.projectile.motion.force.impl.HomingForce;
import narutoshinobicraft.common.jutsu.projectile.motion.force.impl.ThrustForce;
import narutoshinobicraft.common.registry.DispatchRegistry;

/**
 * Tier-1 registry of the pure-math {@link Force} bricks. Add a new force here (Core layer) and it
 * becomes referenceable from any motion's JSON {@code forces} list.
 */
@SuppressWarnings("null")
public final class ForceRegistry {
    private static final DispatchRegistry<Force> REGISTRY = new DispatchRegistry<>("force", Force::codec);

    static {
        REGISTRY.register("narutoshinobicraft:gravity", GravityForce.CODEC);
        REGISTRY.register("narutoshinobicraft:drag", DragForce.CODEC);
        REGISTRY.register("narutoshinobicraft:thrust", ThrustForce.CODEC);
        REGISTRY.register("narutoshinobicraft:easing_thrust", EasingThrustForce.CODEC);
        REGISTRY.register("narutoshinobicraft:homing", HomingForce.CODEC);
    }

    /** JSON dispatch codec for any force, keyed on the "type" field. */
    public static final Codec<Force> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private ForceRegistry() {}
}
