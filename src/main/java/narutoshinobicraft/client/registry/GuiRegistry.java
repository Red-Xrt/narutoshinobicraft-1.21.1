package narutoshinobicraft.client.registry;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.client.gui.overlays.OverlayChakraDisplay;
import narutoshinobicraft.common.resources.ResourceLocateGui;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID, value = Dist.CLIENT)
public class GuiRegistry {

   @SubscribeEvent
   public static void registerGuiChakra(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, ResourceLocateGui.chakraLayerId, OverlayChakraDisplay::renderChakra);
   } 
}
