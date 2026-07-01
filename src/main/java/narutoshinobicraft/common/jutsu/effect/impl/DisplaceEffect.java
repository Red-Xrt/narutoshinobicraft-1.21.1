package narutoshinobicraft.common.jutsu.effect.impl;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.effect.api.EffectContext;
import narutoshinobicraft.common.jutsu.effect.api.JutsuEffect;
import narutoshinobicraft.common.spatial.impulse.apply.EntityImpulse;
import narutoshinobicraft.common.spatial.impulse.spec.ImpulseMode;
import narutoshinobicraft.common.spatial.impulse.spec.ImpulseSpec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class DisplaceEffect implements JutsuEffect {
    public static final MapCodec<DisplaceEffect> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        ImpulseMode.CODEC.fieldOf("mode").forGetter(DisplaceEffect::mode),
        ImpulseSpec.CODEC.forGetter(DisplaceEffect::spec)
    ).apply(inst, DisplaceEffect::new));

    private final ImpulseMode mode;
    private final ImpulseSpec spec;

    public DisplaceEffect(ImpulseMode mode, ImpulseSpec spec) {
        this.mode = mode;
        this.spec = spec;
    }

    public ImpulseMode mode() {
        return this.mode;
    }

    public ImpulseSpec spec() {
        return this.spec;
    }

    @Override
    public void apply(EffectContext context) {
        Entity target = context.target();
        if (target == null) {
            return;
        }
        Vec3 origin = resolveOrigin(context);
        switch (this.mode) {
            case push -> EntityImpulse.pushAway(origin, target, this.spec);
            case pull -> EntityImpulse.pullToward(origin, target, this.spec);
        }
    }

    private static Vec3 resolveOrigin(EffectContext context) {
        if (context.caster() != null) {
            return context.caster().position();
        }
        if (context.position() != null) {
            return context.position();
        }
        return context.source().position();
    }

    @Override
    public MapCodec<? extends JutsuEffect> codec() {
        return CODEC;
    }
}
