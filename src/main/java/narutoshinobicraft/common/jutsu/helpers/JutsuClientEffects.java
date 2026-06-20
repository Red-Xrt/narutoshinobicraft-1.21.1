package narutoshinobicraft.common.jutsu.helpers;

import java.util.function.BiConsumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("null")
public final class JutsuClientEffects {
    private static BiConsumer<LivingEntity, ResourceLocation> chargeVfxSpawner = (entity, vfxId) -> {};

    private JutsuClientEffects() {}

    public static void registerChargeVfxSpawner(BiConsumer<LivingEntity, ResourceLocation> spawner) {
        chargeVfxSpawner = spawner == null ? (entity, vfxId) -> {} : spawner;
    }

    public static void spawnChargeVfx(LivingEntity entity, ResourceLocation vfxId) {
        if (entity != null && vfxId != null) {
            chargeVfxSpawner.accept(entity, vfxId);
        }
    }
}
