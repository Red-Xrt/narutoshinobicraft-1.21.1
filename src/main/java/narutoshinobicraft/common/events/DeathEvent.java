package narutoshinobicraft.common.events;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.data.attachments.DeathTracker;
import narutoshinobicraft.common.events.customs.BattleExpChangedEvent;
import narutoshinobicraft.common.network.payloads.SyncBattleExpPayload;
import narutoshinobicraft.common.network.payloads.SyncChakraPayload;
import narutoshinobicraft.common.registry.AttachmentRegistry;
import narutoshinobicraft.common.registry.GameRuleRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID)
public class DeathEvent {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        var entity = event.getEntity();

        if (entity instanceof ServerPlayer player) {
            DeathTracker tracker = player.level().getData(AttachmentRegistry.PLAYER_DEATH);
            tracker.addDeath(player);
            
            if(!entity.level().getGameRules().getBoolean(GameRuleRegistry.KEEP_BATTLE_EXP_RULE)){
                var playerProcess = entity.getData(AttachmentRegistry.PLAYER_PROCESS);
                playerProcess.setBattleExp(40.0d);
                entity.setData(AttachmentRegistry.PLAYER_PROCESS, playerProcess);
                NeoForge.EVENT_BUS.post(new BattleExpChangedEvent(player, 40.0d));
            } //I setted data here because .CopyOnDeath() will bring data to new entity so we just need to sync back to client.
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var playerProcess = player.getData(AttachmentRegistry.PLAYER_PROCESS);
            NeoForge.EVENT_BUS.post(new BattleExpChangedEvent(player, playerProcess.getBattleExp()));
            
            var charkra = player.getData(AttachmentRegistry.PLAYER_CHAKRA);
            charkra.setCurrentChakra(charkra.getCharkaMax());
            player.setData(AttachmentRegistry.PLAYER_CHAKRA, charkra);
            PacketDistributor.sendToPlayer(player, new SyncBattleExpPayload(playerProcess));
            PacketDistributor.sendToPlayer(player, new SyncChakraPayload(charkra));
        }
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            DeathTracker tracker = serverLevel.getData(AttachmentRegistry.PLAYER_DEATH);
            tracker.tickCleanup(serverLevel);
        }
    }
}
