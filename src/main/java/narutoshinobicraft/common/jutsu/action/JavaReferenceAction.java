package narutoshinobicraft.common.jutsu.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.data.component.JutsuContext;
import narutoshinobicraft.common.jutsu.api.JutsuAction;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public final class JavaReferenceAction implements JutsuAction {
    public static final MapCodec<JavaReferenceAction> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(JavaReferenceAction::actionId)
    ).apply(inst, JavaReferenceAction::new));

    private final ResourceLocation actionId;

    public JavaReferenceAction(ResourceLocation actionId) {
        this.actionId = actionId;
    }

    public ResourceLocation actionId() {
        return this.actionId;
    }

    @Override
    public boolean execute(JutsuContext context) {
        return JutsuRegistry.getJavaAction(this.actionId)
            .map(action -> action.execute(context))
            .orElse(false);
    }

    @Override
    public MapCodec<? extends JutsuAction> codec() {
        return CODEC;
    }
}
