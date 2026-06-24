package narutoshinobicraft.client.fx.speck.register;

import com.mojang.serialization.Codec;
import narutoshinobicraft.client.fx.speck.runtime.look.SmokeFade;
import narutoshinobicraft.client.fx.speck.runtime.look.SpeckLook;
import narutoshinobicraft.common.registry.DispatchRegistry;

@SuppressWarnings("null")
public final class SpeckLookRegistry {
    private static final DispatchRegistry<SpeckLook> REGISTRY =
        new DispatchRegistry<>("speck_look", SpeckLook::codec);

    static {
        REGISTRY.register("narutoshinobicraft:smoke_fade", SmokeFade.CODEC);
    }

    public static final Codec<SpeckLook> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private SpeckLookRegistry() {}
}
