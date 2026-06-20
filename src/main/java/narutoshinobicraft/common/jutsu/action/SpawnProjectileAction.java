package narutoshinobicraft.common.jutsu.action;

import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.data.component.JutsuContext;
import narutoshinobicraft.common.jutsu.api.JutsuAction;
import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import narutoshinobicraft.common.jutsu.projectile.ProjectileConfig;
import narutoshinobicraft.common.jutsu.projectile.ProjectileDefinitionManager;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public final class SpawnProjectileAction implements JutsuAction {
    public static final MapCodec<SpawnProjectileAction> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        ProjectileConfig.CODEC.optionalFieldOf("projectile").forGetter(a -> Optional.ofNullable(a.inline)),
        ResourceLocation.CODEC.optionalFieldOf("projectile_id").forGetter(a -> Optional.ofNullable(a.projectileId))
    ).apply(inst, (inline, id) -> new SpawnProjectileAction(inline.orElse(null), id.orElse(null))));

    private final ProjectileConfig inline;
    private final ResourceLocation projectileId;

    public SpawnProjectileAction(ProjectileConfig inline, ResourceLocation projectileId) {
        if (inline == null && projectileId == null) {
            throw new IllegalArgumentException("spawn_projectile requires 'projectile' or 'projectile_id'");
        }
        this.inline = inline;
        this.projectileId = projectileId;
    }

    @Override
    public boolean execute(JutsuContext context) {
        if (!JutsuAction.requireServerContext(context)) {
            return false;
        }
        ProjectileConfig config = resolveConfig();
        if (config == null) {
            return false;
        }
        JutsuProjectile.spawn(context.level(), context.player(), config, context.power());
        return true;
    }

    private ProjectileConfig resolveConfig() {
        if (this.inline != null) {
            return this.inline;
        }
        if (this.projectileId != null) {
            return ProjectileDefinitionManager.get(this.projectileId).orElse(null);
        }
        return null;
    }

    @Override
    public MapCodec<? extends JutsuAction> codec() {
        return CODEC;
    }
}
