package narutoshinobicraft.client.events;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.support.JutsuScrollSupport;
import narutoshinobicraft.common.network.payloads.SwitchJutsuPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID, value = Dist.CLIENT)
public final class ClientInputEvents {
    private ClientInputEvents() {}

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !player.isShiftKeyDown()) {
            return;
        }

        ItemStack scroll = JutsuScrollSupport.findHeldScroll(player);
        if (scroll.isEmpty()) {
            return;
        }

        double delta = event.getScrollDeltaY();
        if (delta == 0.0D) {
            return;
        }

        int direction = delta < 0.0D ? 1 : -1;
        PacketDistributor.sendToServer(new SwitchJutsuPayload(direction));
        event.setCanceled(true);
    }
}
