package narutoshinobicraft.client.fx.spawn;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import narutoshinobicraft.client.fx.anchor.FxAnchor;
import narutoshinobicraft.client.fx.speck.SpeckOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class BurstSpawn implements SpawnPattern {
    public static final BurstSpawn DEFAULT = new BurstSpawn(24, 0.25d, 0.3d, 0.4d, 0.6d);

    public static final MapCodec<BurstSpawn> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.INT.optionalFieldOf("count", 24).forGetter(e -> e.count),
        Codec.DOUBLE.optionalFieldOf("spread", 0.25d).forGetter(e -> e.spread),
        Codec.DOUBLE.optionalFieldOf("velocity_scale", 0.3d).forGetter(e -> e.velocityScale),
        Codec.DOUBLE.optionalFieldOf("position_spread", 0.4d).forGetter(e -> e.positionSpread),
        Codec.DOUBLE.optionalFieldOf("origin_height_factor", 0.6d).forGetter(e -> e.originHeightFactor)
    ).apply(inst, BurstSpawn::new));

    private final int count;
    private final double spread;
    private final double velocityScale;
    private final double positionSpread;
    private final double originHeightFactor;

    public BurstSpawn(int count, double spread, double velocityScale, double positionSpread, double originHeightFactor) {
        this.count = count;
        this.spread = spread;
        this.velocityScale = velocityScale;
        this.positionSpread = positionSpread;
        this.originHeightFactor = originHeightFactor;
    }

    @Override
    public void play(Level level, FxAnchor anchor, SpeckOptions speck) {
        if (!(anchor instanceof FxAnchor.OnBody onBody)) {
            return;
        }
        LivingEntity body = onBody.entity();
        RandomSource random = level.random;
        Vec3 look = body.getLookAngle();
        double originX = body.getX();
        double originY = body.getY() + body.getBbHeight() * this.originHeightFactor;
        double originZ = body.getZ();

        for (int i = 0; i < this.count; i++) {
            double vx = look.x * this.velocityScale + (random.nextDouble() - 0.5d) * this.spread;
            double vy = look.y * this.velocityScale + (random.nextDouble() - 0.5d) * this.spread;
            double vz = look.z * this.velocityScale + (random.nextDouble() - 0.5d) * this.spread;
            level.addParticle(
                speck,
                originX + (random.nextDouble() - 0.5d) * this.positionSpread,
                originY + (random.nextDouble() - 0.5d) * this.positionSpread,
                originZ + (random.nextDouble() - 0.5d) * this.positionSpread,
                vx, vy, vz
            );
        }
    }

    @Override
    public MapCodec<? extends SpawnPattern> codec() {
        return CODEC;
    }
}
