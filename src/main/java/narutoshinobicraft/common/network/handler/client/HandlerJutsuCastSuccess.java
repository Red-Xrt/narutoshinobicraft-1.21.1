package narutoshinobicraft.common.network.handler.client;

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
            JutsuRegistry.JutsuEntry entry = JutsuRegistry.getJutsu(payload.jutsuId());
            
            if (entry != null) {
                Entity caster = localPlayer.level().getEntity(payload.casterId());                
                if (caster instanceof LivingEntity livingCaster) {
                    ItemStack scrollItem = JutsuScrollSupport.findHeldScroll((Player) livingCaster);
                    if (!scrollItem.isEmpty()) {
                        entry.render().onCast(livingCaster.level(), livingCaster, scrollItem);
                    }
                }
            }
        });
    }
}
