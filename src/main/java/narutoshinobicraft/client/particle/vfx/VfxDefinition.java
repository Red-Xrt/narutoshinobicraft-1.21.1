package narutoshinobicraft.client.particle.vfx;

import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import narutoshinobicraft.client.particle.vfx.emitter.impl.BurstEmitter;
import narutoshinobicraft.client.particle.vfx.emitter.impl.ChargeAuraEmitter;
import narutoshinobicraft.client.particle.vfx.emitter.api.ParticleEmitter;

@SuppressWarnings("null")
public record VfxDefinition(
    JutsuParticleOptions particle,
    ParticleEmitter castEmitter,
    ParticleEmitter chargeEmitter
) {
    public static VfxDefinition of(JutsuParticleOptions particle) {
        return new VfxDefinition(particle, BurstEmitter.DEFAULT, ChargeAuraEmitter.DEFAULT);
    }

    public static VfxDefinition of(JutsuParticleOptions particle, ParticleEmitter castEmitter, ParticleEmitter chargeEmitter) {
        return new VfxDefinition(
            particle,
            castEmitter != null ? castEmitter : BurstEmitter.DEFAULT,
            chargeEmitter != null ? chargeEmitter : ChargeAuraEmitter.DEFAULT
        );
    }
}
