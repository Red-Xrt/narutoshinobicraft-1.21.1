package narutoshinobicraft.client.events;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.client.particle.GenericJutsuParticle;
import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import narutoshinobicraft.client.particle.registry.ParticleRegistry;
import narutoshinobicraft.client.particle.vfx.VfxManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.<JutsuParticleOptions>registerSpriteSet(
            ParticleRegistry.GENERIC_JUTSU_PARTICLE.get(),
            sprites -> new GenericJutsuParticle.Provider(sprites)
        );
        event.registerSpriteSet(ParticleRegistry.SMOKE.get(), sprites -> new GenericJutsuParticle.Provider(sprites));
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new VfxManager());
    }
    
}
