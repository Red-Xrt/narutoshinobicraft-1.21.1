package narutoshinobicraft.common.events;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.client.particle.GenericJutsuParticle;
import narutoshinobicraft.common.data.record.particles.JutsuParticleOptions;
import narutoshinobicraft.common.registry.client.ParticleRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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
    }
}
