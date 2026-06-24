package narutoshinobicraft.client.fx.preset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import narutoshinobicraft.client.fx.spawn.SpawnPattern;
import narutoshinobicraft.client.fx.spawn.SpawnRegistry;
import narutoshinobicraft.client.fx.speck.SpeckOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("null")
public class PresetLibrary extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PresetLibrary.class);
    private static final Gson GSON = new GsonBuilder().create();
    private static final Map<ResourceLocation, Preset> PRESETS = new HashMap<>();

    public PresetLibrary() {
        super(GSON, "vfx");
    }

    public static @Nullable Preset get(ResourceLocation id) {
        return id == null ? null : PRESETS.get(id);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        PRESETS.clear();

        jsonMap.forEach((id, jsonElement) -> {
            if (!jsonElement.isJsonObject()) {
                LOGGER.error("FX preset [{}] must be a JSON object", id);
                return;
            }
            JsonObject root = jsonElement.getAsJsonObject();
            if (!root.has("spawn")) {
                LOGGER.error("FX preset [{}] missing required field 'spawn'", id);
                return;
            }

            JsonObject speckJson = root.deepCopy();
            speckJson.remove("spawn");

            SpeckOptions.CODEC.codec().parse(JsonOps.INSTANCE, speckJson)
                .resultOrPartial(error -> LOGGER.error("Error parsing speck on preset [{}]: {}", id, error))
                .ifPresent(speck -> parseSpawn(root.get("spawn"), id)
                    .ifPresent(spawn -> PRESETS.put(id, new Preset(speck, spawn))));
        });

        LOGGER.info("Loaded {} FX preset(s)", PRESETS.size());
    }

    private static java.util.Optional<SpawnPattern> parseSpawn(JsonElement json, ResourceLocation id) {
        return SpawnRegistry.DISPATCH_CODEC.parse(JsonOps.INSTANCE, json)
            .resultOrPartial(error -> LOGGER.error("Error parsing spawn on preset [{}]: {}", id, error));
    }
}
