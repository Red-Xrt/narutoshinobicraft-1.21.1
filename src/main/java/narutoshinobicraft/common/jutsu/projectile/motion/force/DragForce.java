package narutoshinobicraft.common.jutsu.projectile.motion.force;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import net.minecraft.world.phys.Vec3;

/**
 * Air resistance: scales velocity by a factor &lt; 1 each tick. Mirrors the original mod's
 * {@code motion *= 0.98} slowdown. factor 1.0 = no drag, 0.9 = heavy drag.
 */
@SuppressWarnings("null")
public final class DragForce implements Force {
    public static final MapCodec<DragForce> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.DOUBLE.optionalFieldOf("factor", 0.98D).forGetter(f -> f.factor)
    ).apply(inst, DragForce::new));

    private final double factor;

    public DragForce(double factor) {
        this.factor = factor;
    }

    @Override
    public Vec3 apply(JutsuProjectile projectile, Vec3 velocity) {
        return velocity.scale(this.factor);
    }

    @Override
    public MapCodec<? extends Force> codec() {
        return CODEC;
    }
}
