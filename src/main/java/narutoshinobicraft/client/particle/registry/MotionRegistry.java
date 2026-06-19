package narutoshinobicraft.client.particle.registry;

import com.mojang.serialization.Codec;

import narutoshinobicraft.client.particle.api.ParticleMotion;
import narutoshinobicraft.client.particle.motion.LinearMotion;
import narutoshinobicraft.common.registry.DispatchRegistry;

@SuppressWarnings("null")
public final class MotionRegistry {
    private static final DispatchRegistry<ParticleMotion> REGISTRY =
        new DispatchRegistry<>("motion", ParticleMotion::codec);

    static {
        REGISTRY.register("narutoshinobicraft:linear", LinearMotion.CODEC);
        // You can add more motion in here!
    }

    /** JSON dispatch codec for any motion, keyed on the "type" field. */
    public static final Codec<ParticleMotion> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private MotionRegistry() {}
}
