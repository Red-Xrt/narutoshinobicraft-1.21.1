package narutoshinobicraft.common.network.handler.client;

import narutoshinobicraft.common.network.payloads.JutsuCastSuccessPayload;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class HandlerJutsuCastSuccess {
    public static void handler(JutsuCastSuccessPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player localPlayer = context.player();
            JutsuRegistry.JutsuEntry entry = JutsuRegistry.getJutsu(payload.jutsuId());
            if (entry != null) {
                entry.render().onCast(localPlayer.level(), localPlayer, localPlayer.getMainHandItem());
            }
        });
    }
}
