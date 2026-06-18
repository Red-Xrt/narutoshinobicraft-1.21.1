package narutoshinobicraft.common.events;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.data.attachments.PlayerProcess;
import narutoshinobicraft.common.events.customs.BattleExpChangedEvent;
import narutoshinobicraft.common.network.payloads.SyncBattleExpPayload;
import narutoshinobicraft.common.registry.AttachmentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID)
public class BattleExpEvent {
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event){
        var entity = event.getEntity();
        if(entity instanceof ServerPlayer serverPlayer && !entity.level().isClientSide){
            PlayerProcess playerProcess = entity.getData(AttachmentRegistry.PLAYER_PROCESS);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncBattleExpPayload(playerProcess));
            NeoForge.EVENT_BUS.post(new BattleExpChangedEvent(serverPlayer, playerProcess.getBattleExp()));
        }
    }

    @SubscribeEvent
    public static void onDamaged(LivingDamageEvent.Post event) {
        var targetEntity = event.getEntity();
        var sourceEntity = event.getSource().getEntity();
        float damageSource = event.getOriginalDamage();

        if (targetEntity.equals(sourceEntity) || damageSource <= 0f) return;

        if (targetEntity instanceof ServerPlayer targetPlayer && damageSource < targetPlayer.getHealth()) {
            PlayerProcess targetProcess = targetPlayer.getData(AttachmentRegistry.PLAYER_PROCESS);
            double battleExp = targetProcess.getBattleExp();
            double addedExp = battleExp < 1d ? 1d : (damageSource / Math.sqrt(Math.sqrt(battleExp)));
            
            targetProcess.addBattleExp(addedExp);
            targetPlayer.setData(AttachmentRegistry.PLAYER_PROCESS, targetProcess);
            PacketDistributor.sendToPlayer(targetPlayer, new SyncBattleExpPayload(targetProcess));

            targetPlayer.displayClientMessage(Component.translatable("chattext.ninjaexperience", String.format("%.1f", targetProcess.getBattleExp())), true);

            NeoForge.EVENT_BUS.post(new BattleExpChangedEvent(targetPlayer, targetProcess.getBattleExp()));
        }
        
        if (sourceEntity instanceof ServerPlayer sourcePlayer) {
            PlayerProcess sourceProcess = sourcePlayer.getData(AttachmentRegistry.PLAYER_PROCESS);
            double gainedExp = 1d;

            if (targetEntity instanceof LivingEntity targetLiving) {
                AttributeInstance attDamage = targetLiving.getAttribute(Attributes.ATTACK_DAMAGE);
                AttributeInstance attArmor = targetLiving.getAttribute(Attributes.ARMOR);
                double attackDamage = attDamage != null ? attDamage.getValue() : 0.0d;
                double armorValue = attArmor != null ? attArmor.getValue() : 0f;
                int resistance = targetLiving.hasEffect(MobEffects.DAMAGE_RESISTANCE) ? targetLiving.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1 : 1;
                
                double x = Math.sqrt(targetLiving.getMaxHealth() * attackDamage * Math.sqrt(armorValue + 1d)) * resistance;
                gainedExp = Math.min(x * Math.min(damageSource / targetLiving.getMaxHealth(), 1f) * 0.5d, 50d);
            }

            if (gainedExp > 0) {
                sourceProcess.addBattleExp(gainedExp);
                sourcePlayer.setData(AttachmentRegistry.PLAYER_PROCESS, sourceProcess);
                PacketDistributor.sendToPlayer(sourcePlayer, new SyncBattleExpPayload(sourceProcess));
                
                sourcePlayer.displayClientMessage(Component.translatable("chattext.ninjaexperience", String.format("%.1f", sourceProcess.getBattleExp())), true);

                NeoForge.EVENT_BUS.post(new BattleExpChangedEvent(sourcePlayer, sourceProcess.getBattleExp()));
            }
        }
    }

    @SubscribeEvent
	public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        var entity = event.getEntity();
		if (entity instanceof ServerPlayer serverPlayer && !entity.level().isClientSide) {
			PlayerProcess playerProcess = entity.getData(AttachmentRegistry.PLAYER_PROCESS);
            PacketDistributor.sendToPlayer(serverPlayer, new SyncBattleExpPayload(playerProcess));
		}
	}
}
