package narutoshinobicraft.client.network.handler;

import narutoshinobicraft.common.registry.AttachmentRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import narutoshinobicraft.common.data.attachments.PlayerVariants;
import narutoshinobicraft.common.network.payloads.SyncPlayerVariantPayload;

@SuppressWarnings("null")
public class HandlerSyncNinjaVariant {
    public static void handler(SyncPlayerVariantPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player localPlayer = context.player();
            PlayerVariants playerVariants = payload.playerVariants();
            localPlayer.setData(AttachmentRegistry.PLAYER_VARIANTS, playerVariants);
        });
    }
}
