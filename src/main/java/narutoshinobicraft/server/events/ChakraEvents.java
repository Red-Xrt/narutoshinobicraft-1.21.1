package narutoshinobicraft.server.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.data.attachments.PlayersChakra;
import narutoshinobicraft.common.events.BattleExpChangedEvent;
import narutoshinobicraft.common.network.payloads.SyncChakraPayload;
import narutoshinobicraft.common.registry.AttachmentRegistry;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID)
public class ChakraEvents {
    @SubscribeEvent
    //TODO: I think we can do to trigger a keybind instead of waiting
    //TODO: Need to have achivement check and i need to make it work correctly i need to have achivement ninja first to have a charka
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        if (!player.getData(AttachmentRegistry.PLAYER_PROCESS).isNinja()) return;

        PlayersChakra chakraAttachment = player.getData(AttachmentRegistry.PLAYER_CHAKRA);
        double oldCurrentChakra = chakraAttachment.getCurrentChakra();
        double oldChakraMax = chakraAttachment.getCharkaMax();

        if (oldCurrentChakra > oldChakraMax && player.tickCount % 20 == 0) {
            chakraAttachment.consumeChakra(10.0d);
        }

        if (oldCurrentChakra < 10.0d && oldChakraMax > 150.0d && !player.isCreative()) {
            if (!player.hasEffect(MobEffects.WEAKNESS) && 
                !player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) && 
                !player.hasEffect(MobEffects.CONFUSION)) {
                    
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 3));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 3));
            }
        }

        chakraAttachment.addMotionLessTime(1);

        if (player.getDeltaMovement().horizontalDistanceSqr() > 0.01d || !player.onGround() || player.swinging) { //TODO: i scare this will affect when other player push current player
            chakraAttachment.setMotionLessTime(0);
        }

        if (chakraAttachment.getMotionLessTime() > 100 && player.tickCount % 80 == 0) {
            chakraAttachment.consumeChakra(-2.5d);
        }

        if (oldCurrentChakra != chakraAttachment.getCurrentChakra() || oldChakraMax != chakraAttachment.getCharkaMax()) {
            player.setData(AttachmentRegistry.PLAYER_CHAKRA, chakraAttachment);
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncChakraPayload(chakraAttachment));
        }
    }
    
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event){
        var enity = event.getEntity();
        
        //TODO: CONFIG TOOO BLA BLA
        if(enity instanceof ServerPlayer serverPlayer && !enity.level().isClientSide /*&& enity.getData(AttachmentRegistry.PLAYER_PROCESS).isNinja() && enity.experienceLevel >= 0*/){
            var chakra = enity.getData(AttachmentRegistry.PLAYER_CHAKRA);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncChakraPayload(chakra));
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event){
        var enity = event.getEntity();
        if(enity instanceof ServerPlayer serverPlayer && !enity.level().isClientSide){
            PacketDistributor.sendToPlayer(serverPlayer, new SyncChakraPayload(enity.getData(AttachmentRegistry.PLAYER_CHAKRA)));
        }
    }

    @SubscribeEvent
    public static void onPlayerSleep(CanPlayerSleepEvent event){
        var entity = event.getEntity();
        var chakraAttachment = entity.getData(AttachmentRegistry.PLAYER_CHAKRA);
        if (!(entity instanceof ServerPlayer player)) return;
        
        ServerLevel serverLevel = player.serverLevel();
        serverLevel.getServer().tell(new TickTask(serverLevel.getServer().getTickCount(), () -> {
            if (player.isSleeping()) {
                boolean areAllPlayersAsleep = serverLevel.getPlayers(Player::isSleeping).size() == serverLevel.players().size(); //TODO: make it comptiable with new modern game rule playersSleepingPercentage
                if (areAllPlayersAsleep) {
                    while(chakraAttachment.getCurrentChakra() < chakraAttachment.getCharkaMax()){
                        chakraAttachment.consumeChakra(-0.6f);
                    }
                }
            }
        }));
    }

    @SubscribeEvent
    public static void onBattleExpChanged(BattleExpChangedEvent event){
        Player player = event.getPlayer();
        if (player.level().isClientSide()) return;

        double maxChakra = event.getNewExp() * 0.5d; //TODO: This thing was so OP. 2 Battle XP = 1 Chakra => 100k Battle Exp = 50k Chakra? I need to make it hard and player need to get tail beast if they want more chakra
        var chakraAttachment = player.getData(AttachmentRegistry.PLAYER_CHAKRA);

        if (maxChakra != chakraAttachment.getCharkaMax()) {
            chakraAttachment.setCharkaMax(maxChakra);
            player.setData(AttachmentRegistry.PLAYER_CHAKRA, chakraAttachment);
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncChakraPayload(chakraAttachment));
        }
    }
}
