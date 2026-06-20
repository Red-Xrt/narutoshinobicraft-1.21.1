package narutoshinobicraft.server.events;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.data.JutsuDefinitionManager;
import narutoshinobicraft.common.jutsu.data.ScrollPresetManagerLoader;
import narutoshinobicraft.common.jutsu.projectile.ProjectileDefinitionManagerLoader;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID)
public final class ServerModEvents {
    private ServerModEvents() {}

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ProjectileDefinitionManagerLoader());
        event.addListener(new JutsuDefinitionManager());
        event.addListener(new ScrollPresetManagerLoader());
    }
}
