package narutoshinobicraft.client.gui.overlays;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import narutoshinobicraft.common.registry.AttachmentRegistry;

@SuppressWarnings("null")
public class OverlayChakraDisplay {
    public static void renderChakra(GuiGraphics guiGraphics, DeltaTracker deltaTracker){
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        int warningTime = 60;
        //boolean showSageMode = false;

        Minecraft minecraft = Minecraft.getInstance();
        Player entity = minecraft.player;
        var chakra = entity.getData(AttachmentRegistry.PLAYER_CHAKRA);
        var process = entity.getData(AttachmentRegistry.PLAYER_PROCESS);
        
        //TODO: SAGEMODE TOO
        if (process.isNinja() && chakra.getCurrentChakra() > 0) {
            int baseColor = (warningTime % 20 < 10) ? 0xFF00FFFF : 0xFFFF0000;
            int startX = screenWidth / 2 - 200; 
            int barWidth = 80;
            int bottomY = screenHeight - 9; 
            double totalLayers = chakra.getCurrentChakra() / chakra.getCharkaMax(); 
            double topLayerPercentage = totalLayers - Math.floor(totalLayers); 
            
            if (totalLayers > 0 && topLayerPercentage == 0) {
                topLayerPercentage = 1.0; 
            }

            int totalFullLayers = (int) Math.ceil(totalLayers);
            int topY = screenHeight - (4 * (totalFullLayers - 1) + 9);

            guiGraphics.fill(startX - 1, topY - 1, startX + barWidth + 1, screenHeight - 5, 0xFF202020);

            for (int currentY = topY; currentY <= bottomY; currentY += 4) {
                boolean isTopLayer = (currentY == topY);
                boolean isBottomLayer = (currentY == bottomY);
                double fillPercentage = isTopLayer ? topLayerPercentage : 1.0;
                int currentWidth = (int) (fillPercentage * barWidth);
                int layerColor = isBottomLayer ? baseColor : 0xFFFFFF00;

                guiGraphics.fill(startX, currentY, startX + currentWidth, currentY + 3, layerColor);
            }

            String text = String.format("%d/%d", (int) chakra.getCurrentChakra(), (int) chakra.getCharkaMax());
            guiGraphics.drawString(minecraft.font, text, startX + 17, topY - 10, baseColor, true); //TODO: Fix this it not center if amount so big
        }
        if(warningTime > 0){
            --warningTime;
        }
    }
}