package narutoshinobicraft.client.particle.vfx;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

@SuppressWarnings("null")
public final class JutsuVfxSpawner {
    private JutsuVfxSpawner() {}

    public static void spawnCastVfx(LivingEntity caster, ResourceLocation vfxId) {
        VfxDefinition vfx = VfxManager.get(vfxId);
        if (caster == null || vfx == null) {
            return;
        }
        vfx.castEmitter().emit(caster.level(), caster, vfx.particle());
    }

    public static void spawnChargeVfx(LivingEntity caster, ResourceLocation vfxId) {
        VfxDefinition vfx = VfxManager.get(vfxId);
        if (caster == null || vfx == null) {
            return;
        }
        vfx.chargeEmitter().emit(caster.level(), caster, vfx.particle());
    }
}
