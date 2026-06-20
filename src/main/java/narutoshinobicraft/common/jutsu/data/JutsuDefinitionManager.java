package narutoshinobicraft.common.jutsu.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.action.ActionRegistry;
import narutoshinobicraft.common.jutsu.api.JutsuDefinition;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@SuppressWarnings("null")
public class JutsuDefinitionManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(JutsuDefinitionManager.class);
    private static final Gson GSON = new GsonBuilder().create();

    public JutsuDefinitionManager() {
        super(GSON, "jutsu");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        JutsuRegistry.clearDataDriven();
        int[] registered = {0};
        jsonMap.forEach((id, jsonElement) -> {
            if (parseEntry(id, jsonElement)) {
                registered[0]++;
            }
        });
        NarutoShinobiCraft.LOGGER.info("Loaded {} datapack jutsu definition(s) ({} registered)", jsonMap.size(), registered[0]);
    }

    private static boolean parseEntry(ResourceLocation id, JsonElement jsonElement) {
        if (!jsonElement.isJsonObject()) {
            LOGGER.error("Jutsu [{}] must be a JSON object", id);
            return false;
        }
        JsonObject root = jsonElement.getAsJsonObject();
        JsonElement actionJson = root.get("action");
        if (actionJson == null) {
            LOGGER.error("Jutsu [{}] missing required field 'action'", id);
            return false;
        }

        JsonObject definitionJson = root.deepCopy();
        definitionJson.remove("action");

        var definitionResult = JutsuDefinition.CODEC.parse(JsonOps.INSTANCE, definitionJson);
        var definition = definitionResult.resultOrPartial(error -> LOGGER.error("Failed to parse jutsu definition [{}]: {}", id, error)).orElse(null);
        if (definition == null) {
            return false;
        }
        var actionResult = ActionRegistry.DISPATCH_CODEC.parse(JsonOps.INSTANCE, actionJson);
        var action = actionResult.resultOrPartial(error -> LOGGER.error("Failed to parse jutsu action [{}]: {}", id, error)).orElse(null);
        if (action == null) {
            return false;
        }
        JutsuRegistry.registerDataDriven(id, definition, action);
        return true;
    }
}
