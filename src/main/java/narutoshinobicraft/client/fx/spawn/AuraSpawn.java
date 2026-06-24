package narutoshinobicraft.client.fx.spawn;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import narutoshinobicraft.client.fx.anchor.FxAnchor;
import narutoshinobicraft.client.fx.speck.SpeckOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@SuppressWarnings("null")
public final class AuraSpawn implements SpawnPattern {
    public static final AuraSpawn DEFAULT = new AuraSpawn(30, 0.2d, 0.5d);

    public static final MapCodec<AuraSpawn> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.INT.optionalFieldOf("count", 30).forGetter(e -> e.count),
        Codec.DOUBLE.optionalFieldOf("radius", 0.2d).forGetter(e -> e.radius),
        Codec.DOUBLE.optionalFieldOf("rise", 0.5d).forGetter(e -> e.rise)
    ).apply(inst, AuraSpawn::new));

    private final int count;
    private final double radius;
    private final double rise;

    public AuraSpawn(int count, double radius, double rise) {
        this.count = count;
        this.radius = radius;
        this.rise = rise;
    }

    @Override
    public void play(Level level, FxAnchor anchor, SpeckOptions speck) {
        if (!(anchor instanceof FxAnchor.OnBody onBody)) {
            return;
        }
        LivingEntity body = onBody.entity();
        RandomSource random = level.random;
        double baseX = body.getX();
        double baseY = body.getY();
        double baseZ = body.getZ();

        for (int i = 0; i < this.count; i++) {
            double offsetX = random.nextGaussian() * this.radius;
            double offsetZ = random.nextGaussian() * this.radius;
            double upward = this.rise * (0.85d + random.nextDouble() * 0.3d);
            level.addParticle(speck, baseX + offsetX, baseY, baseZ + offsetZ, 0.0d, upward, 0.0d);
        }
    }

    @Override
    public MapCodec<? extends SpawnPattern> codec() {
        return CODEC;
    }
}
