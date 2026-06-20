package narutoshinobicraft.common.jutsu.action.registry;

import com.mojang.serialization.Codec;

import narutoshinobicraft.common.jutsu.action.api.JutsuAction;
import narutoshinobicraft.common.jutsu.action.impl.ApplyEffectsAction;
import narutoshinobicraft.common.jutsu.action.impl.JavaReferenceAction;
import narutoshinobicraft.common.jutsu.action.impl.NoopAction;
import narutoshinobicraft.common.jutsu.action.impl.SequenceAction;
import narutoshinobicraft.common.jutsu.action.impl.SpawnProjectileAction;
import narutoshinobicraft.common.registry.DispatchRegistry;

public final class ActionRegistry {
    private static final DispatchRegistry<JutsuAction> REGISTRY =
        new DispatchRegistry<>("jutsu_action", JutsuAction::codec);

    static {
        REGISTRY.register("narutoshinobicraft:noop", NoopAction.CODEC);
        REGISTRY.register("narutoshinobicraft:spawn_projectile", SpawnProjectileAction.CODEC);
        REGISTRY.register("narutoshinobicraft:apply_effects", ApplyEffectsAction.CODEC);
        REGISTRY.register("narutoshinobicraft:java", JavaReferenceAction.CODEC);
    }

    public static final Codec<JutsuAction> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    static {
        REGISTRY.register("narutoshinobicraft:sequence", SequenceAction.createCodec());
    }

    private ActionRegistry() {}
}
