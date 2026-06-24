package narutoshinobicraft.client.fx.speck.register;

import com.mojang.serialization.Codec;
import narutoshinobicraft.client.fx.speck.runtime.motion.LinearMotion;
import narutoshinobicraft.client.fx.speck.runtime.motion.SpeckMotion;
import narutoshinobicraft.common.registry.DispatchRegistry;

@SuppressWarnings("null")
public final class SpeckMotionRegistry {
    private static final DispatchRegistry<SpeckMotion> REGISTRY =
        new DispatchRegistry<>("speck_motion", SpeckMotion::codec);

    static {
        REGISTRY.register("narutoshinobicraft:linear", LinearMotion.CODEC);
    }

    public static final Codec<SpeckMotion> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private SpeckMotionRegistry() {}
}
