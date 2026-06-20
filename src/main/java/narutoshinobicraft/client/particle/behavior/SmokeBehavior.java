package narutoshinobicraft.client.particle.behavior;

import com.mojang.serialization.MapCodec;

import narutoshinobicraft.client.particle.GenericJutsuParticle;
import narutoshinobicraft.client.particle.api.ParticleBehavior;
import net.minecraft.util.Mth;

public class SmokeBehavior implements ParticleBehavior {
    public static final MapCodec<SmokeBehavior> CODEC = MapCodec.unit(SmokeBehavior::new);

    public SmokeBehavior() {}

    @Override
    public void tick(GenericJutsuParticle particle) {
        float f = (float)particle.getAge() / (float)particle.getLifetime();
        float targetScale = Mth.clamp(f * 32.0F, 0.0F, 1.0F);
        particle.setParticleScale(particle.getInitialScale() * targetScale);

        float targetAlpha = particle.getInitialAlpha() * (1.0F - f * f * 0.5F);
        particle.setAlpha(targetAlpha);
    }

    @Override
    public MapCodec<? extends ParticleBehavior> codec() {
        return CODEC;
    }
}

