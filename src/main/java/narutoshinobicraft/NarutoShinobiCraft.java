package narutoshinobicraft;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.fml.ModContainer;
import narutoshinobicraft.common.commands.CommandsManager;
import narutoshinobicraft.common.registry.AttachmentRegistry;
import narutoshinobicraft.common.registry.DataComponentRegistry;
import narutoshinobicraft.common.registry.GameRuleRegistry;
import narutoshinobicraft.common.registry.SoundRegistry;
import narutoshinobicraft.common.registry.client.ParticleRegistry;

@SuppressWarnings("null")
@Mod(NarutoShinobiCraft.MODID)
public class NarutoShinobiCraft {
    public static final String MODID = "narutoshinobicraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NarutoShinobiCraft(IEventBus modEventBus, ModContainer modContainer) {
        AttachmentRegistry.ATTACHMENT_TYPES.register(modEventBus);
        DataComponentRegistry.DATA_COMPONENT_TYPES.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);
        ParticleRegistry.PARTICLE_REGISTER.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> {
            CommandsManager.register(event.getDispatcher());
        });

        GameRuleRegistry.register();
    }
}
