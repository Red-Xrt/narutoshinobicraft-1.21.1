package narutoshinobicraft.common.jutsu.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("null")
public class ScrollPresetManagerLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrollPresetManagerLoader.class);
    private static final Gson GSON = new GsonBuilder().create();

    public ScrollPresetManagerLoader() {
        super(GSON, "scrolls");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, ScrollPreset> loaded = new HashMap<>();
        jsonMap.forEach((id, jsonElement) -> {
            ScrollPreset.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                .resultOrPartial(error -> LOGGER.error("Failed to parse scroll preset [{}]: {}", id, error))
                .ifPresent(preset -> {
                    if (validatePreset(id, preset)) {
                        loaded.put(id, preset);
                    }
                });
        });
        ScrollPresetManager.replaceAll(loaded);
        NarutoShinobiCraft.LOGGER.info("Loaded {} scroll preset(s)", loaded.size());
    }

    private static boolean validatePreset(ResourceLocation id, ScrollPreset preset) {
        if (preset.jutsuIds().isEmpty()) {
            LOGGER.error("Scroll preset [{}] has empty jutsu_ids", id);
            return false;
        }
        if (!ScrollPresetManager.isKnownItem(preset.itemId())) {
            LOGGER.error("Scroll preset [{}] references unknown item [{}]", id, preset.itemId());
            return false;
        }
        for (ResourceLocation jutsuId : preset.jutsuIds()) {
            if (JutsuRegistry.getJutsu(jutsuId) == null) {
                LOGGER.error("Scroll preset [{}] references unregistered jutsu [{}]", id, jutsuId);
                return false;
            }
        }
        return true;
    }
}
