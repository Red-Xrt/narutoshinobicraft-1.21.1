package narutoshinobicraft.common.jutsu.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import narutoshinobicraft.common.jutsu.effect.registry.EffectRegistry;
import narutoshinobicraft.common.jutsu.effect.api.JutsuEffect;
import narutoshinobicraft.common.jutsu.projectile.behavior.api.ProjectileBehavior;
import narutoshinobicraft.common.jutsu.projectile.motion.api.EntityMotion;
import narutoshinobicraft.common.jutsu.projectile.motion.registry.EntityMotionRegistry;
import narutoshinobicraft.common.jutsu.projectile.behavior.registry.ProjectileBehaviorRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@SuppressWarnings("null")
public record ProjectileConfig(
    EntityMotion motion,
    List<ProjectileBehavior> behaviors,
    List<JutsuEffect> onHit,
    List<ResourceLocation> onFly,
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
        ResourceLocation.CODEC.listOf().optionalFieldOf("on_fly", List.of())
            .forGetter(ProjectileConfig::onFly),
        Codec.FLOAT.optionalFieldOf("width", 0.5f).forGetter(ProjectileConfig::width),
        Codec.FLOAT.optionalFieldOf("height", 0.5f).forGetter(ProjectileConfig::height),
        Codec.INT.optionalFieldOf("lifetime", 100).forGetter(ProjectileConfig::lifetime)
    ).apply(inst, ProjectileConfig::new));

    public ProjectileConfig {
        behaviors = List.copyOf(behaviors);
        onHit = List.copyOf(onHit);
        onFly = List.copyOf(onFly);
    }
}
