package narutoshinobicraft.client.events;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.client.particle.GenericJutsuParticle;
import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import narutoshinobicraft.client.particle.registry.ParticleRegistry;
import narutoshinobicraft.client.particle.vfx.JutsuVfxSpawner;
import narutoshinobicraft.client.particle.vfx.VfxManager;
import narutoshinobicraft.common.jutsu.data.JutsuDefinitionManager;
import narutoshinobicraft.common.jutsu.data.ScrollPresetManagerLoader;
import narutoshinobicraft.common.jutsu.helpers.JutsuClientEffects;
import narutoshinobicraft.common.registry.EntityRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@SuppressWarnings("null")
@EventBusSubscriber(modid = NarutoShinobiCraft.MODID, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        JutsuClientEffects.registerChargeVfxSpawner(JutsuVfxSpawner::spawnChargeVfx);
        event.<JutsuParticleOptions>registerSpriteSet(
            ParticleRegistry.GENERIC_JUTSU_PARTICLE.get(),
            sprites -> new GenericJutsuParticle.Provider(sprites)
        );
        event.registerSpriteSet(ParticleRegistry.SMOKE.get(), sprites -> new GenericJutsuParticle.Provider(sprites));
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new JutsuDefinitionManager());
        event.registerReloadListener(new ScrollPresetManagerLoader());
        event.registerReloadListener(new VfxManager());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.JUTSU_PROJECTILE.get(), ThrownItemRenderer::new);
    }
}
