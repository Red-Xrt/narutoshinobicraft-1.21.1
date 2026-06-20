package narutoshinobicraft.common.jutsu.effect.registry;

import com.mojang.serialization.Codec;

import narutoshinobicraft.common.jutsu.effect.api.JutsuEffect;
import narutoshinobicraft.common.jutsu.effect.impl.DamageEffect;
import narutoshinobicraft.common.jutsu.effect.impl.KnockbackEffect;
import narutoshinobicraft.common.registry.DispatchRegistry;

/**
 * Registry of composable {@link JutsuEffect}s. Add a gameplay primitive here (Core layer) and it can be
 * dropped into any effect list (e.g. a projectile's {@code on_hit}) from data.
 */
@SuppressWarnings("null")
public final class EffectRegistry {
    private static final DispatchRegistry<JutsuEffect> REGISTRY =
        new DispatchRegistry<>("jutsu_effect", JutsuEffect::codec);

    static {
        REGISTRY.register("narutoshinobicraft:damage", DamageEffect.CODEC);
        REGISTRY.register("narutoshinobicraft:knockback", KnockbackEffect.CODEC);
    }

    /** JSON dispatch codec for any effect, keyed on the "type" field. */
    public static final Codec<JutsuEffect> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private EffectRegistry() {}
}
