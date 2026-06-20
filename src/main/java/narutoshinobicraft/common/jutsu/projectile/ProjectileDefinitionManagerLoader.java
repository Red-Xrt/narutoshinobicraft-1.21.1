package narutoshinobicraft.common.jutsu.projectile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import narutoshinobicraft.NarutoShinobiCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("null")
public class ProjectileDefinitionManagerLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectileDefinitionManagerLoader.class);
    private static final Gson GSON = new GsonBuilder().create();

    public ProjectileDefinitionManagerLoader() {
        super(GSON, "projectiles");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, ProjectileConfig> loaded = new HashMap<>();
        jsonMap.forEach((id, jsonElement) -> {
            ProjectileConfig.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                .resultOrPartial(error -> LOGGER.error("Failed to parse projectile [{}]: {}", id, error))
                .ifPresent(config -> loaded.put(id, config));
        });
        ProjectileDefinitionManager.replaceAll(loaded);
        NarutoShinobiCraft.LOGGER.info("Loaded {} datapack projectile blueprint(s)", loaded.size());
    }
}
