package narutoshinobicraft.common.events;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.network.payloads.SyncPlayerVariantPayload;
import narutoshinobicraft.common.registry.AttachmentRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID)
public class PlayerVariantEvent {
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event){
        var enity = event.getEntity();
    
        if(enity instanceof ServerPlayer serverPlayer && !enity.level().isClientSide){
            var playerVariants = enity.getData(AttachmentRegistry.PLAYER_VARIANTS);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncPlayerVariantPayload(playerVariants));
        }
    }
}
