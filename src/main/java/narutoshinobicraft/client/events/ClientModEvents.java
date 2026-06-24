package narutoshinobicraft.client.events;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.client.fx.anchor.FxAnchor;
import narutoshinobicraft.client.fx.preset.FxPlayer;
import narutoshinobicraft.client.fx.preset.PresetLibrary;
import narutoshinobicraft.client.fx.speck.FxSpeck;
import narutoshinobicraft.client.fx.speck.SpeckOptions;
import narutoshinobicraft.client.fx.speck.register.SpeckTypeRegistry;
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
        JutsuClientEffects.registerScrollAura((entity, presetId) ->
            FxPlayer.play(presetId, FxAnchor.on(entity))
        );
        JutsuClientEffects.registerProjectileTrail((entity, motion, presetId) ->
            FxPlayer.play(presetId, FxAnchor.inFlight(entity, motion))
        );
        event.<SpeckOptions>registerSpriteSet(
            SpeckTypeRegistry.GENERIC.get(),
            sprites -> new FxSpeck.Provider(sprites)
        );
        event.registerSpriteSet(SpeckTypeRegistry.SMOKE.get(), sprites -> new FxSpeck.Provider(sprites));
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new JutsuDefinitionManager());
        event.registerReloadListener(new ScrollPresetManagerLoader());
        event.registerReloadListener(new PresetLibrary());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.JUTSU_PROJECTILE.get(), ThrownItemRenderer::new);
    }
}
