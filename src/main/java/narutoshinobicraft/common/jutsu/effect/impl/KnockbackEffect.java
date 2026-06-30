package narutoshinobicraft.common.jutsu.effect.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.effect.api.EffectContext;
import narutoshinobicraft.common.jutsu.effect.api.JutsuEffect;
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
        double dx;
        double dz;
        if (context.caster() != null) {
            dx = victim.getX() - context.caster().getX();
            dz = victim.getZ() - context.caster().getZ();
        } else if (context.position() != null) {
            dx = context.position().x - victim.getX();
            dz = context.position().z - victim.getZ();
        } else {
            dx = context.source().getX() - victim.getX();
            dz = context.source().getZ() - victim.getZ();
        }
        victim.knockback(this.strength, dx, dz);
    }

    @Override
    public MapCodec<? extends JutsuEffect> codec() {
        return CODEC;
    }
}
