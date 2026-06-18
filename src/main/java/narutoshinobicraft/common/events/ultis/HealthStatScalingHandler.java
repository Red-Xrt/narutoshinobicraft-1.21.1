package narutoshinobicraft.common.events.ultis;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.events.customs.BattleExpChangedEvent;
import narutoshinobicraft.common.resources.ResourceLocateEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID)
public class HealthStatScalingHandler {
    @SubscribeEvent
    public static void onBattleExpChanged(BattleExpChangedEvent event) {
        Player player = event.getPlayer();
        if (player.level().isClientSide()) return;

        double totalMaxHealth = event.getNewExp() * 0.005d;

        AttributeInstance maxHealthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttribute != null) {
            AttributeModifier currentModifier = maxHealthAttribute.getModifier(ResourceLocateEntity.NINJA_HEALTH_ID);
            
            if (currentModifier == null || (int)currentModifier.amount() / 2 != (int)totalMaxHealth / 2) {
                if (currentModifier != null) {
                    maxHealthAttribute.removeModifier(ResourceLocateEntity.NINJA_HEALTH_ID);
                }
                
                if (totalMaxHealth > 0) {
                    maxHealthAttribute.addPermanentModifier(new AttributeModifier(
                        ResourceLocateEntity.NINJA_HEALTH_ID, 
                        totalMaxHealth, 
                        AttributeModifier.Operation.ADD_VALUE
                    ));
                }
                
                if (player.isAlive() && player.getHealth() > 0) {
                    float newHealth = Math.min(player.getHealth() + 0.1f, (float)maxHealthAttribute.getValue());
                    player.setHealth(newHealth);
                }
            }
        }
    }
}
