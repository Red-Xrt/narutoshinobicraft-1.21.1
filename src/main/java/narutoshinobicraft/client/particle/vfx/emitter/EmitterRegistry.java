package narutoshinobicraft.client.particle.vfx.emitter;

import narutoshinobicraft.common.registry.DispatchRegistry;
import com.mojang.serialization.Codec;

public final class EmitterRegistry {
    private static final DispatchRegistry<ParticleEmitter> REGISTRY =
        new DispatchRegistry<>("emitter", ParticleEmitter::codec);

    static {
        REGISTRY.register("narutoshinobicraft:burst", BurstEmitter.CODEC);
        REGISTRY.register("narutoshinobicraft:charge_aura", ChargeAuraEmitter.CODEC);
    }

    public static final Codec<ParticleEmitter> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private EmitterRegistry() {}
}
