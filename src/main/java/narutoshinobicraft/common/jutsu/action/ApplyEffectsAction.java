package narutoshinobicraft.common.jutsu.action;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.data.component.JutsuContext;
import narutoshinobicraft.common.jutsu.api.JutsuAction;
import narutoshinobicraft.common.jutsu.effect.EffectContext;
import narutoshinobicraft.common.jutsu.effect.EffectRegistry;
import narutoshinobicraft.common.jutsu.effect.JutsuEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class ApplyEffectsAction implements JutsuAction {
    public static final MapCodec<ApplyEffectsAction> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        EffectRegistry.DISPATCH_CODEC.listOf().fieldOf("effects").forGetter(ApplyEffectsAction::effects),
        com.mojang.serialization.Codec.DOUBLE.optionalFieldOf("range", 3.0d).forGetter(ApplyEffectsAction::range)
    ).apply(inst, ApplyEffectsAction::new));

    private final List<JutsuEffect> effects;
    private final double range;

    public ApplyEffectsAction(List<JutsuEffect> effects, double range) {
        this.effects = List.copyOf(effects);
        this.range = range;
    }

    public List<JutsuEffect> effects() {
        return this.effects;
    }

    public double range() {
        return this.range;
    }

    @Override
    public boolean execute(JutsuContext context) {
        if (!JutsuAction.requireServerContext(context)) {
            return false;
        }
        LivingEntity player = context.player();
        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().scale(this.range));
        var hit = context.level().clip(new net.minecraft.world.level.ClipContext(
            eye, end,
            net.minecraft.world.level.ClipContext.Block.OUTLINE,
            net.minecraft.world.level.ClipContext.Fluid.NONE,
            player
        ));
        Vec3 position = hit.getType() != net.minecraft.world.phys.HitResult.Type.MISS
            ? hit.getLocation()
            : end;
        EffectContext effectContext = new EffectContext(
            context.level(), player, player, null, position, context.power()
        );
        for (JutsuEffect effect : this.effects) {
            effect.apply(effectContext);
        }
        return true;
    }

    @Override
    public MapCodec<? extends JutsuAction> codec() {
        return CODEC;
    }
}
