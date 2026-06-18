package narutoshinobicraft.common.network.handler.client;

import narutoshinobicraft.common.network.payloads.JutsuCastSuccessPayload;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class HandlerJutsuCastSuccess {
    public static void handler(JutsuCastSuccessPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player localPlayer = context.player();
            JutsuRegistry.JutsuEntry entry = JutsuRegistry.getJutsu(payload.jutsuId());
            
            if (entry != null) {
                Entity caster = localPlayer.level().getEntity(payload.casterId());                
                if (caster instanceof LivingEntity livingCaster) {
                    entry.render().onCast(livingCaster.level(), livingCaster, livingCaster.getMainHandItem());
                }
            }
        });
    }
}
