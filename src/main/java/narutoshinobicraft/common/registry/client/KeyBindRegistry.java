package narutoshinobicraft.common.registry.client;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.client.keybind.ModKeybinds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID, value = Dist.CLIENT)
public class KeyBindRegistry {
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event){
        event.register(ModKeybinds.SPECIAL_JUSTSU_1_KEYBIND.get());
        event.register(ModKeybinds.SPECIAL_JUSTSU_2_KEYBIND.get());
        event.register(ModKeybinds.SPECIAL_JUSTSU_3_KEYBIND.get());
    }
}
