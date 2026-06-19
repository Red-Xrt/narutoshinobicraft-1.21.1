package narutoshinobicraft.client.particle.registry;

import com.mojang.serialization.Codec;
import narutoshinobicraft.client.particle.api.ParticleBehavior;
import narutoshinobicraft.client.particle.behavior.SmokeBehavior;
import narutoshinobicraft.common.registry.DispatchRegistry;


@SuppressWarnings("null")
public final class BehaviorRegistry {
    private static final DispatchRegistry<ParticleBehavior> REGISTRY =
        new DispatchRegistry<>("behavior", ParticleBehavior::codec);

    static {
        REGISTRY.register("narutoshinobicraft:smoke_visuals", SmokeBehavior.CODEC);
        // You can add more behaviors in here!
    }

    /** JSON dispatch codec for any behavior, keyed on the "type" field. */
    public static final Codec<ParticleBehavior> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private BehaviorRegistry() {}
}
