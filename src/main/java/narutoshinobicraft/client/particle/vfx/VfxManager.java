package narutoshinobicraft.client.particle.vfx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("null")
public class VfxManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(VfxManager.class);
    private static final Gson GSON = new GsonBuilder().create();
    public static final Map<ResourceLocation, JutsuParticleOptions> VFX_LIBRARY = new HashMap<>();

    public VfxManager() {
        super(GSON, "vfx");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        VFX_LIBRARY.clear();
        
        jsonMap.forEach((id, jsonElement) -> {
            JutsuParticleOptions.CODEC.codec().parse(JsonOps.INSTANCE, jsonElement)
                .resultOrPartial(error -> LOGGER.error("Error when read Json file {}: {}", id, error))
                .ifPresent(options -> VFX_LIBRARY.put(id, options));
        });
        
        LOGGER.info("Successful added new {}  VFX!", VFX_LIBRARY.size());
    }
}
