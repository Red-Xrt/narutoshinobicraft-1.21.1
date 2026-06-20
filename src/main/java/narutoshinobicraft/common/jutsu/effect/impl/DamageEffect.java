package narutoshinobicraft.common.jutsu.effect.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.effect.api.EffectContext;
import narutoshinobicraft.common.jutsu.effect.api.JutsuEffect;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("null")
public final class DamageEffect implements JutsuEffect {
    public static final MapCodec<DamageEffect> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.FLOAT.optionalFieldOf("amount", 4.0f).forGetter(e -> e.amount),
        Codec.BOOL.optionalFieldOf("scale_with_power", false).forGetter(e -> e.scaleWithPower)
    ).apply(inst, DamageEffect::new));

    private final float amount;
    private final boolean scaleWithPower;

    public DamageEffect(float amount, boolean scaleWithPower) {
        this.amount = amount;
        this.scaleWithPower = scaleWithPower;
    }

    @Override
    public void apply(EffectContext context) {
        if (!(context.target() instanceof LivingEntity victim)) {
            return;
        }
        float dmg = this.scaleWithPower ? this.amount * Math.max(1.0f, context.power()) : this.amount;
        victim.hurt(context.level().damageSources().indirectMagic(context.source(), context.caster()), dmg);
    }

    @Override
    public MapCodec<? extends JutsuEffect> codec() {
        return CODEC;
    }
}

