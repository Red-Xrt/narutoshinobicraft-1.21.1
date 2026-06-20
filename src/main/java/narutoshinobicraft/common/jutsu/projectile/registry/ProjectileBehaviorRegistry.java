package narutoshinobicraft.common.jutsu.projectile.registry;

import com.mojang.serialization.Codec;

import narutoshinobicraft.common.jutsu.projectile.behavior.ProjectileBehavior;
import narutoshinobicraft.common.jutsu.projectile.behavior.ScaleOverTimeBehavior;
import narutoshinobicraft.common.jutsu.projectile.behavior.SpiralBehavior;
import narutoshinobicraft.common.registry.DispatchRegistry;

/**
 * Registry of {@link ProjectileBehavior} overlays — the customization surface where addons make their
 * jutsu visually/feel distinct on top of shared physics.
 */
@SuppressWarnings("null")
public final class ProjectileBehaviorRegistry {
    private static final DispatchRegistry<ProjectileBehavior> REGISTRY =
        new DispatchRegistry<>("projectile_behavior", ProjectileBehavior::codec);

    static {
        REGISTRY.register("narutoshinobicraft:spiral", SpiralBehavior.CODEC);
        REGISTRY.register("narutoshinobicraft:scale_over_time", ScaleOverTimeBehavior.CODEC);
    }

    /** JSON dispatch codec for any behavior, keyed on the "type" field. */
    public static final Codec<ProjectileBehavior> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private ProjectileBehaviorRegistry() {}
}
