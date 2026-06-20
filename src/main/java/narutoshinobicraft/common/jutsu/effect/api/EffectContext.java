package narutoshinobicraft.common.jutsu.effect.api;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Everything a {@link JutsuEffect} needs to do its job, decoupled from how it was triggered (on-hit,
 * on-tick, on-cast...). Keeps effects reusable across contexts.
 *
 * @param level    the world the effect happens in (server side)
 * @param caster   who cast the jutsu (may be null if the source has no owner)
 * @param source   the immediate source entity (e.g. the projectile), may equal caster
 * @param target   the entity hit, if any (null for block/area impacts)
 * @param position where the effect is centered
 * @param power    the charged power of the cast, for scaling
 */
public record EffectContext(
    Level level,
    @Nullable LivingEntity caster,
    Entity source,
    @Nullable Entity target,
    Vec3 position,
    float power
) {}
