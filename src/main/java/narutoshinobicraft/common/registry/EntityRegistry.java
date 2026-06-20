package narutoshinobicraft.common.registry;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("null")
public final class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(Registries.ENTITY_TYPE, NarutoShinobiCraft.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<JutsuProjectile>> JUTSU_PROJECTILE =
        ENTITY_TYPES.register("jutsu_projectile", () ->
            EntityType.Builder.<JutsuProjectile>of(JutsuProjectile::new, MobCategory.MISC)
                .sized(0.5f, 0.5f)
                .clientTrackingRange(8)
                .updateInterval(1)
                .build("jutsu_projectile"));

    private EntityRegistry() {}
}
