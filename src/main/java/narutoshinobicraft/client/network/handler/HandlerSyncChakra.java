package narutoshinobicraft.client.network.handler;

import narutoshinobicraft.common.registry.AttachmentRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import narutoshinobicraft.common.data.attachments.PlayersChakra;
import narutoshinobicraft.common.network.payloads.SyncChakraPayload;

@SuppressWarnings("null")
public class HandlerSyncChakra {
    public static void handler(SyncChakraPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player localPlayer = context.player();
            PlayersChakra newChakra = payload.chakra();
            localPlayer.setData(AttachmentRegistry.PLAYER_CHAKRA, newChakra);
        });
    }
}
