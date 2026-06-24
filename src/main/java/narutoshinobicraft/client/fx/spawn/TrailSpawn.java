package narutoshinobicraft.client.fx.spawn;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import narutoshinobicraft.client.fx.anchor.FxAnchor;
import narutoshinobicraft.client.fx.speck.SpeckOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class TrailSpawn implements SpawnPattern {
    public static final TrailSpawn DEFAULT = new TrailSpawn(4, 0.15d, 0.35d);

    public static final MapCodec<TrailSpawn> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.INT.optionalFieldOf("count", 4).forGetter(e -> e.count),
        Codec.DOUBLE.optionalFieldOf("spread", 0.15d).forGetter(e -> e.spread),
        Codec.DOUBLE.optionalFieldOf("motion_scale", 0.35d).forGetter(e -> e.motionScale)
    ).apply(inst, TrailSpawn::new));

    private final int count;
    private final double spread;
    private final double motionScale;

    public TrailSpawn(int count, double spread, double motionScale) {
        this.count = count;
        this.spread = spread;
        this.motionScale = motionScale;
    }

    @Override
    public void play(Level level, FxAnchor anchor, SpeckOptions speck) {
        if (!(anchor instanceof FxAnchor.InFlight flight)) {
            return;
        }
        Entity entity = flight.entity();
        Vec3 motion = flight.motion();
        RandomSource random = level.random;
        double x = entity.getX();
        double y = entity.getY() + entity.getBbHeight() * 0.5d;
        double z = entity.getZ();

        for (int i = 0; i < this.count; i++) {
            double vx = motion.x * this.motionScale + (random.nextDouble() - 0.5d) * this.spread;
            double vy = motion.y * this.motionScale + (random.nextDouble() - 0.5d) * this.spread;
            double vz = motion.z * this.motionScale + (random.nextDouble() - 0.5d) * this.spread;
            level.addParticle(
                speck,
                x + (random.nextDouble() - 0.5d) * this.spread,
                y + (random.nextDouble() - 0.5d) * this.spread,
                z + (random.nextDouble() - 0.5d) * this.spread,
                vx, vy, vz
            );
        }
    }

    @Override
    public MapCodec<? extends SpawnPattern> codec() {
        return CODEC;
    }
}
