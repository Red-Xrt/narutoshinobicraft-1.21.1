package narutoshinobicraft.common.jutsu.projectile;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.effect.EffectRegistry;
import narutoshinobicraft.common.jutsu.effect.JutsuEffect;
import narutoshinobicraft.common.jutsu.projectile.behavior.ProjectileBehavior;
import narutoshinobicraft.common.jutsu.projectile.motion.EntityMotion;
import narutoshinobicraft.common.jutsu.projectile.registry.EntityMotionRegistry;
import narutoshinobicraft.common.jutsu.projectile.registry.ProjectileBehaviorRegistry;

/**
 * The Lego blueprint of a projectile: ONE motion + a list of behaviors + a list of on-hit effects +
 * basic hitbox/lifetime config. This is what JSON assembles (Phase 3) and what gets serialized onto a
 * live {@link JutsuProjectile} for persistence. For now it's also built directly in Java for testing.
 */
@SuppressWarnings("null")
public record ProjectileConfig(
    EntityMotion motion,
    List<ProjectileBehavior> behaviors,
    List<JutsuEffect> onHit,
    float width,
    float height,
    int lifetime
) {
    public static final Codec<ProjectileConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        EntityMotionRegistry.DISPATCH_CODEC.fieldOf("motion").forGetter(ProjectileConfig::motion),
        ProjectileBehaviorRegistry.DISPATCH_CODEC.listOf().optionalFieldOf("behaviors", List.of())
            .forGetter(ProjectileConfig::behaviors),
        EffectRegistry.DISPATCH_CODEC.listOf().optionalFieldOf("on_hit", List.of())
            .forGetter(ProjectileConfig::onHit),
        Codec.FLOAT.optionalFieldOf("width", 0.5f).forGetter(ProjectileConfig::width),
        Codec.FLOAT.optionalFieldOf("height", 0.5f).forGetter(ProjectileConfig::height),
        Codec.INT.optionalFieldOf("lifetime", 100).forGetter(ProjectileConfig::lifetime)
    ).apply(inst, ProjectileConfig::new));

    public ProjectileConfig {
        behaviors = List.copyOf(behaviors);
        onHit = List.copyOf(onHit);
    }
}
