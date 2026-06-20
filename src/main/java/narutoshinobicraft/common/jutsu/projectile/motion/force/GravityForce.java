package narutoshinobicraft.common.jutsu.projectile.motion.force;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import net.minecraft.world.phys.Vec3;

/**
 * Pulls the projectile down by a constant amount each tick. Mirrors the original mod's ballistic
 * branch ({@code motionY = motionY * 0.98 - 0.04} in {@code EntityScalableProjectile}); here the
 * {@code 0.04} lives as {@code strength} and the {@code 0.98} is a separate {@link DragForce}.
 */
@SuppressWarnings("null")
public final class GravityForce implements Force {
    public static final MapCodec<GravityForce> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.DOUBLE.optionalFieldOf("strength", 0.04D).forGetter(f -> f.strength)
    ).apply(inst, GravityForce::new));

    private final double strength;

    public GravityForce(double strength) {
        this.strength = strength;
    }

    @Override
    public Vec3 apply(JutsuProjectile projectile, Vec3 velocity) {
        return velocity.add(0.0D, -this.strength, 0.0D);
    }

    @Override
    public MapCodec<? extends Force> codec() {
        return CODEC;
    }
}
