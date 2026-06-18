package narutoshinobicraft.common.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.api.JutsuDefinition;
import narutoshinobicraft.common.jutsu.api.JutsuAction;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public final class JutsuRegistry {
    private static final Map<ResourceLocation, JutsuEntry> JUTSU_HUB = new HashMap<>();

    public record JutsuEntry(JutsuDefinition definition, JutsuAction action) {}

    private JutsuRegistry() {}

    public static void register(String name, JutsuDefinition definition, JutsuAction action) {
        register(ResourceLocation.fromNamespaceAndPath(NarutoShinobiCraft.MODID, name), definition, action);
    }

    public static void register(ResourceLocation id, JutsuDefinition definition, JutsuAction action) {
        if (id == null || definition == null || action == null) {
            throw new IllegalArgumentException("Jutsu registration requires id, definition, and action");
        }
        if (JUTSU_HUB.containsKey(id)) {
            NarutoShinobiCraft.LOGGER.warn("Overwriting jutsu registration for [{}]", id);
        }
        JUTSU_HUB.put(id, new JutsuEntry(definition, action));
    }

    public static @Nullable JutsuEntry getJutsu(ResourceLocation id) {
        return id == null ? null : JUTSU_HUB.get(id);
    }

    public static Map<ResourceLocation, JutsuEntry> view() {
        return Collections.unmodifiableMap(JUTSU_HUB);
    }
}
