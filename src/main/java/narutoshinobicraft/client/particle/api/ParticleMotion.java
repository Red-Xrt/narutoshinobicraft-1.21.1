package narutoshinobicraft.client.particle.api;

import com.mojang.serialization.MapCodec;

import narutoshinobicraft.client.particle.GenericJutsuParticle;

public interface ParticleMotion {
    void apply(GenericJutsuParticle particle);

    MapCodec<? extends ParticleMotion> codec();
}
