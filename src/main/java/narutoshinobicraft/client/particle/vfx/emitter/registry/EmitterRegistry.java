package narutoshinobicraft.client.particle.vfx.emitter.registry;

import com.mojang.serialization.Codec;

import narutoshinobicraft.client.particle.vfx.emitter.api.ParticleEmitter;
import narutoshinobicraft.client.particle.vfx.emitter.impl.BurstEmitter;
import narutoshinobicraft.client.particle.vfx.emitter.impl.ChargeAuraEmitter;
import narutoshinobicraft.common.registry.DispatchRegistry;

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
