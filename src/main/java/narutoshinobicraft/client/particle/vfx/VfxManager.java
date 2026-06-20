package narutoshinobicraft.client.particle.vfx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import narutoshinobicraft.client.particle.vfx.emitter.registry.EmitterRegistry;
import narutoshinobicraft.client.particle.vfx.emitter.api.ParticleEmitter;
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
public class VfxManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(VfxManager.class);
    private static final Gson GSON = new GsonBuilder().create();
    private static final Map<ResourceLocation, VfxDefinition> VFX_LIBRARY = new HashMap<>();

    public VfxManager() {
        super(GSON, "vfx");
    }

    public static @Nullable VfxDefinition get(ResourceLocation id) {
        return id == null ? null : VFX_LIBRARY.get(id);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        VFX_LIBRARY.clear();

        jsonMap.forEach((id, jsonElement) -> {
            if (!jsonElement.isJsonObject()) {
                LOGGER.error("VFX [{}] must be a JSON object", id);
                return;
            }
            JsonObject root = jsonElement.getAsJsonObject();
            JsonObject particleJson = root.deepCopy();
            JsonElement castJson = particleJson.remove("cast_emitter");
            JsonElement chargeJson = particleJson.remove("charge_emitter");

            JutsuParticleOptions.CODEC.codec().parse(JsonOps.INSTANCE, particleJson)
                .resultOrPartial(error -> LOGGER.error("Error parsing VFX particle [{}]: {}", id, error))
                .ifPresent(particle -> {
                    ParticleEmitter castEmitter = parseEmitter(castJson, id, "cast_emitter");
                    ParticleEmitter chargeEmitter = parseEmitter(chargeJson, id, "charge_emitter");
                    VFX_LIBRARY.put(id, VfxDefinition.of(particle, castEmitter, chargeEmitter));
                });
        });

        LOGGER.info("Loaded {} VFX definition(s)", VFX_LIBRARY.size());
    }

    private static ParticleEmitter parseEmitter(JsonElement json, ResourceLocation vfxId, String fieldName) {
        if (json == null || json.isJsonNull()) {
            return null;
        }
        return EmitterRegistry.DISPATCH_CODEC.parse(JsonOps.INSTANCE, json)
            .resultOrPartial(error -> LOGGER.error("Error parsing {} on VFX [{}]: {}", fieldName, vfxId, error))
            .orElse(null);
    }
}
