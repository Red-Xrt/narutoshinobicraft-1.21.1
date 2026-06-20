package narutoshinobicraft.common.jutsu.projectile.behavior.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import narutoshinobicraft.common.jutsu.projectile.behavior.api.ProjectileBehavior;

/**
 * Lerps the projectile's scale from {@code from} to {@code to} over {@code ticks}, then holds. Stateless:
 * the progress is derived purely from the projectile's age. Drives the hitbox (and, later, render size).
 */
@SuppressWarnings("null")
public final class ScaleOverTimeBehavior implements ProjectileBehavior {
    public static final MapCodec<ScaleOverTimeBehavior> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.FLOAT.optionalFieldOf("from", 0.2f).forGetter(b -> b.from),
        Codec.FLOAT.optionalFieldOf("to", 1.0f).forGetter(b -> b.to),
        Codec.INT.optionalFieldOf("ticks", 20).forGetter(b -> b.ticks)
    ).apply(inst, ScaleOverTimeBehavior::new));

    private final float from;
    private final float to;
    private final int ticks;

    public ScaleOverTimeBehavior(float from, float to, int ticks) {
        this.from = from;
        this.to = to;
        this.ticks = Math.max(1, ticks);
    }

    @Override
    public void start(JutsuProjectile projectile) {
        projectile.setProjectileScale(this.from);
    }

    @Override
    public void tick(JutsuProjectile projectile) {
        float t = Math.min(1.0f, (float) projectile.getProjectileAge() / this.ticks);
        projectile.setProjectileScale(this.from + (this.to - this.from) * t);
    }

    @Override
    public MapCodec<? extends ProjectileBehavior> codec() {
        return CODEC;
    }
}
