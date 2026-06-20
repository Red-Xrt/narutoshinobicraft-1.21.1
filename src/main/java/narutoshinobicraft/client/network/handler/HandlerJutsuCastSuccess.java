package narutoshinobicraft.client.network.handler;

import narutoshinobicraft.client.particle.vfx.JutsuVfxSpawner;
import narutoshinobicraft.common.jutsu.support.JutsuScrollSupport;
import narutoshinobicraft.common.network.payloads.JutsuCastSuccessPayload;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class HandlerJutsuCastSuccess {
    public static void handler(JutsuCastSuccessPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player localPlayer = context.player();
            var entry = JutsuRegistry.findEntry(payload.jutsuId()).orElse(null);
            if (entry == null) {
                return;
            }

            Entity caster = localPlayer.level().getEntity(payload.casterId());
            if (!(caster instanceof LivingEntity livingCaster)) {
                return;
            }

            entry.definition().vfxId()
                .ifPresent(vfxId -> JutsuVfxSpawner.spawnCastVfx(livingCaster, vfxId));

            if (livingCaster instanceof Player playerCaster) {
                ItemStack scrollItem = JutsuScrollSupport.findHeldScroll(playerCaster);
                if (!scrollItem.isEmpty()) {
                    entry.render().onCast(livingCaster.level(), livingCaster, scrollItem);
                }
            }
        });
    }
}
