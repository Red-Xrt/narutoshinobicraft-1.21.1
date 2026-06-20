package narutoshinobicraft.common.jutsu.effect;

import com.mojang.serialization.MapCodec;

/**
 * Trục 2 — HỆ QUẢ GAMEPLAY. A composable block of "what happens": damage, knockback, ignite, apply
 * status, explode, spawn... Effects are assembled into lists (e.g. a projectile's {@code on_hit}) and
 * resolved in order. This is the seed of the D3 composable-effect system; new effects register in
 * {@link narutoshinobicraft.common.jutsu.effect.EffectRegistry} and become referenceable from data.
 */
public interface JutsuEffect {
    void apply(EffectContext context);

    MapCodec<? extends JutsuEffect> codec();
}
