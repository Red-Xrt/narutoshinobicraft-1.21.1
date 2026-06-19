package narutoshinobicraft.client.network.handler;

import narutoshinobicraft.common.data.attachments.PlayerProcess;
import narutoshinobicraft.common.network.payloads.SyncBattleExpPayload;
import narutoshinobicraft.common.registry.AttachmentRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@SuppressWarnings("null")
public class HandlerSyncBattleExp {
    public static void handler(SyncBattleExpPayload payload, IPayloadContext context){
        context.enqueueWork(() -> {
            Player localPlayer = context.player();
            PlayerProcess newPlayerProcess = payload.playerProcess();
            localPlayer.setData(AttachmentRegistry.PLAYER_PROCESS, newPlayerProcess);
        });
    }
}
