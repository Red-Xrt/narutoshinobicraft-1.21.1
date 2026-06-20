package narutoshinobicraft.client.particle.vfx.emitter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class BurstEmitter implements ParticleEmitter {
    public static final BurstEmitter DEFAULT = new BurstEmitter(24, 0.25d, 0.3d, 0.4d, 0.6d);

    public static final MapCodec<BurstEmitter> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.INT.optionalFieldOf("count", 24).forGetter(e -> e.count),
        Codec.DOUBLE.optionalFieldOf("spread", 0.25d).forGetter(e -> e.spread),
        Codec.DOUBLE.optionalFieldOf("velocity_scale", 0.3d).forGetter(e -> e.velocityScale),
        Codec.DOUBLE.optionalFieldOf("position_spread", 0.4d).forGetter(e -> e.positionSpread),
        Codec.DOUBLE.optionalFieldOf("origin_height_factor", 0.6d).forGetter(e -> e.originHeightFactor)
    ).apply(inst, BurstEmitter::new));

    private final int count;
    private final double spread;
    private final double velocityScale;
    private final double positionSpread;
    private final double originHeightFactor;

    public BurstEmitter(int count, double spread, double velocityScale, double positionSpread, double originHeightFactor) {
        this.count = count;
        this.spread = spread;
        this.velocityScale = velocityScale;
        this.positionSpread = positionSpread;
        this.originHeightFactor = originHeightFactor;
    }

    @Override
    public void emit(Level level, LivingEntity caster, JutsuParticleOptions particle) {
        RandomSource random = level.random;
        Vec3 look = caster.getLookAngle();
        double originX = caster.getX();
        double originY = caster.getY() + caster.getBbHeight() * this.originHeightFactor;
        double originZ = caster.getZ();

        for (int i = 0; i < this.count; i++) {
            double vx = look.x * this.velocityScale + (random.nextDouble() - 0.5d) * this.spread;
            double vy = look.y * this.velocityScale + (random.nextDouble() - 0.5d) * this.spread;
            double vz = look.z * this.velocityScale + (random.nextDouble() - 0.5d) * this.spread;
            level.addParticle(
                particle,
                originX + (random.nextDouble() - 0.5d) * this.positionSpread,
                originY + (random.nextDouble() - 0.5d) * this.positionSpread,
                originZ + (random.nextDouble() - 0.5d) * this.positionSpread,
                vx, vy, vz
            );
        }
    }

    @Override
    public MapCodec<? extends ParticleEmitter> codec() {
        return CODEC;
    }
}
