package narutoshinobicraft.common.spatial.aim;

import java.util.function.Predicate;

import narutoshinobicraft.common.data.component.JutsuContext;
import narutoshinobicraft.common.spatial.aim.utils.EntityTargetFilter;
import narutoshinobicraft.common.spatial.query.RayQueries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;

@SuppressWarnings("null")
public final class AimResolver {
    private AimResolver() {}

    public static HitResult resolve(JutsuContext context, AimSpec aimSpec) {
        Predicate<LivingEntity> entityFilter = buildEntityFilter(context.caster(), aimSpec.targetFilter());
        return RayQueries.objectEntityLookingAt(
            context.caster(),
            aimSpec.range(),
            aimSpec.hitboxGrow(),
            aimSpec.stopOnLiquid(),
            entityFilter
        );
    }

    private static Predicate<LivingEntity> buildEntityFilter(Player caster, EntityTargetFilter mode) {
        Predicate<LivingEntity> base = target ->
            target != null && !target.isSpectator() && !target.equals(caster) && target.isPickable();

        return switch (mode) {
            case all -> base;
            case only_enemy -> base.and(target -> {
                if (target instanceof Enemy) {
                    return true;
                }
                if (caster.getTeam() != null && target.getTeam() != null) {
                    return !caster.isAlliedTo(target);
                }
                return false;
            });
            case allies_only -> base.and(caster::isAlliedTo);
        };
    }
}
