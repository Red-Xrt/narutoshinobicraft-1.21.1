package narutoshinobicraft.client.particle.vfx.emitter;

import com.mojang.serialization.MapCodec;

import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface ParticleEmitter {
    void emit(Level level, LivingEntity caster, JutsuParticleOptions particle);

    MapCodec<? extends ParticleEmitter> codec();
}
