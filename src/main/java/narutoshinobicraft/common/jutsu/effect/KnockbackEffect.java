package narutoshinobicraft.common.jutsu.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.entity.LivingEntity;

/**
 * Pushes the hit entity away from the impact point. {@code strength} follows vanilla knockback units.
 */
@SuppressWarnings("null")
public final class KnockbackEffect implements JutsuEffect {
    public static final MapCodec<KnockbackEffect> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.DOUBLE.optionalFieldOf("strength", 0.5D).forGetter(e -> e.strength)
    ).apply(inst, KnockbackEffect::new));

    private final double strength;

    public KnockbackEffect(double strength) {
        this.strength = strength;
    }

    @Override
    public void apply(EffectContext context) {
        if (!(context.target() instanceof LivingEntity victim)) {
            return;
        }
        victim.knockback(
            this.strength,
            context.position().x - victim.getX(),
            context.position().z - victim.getZ()
        );
    }

    @Override
    public MapCodec<? extends JutsuEffect> codec() {
        return CODEC;
    }
}
