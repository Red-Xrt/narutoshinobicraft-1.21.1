package narutoshinobicraft.server.network.handler;

import narutoshinobicraft.common.jutsu.support.JutsuScrollSupport;
import narutoshinobicraft.common.network.payloads.SwitchJutsuPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class HandlerSwitchJutsu {
    private HandlerSwitchJutsu() {}

    public static void handler(SwitchJutsuPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) {
                return;
            }
            ItemStack scroll = JutsuScrollSupport.findHeldScroll(player);
            if (scroll.isEmpty()) {
                return;
            }
            JutsuScrollSupport.switchJutsu(scroll, player, payload.direction());
        });
    }
}
