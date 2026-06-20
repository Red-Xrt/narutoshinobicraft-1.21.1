package narutoshinobicraft.common.jutsu.projectile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public final class ProjectileDefinitionManager {
    private static final Map<ResourceLocation, ProjectileConfig> LIBRARY = new HashMap<>();

    private ProjectileDefinitionManager() {}

    public static void replaceAll(Map<ResourceLocation, ProjectileConfig> loaded) {
        LIBRARY.clear();
        LIBRARY.putAll(loaded);
    }

    public static Optional<ProjectileConfig> get(ResourceLocation id) {
        return id == null ? Optional.empty() : Optional.ofNullable(LIBRARY.get(id));
    }

    public static Map<ResourceLocation, ProjectileConfig> view() {
        return Collections.unmodifiableMap(LIBRARY);
    }
}
