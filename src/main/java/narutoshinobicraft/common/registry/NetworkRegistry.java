package narutoshinobicraft.common.registry;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.network.payloads.SyncBattleExpPayload;
import narutoshinobicraft.common.network.payloads.SyncChakraPayload;
import narutoshinobicraft.common.network.payloads.SyncPlayerVariantPayload;
import narutoshinobicraft.common.network.handler.client.HandlerSyncBattleExp;
import narutoshinobicraft.common.network.handler.client.HandlerSyncChakra;
import narutoshinobicraft.common.network.handler.client.HandlerSyncNinjaVariant;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID)
public class NetworkRegistry {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                SyncChakraPayload.TYPE,
                SyncChakraPayload.STREAM_CODEC,
                HandlerSyncChakra::handler
        );

        registrar.playToClient(
            SyncBattleExpPayload.TYPE, 
            SyncBattleExpPayload.STREAM_CODEC, 
            HandlerSyncBattleExp::handler
        );

        registrar.playToClient(
            SyncPlayerVariantPayload.TYPE, 
            SyncPlayerVariantPayload.STREAM_CODEC, 
            HandlerSyncNinjaVariant::handler);
    }
}