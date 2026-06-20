package narutoshinobicraft.client.particle.vfx.emitter.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import narutoshinobicraft.client.particle.vfx.emitter.api.ParticleEmitter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@SuppressWarnings("null")
public final class ChargeAuraEmitter implements ParticleEmitter {
    public static final ChargeAuraEmitter DEFAULT = new ChargeAuraEmitter(30, 0.2d, 0.5d);

    public static final MapCodec<ChargeAuraEmitter> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.INT.optionalFieldOf("count", 30).forGetter(e -> e.count),
        Codec.DOUBLE.optionalFieldOf("radius", 0.2d).forGetter(e -> e.radius),
        Codec.DOUBLE.optionalFieldOf("rise", 0.5d).forGetter(e -> e.rise)
    ).apply(inst, ChargeAuraEmitter::new));

    private final int count;
    private final double radius;
    private final double rise;

    public ChargeAuraEmitter(int count, double radius, double rise) {
        this.count = count;
        this.radius = radius;
        this.rise = rise;
    }

    @Override
    public void emit(Level level, LivingEntity caster, JutsuParticleOptions particle) {
        RandomSource random = level.random;
        double baseX = caster.getX();
        double baseY = caster.getY();
        double baseZ = caster.getZ();

        for (int i = 0; i < this.count; i++) {
            double offsetX = random.nextGaussian() * this.radius;
            double offsetZ = random.nextGaussian() * this.radius;
            double upward = this.rise * (0.85d + random.nextDouble() * 0.3d);
            level.addParticle(particle, baseX + offsetX, baseY, baseZ + offsetZ, 0.0d, upward, 0.0d);
        }
    }

    @Override
    public MapCodec<? extends ParticleEmitter> codec() {
        return CODEC;
    }
}
