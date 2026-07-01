package narutoshinobicraft.common.jutsu.helpers.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;

@SuppressWarnings("null")
public final class JutsuClientEffects {

    @FunctionalInterface

    public interface ProjectileTrailSpawner {
        void spawn(Entity entity, Vec3 motion, ResourceLocation presetId);
    }

    private static BiConsumer<LivingEntity, ResourceLocation> scrollAura = (entity, presetId) -> {};

    private static ProjectileTrailSpawner projectileTrail = (entity, motion, presetId) -> {};

    private JutsuClientEffects() {}

    public static void registerScrollAura(BiConsumer<LivingEntity, ResourceLocation> handler) {
        scrollAura = handler == null ? (entity, presetId) -> {} : handler;
    }

    public static void registerProjectileTrail(ProjectileTrailSpawner handler) {
        projectileTrail = handler == null ? (entity, motion, presetId) -> {} : handler;
    }

    public static void playScrollAura(LivingEntity entity, ResourceLocation presetId) {
        if (entity != null && presetId != null) {
            scrollAura.accept(entity, presetId);
        }
    }

    public static void playProjectileTrail(Entity entity, Vec3 motion, ResourceLocation presetId) {
        if (entity != null && presetId != null) {
            projectileTrail.spawn(entity, motion == null ? Vec3.ZERO : motion, presetId);
        }
    }
}
