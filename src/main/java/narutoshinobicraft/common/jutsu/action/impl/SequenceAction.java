package narutoshinobicraft.common.jutsu.action.impl;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.data.component.JutsuContext;
import narutoshinobicraft.common.jutsu.action.api.JutsuAction;
import narutoshinobicraft.common.jutsu.action.registry.ActionRegistry;

@SuppressWarnings("null")
public final class SequenceAction implements JutsuAction {
    private static MapCodec<SequenceAction> codec;

    private final List<JutsuAction> steps;

    public SequenceAction(List<JutsuAction> steps) {
        this.steps = List.copyOf(steps);
    }

    public static MapCodec<SequenceAction> createCodec() {
        if (codec == null) {
            codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
                ActionRegistry.DISPATCH_CODEC.listOf().fieldOf("steps").forGetter(SequenceAction::steps)
            ).apply(inst, SequenceAction::new));
        }
        return codec;
    }

    public List<JutsuAction> steps() {
        return this.steps;
    }

    @Override
    public boolean execute(JutsuContext context) {
        for (JutsuAction step : this.steps) {
            if (!step.execute(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public MapCodec<? extends JutsuAction> codec() {
        return createCodec();
    }
}
