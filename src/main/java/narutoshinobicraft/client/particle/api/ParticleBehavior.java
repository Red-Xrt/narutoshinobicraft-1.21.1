package narutoshinobicraft.client.particle.api;

import com.mojang.serialization.MapCodec;

import narutoshinobicraft.client.particle.GenericJutsuParticle;

public interface ParticleBehavior {
    void tick(GenericJutsuParticle particle);
    MapCodec<? extends ParticleBehavior> codec();
}
