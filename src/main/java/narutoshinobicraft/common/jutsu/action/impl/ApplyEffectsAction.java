package narutoshinobicraft.common.jutsu.action.impl;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import narutoshinobicraft.common.data.component.JutsuContext;
import narutoshinobicraft.common.jutsu.action.api.JutsuAction;
import narutoshinobicraft.common.jutsu.effect.api.EffectContext;
import narutoshinobicraft.common.jutsu.effect.api.JutsuEffect;
import narutoshinobicraft.common.jutsu.effect.registry.EffectRegistry;
import narutoshinobicraft.common.spatial.aim.AimResolver;
import narutoshinobicraft.common.spatial.aim.AimSpec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class ApplyEffectsAction implements JutsuAction {
    public static final MapCodec<ApplyEffectsAction> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        EffectRegistry.DISPATCH_CODEC.listOf().fieldOf("effects").forGetter(ApplyEffectsAction::effects),
        AimSpec.CODEC.fieldOf("aim").forGetter(ApplyEffectsAction::aimSpec)
    ).apply(inst, ApplyEffectsAction::new));

    private final List<JutsuEffect> effects;
    private final AimSpec aimSpec;

    public ApplyEffectsAction(List<JutsuEffect> effects, AimSpec aimSpec) {
        this.effects = List.copyOf(effects);
        this.aimSpec = aimSpec;
    }

    public List<JutsuEffect> effects() {
        return this.effects;
    }

    public AimSpec aimSpec() {
        return this.aimSpec;
    }

    @Override
    public boolean execute(JutsuContext context) {
        if (!JutsuAction.requireServerContext(context)) {
            return false;
        }

        HitResult hitResult = AimResolver.resolve(context, this.aimSpec);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return false;
        }

        LivingEntity targetEntity = null;
        Vec3 position = hitResult.getLocation();

        if (hitResult instanceof EntityHitResult entityHit
            && entityHit.getEntity() instanceof LivingEntity living) {
            targetEntity = living;
        }

        EffectContext effectContext = new EffectContext(
            context.level(),
            context.caster(),
            context.caster(),
            targetEntity,
            position,
            context.power()
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
