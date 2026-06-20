package narutoshinobicraft.common.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.data.component.JutsuStackOps;
import narutoshinobicraft.common.jutsu.api.JutsuDefinition;
import narutoshinobicraft.common.jutsu.api.JutsuRender;
import narutoshinobicraft.common.jutsu.api.JutsuAction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("null")
public final class JutsuRegistry {
    private static final Map<ResourceLocation, JutsuEntry> JUTSU_HUB = new HashMap<>();
    private static final Set<ResourceLocation> DATA_DRIVEN = new HashSet<>();
    private static final Map<ResourceLocation, JutsuAction> JAVA_ACTIONS = new HashMap<>();

    public record JutsuEntry(JutsuDefinition definition, JutsuAction action, JutsuRender render) {
        public JutsuEntry(JutsuDefinition definition, JutsuAction action) {
            this(definition, action, new JutsuRender() {});
        }
    }

    private JutsuRegistry() {}

    public static void register(String name, JutsuDefinition definition, JutsuAction action) {
        register(ResourceLocation.fromNamespaceAndPath(NarutoShinobiCraft.MODID, name), definition, action, new JutsuRender() {});
    }

    public static void register(ResourceLocation id, JutsuDefinition definition, JutsuAction action, JutsuRender render) {
        if (id == null || definition == null || action == null) {
            throw new IllegalArgumentException("Jutsu registration requires id, definition, and action");
        }
        if (JUTSU_HUB.containsKey(id) && !DATA_DRIVEN.contains(id)) {
            NarutoShinobiCraft.LOGGER.warn("Overwriting Java jutsu registration for [{}]", id);
        }
        DATA_DRIVEN.remove(id);
        JUTSU_HUB.put(id, new JutsuEntry(definition, action, render));
    }

    public static void registerJavaAction(ResourceLocation id, JutsuAction action) {
        if (id == null || action == null) {
            throw new IllegalArgumentException("Java action registration requires id and action");
        }
        JAVA_ACTIONS.put(id, action);
    }

    public static Optional<JutsuAction> getJavaAction(ResourceLocation id) {
        return id == null ? Optional.empty() : Optional.ofNullable(JAVA_ACTIONS.get(id));
    }

    public static void registerDataDriven(ResourceLocation id, JutsuDefinition definition, JutsuAction action) {
        if (id == null || definition == null || action == null) {
            NarutoShinobiCraft.LOGGER.warn("Skipping datapack jutsu registration with null id, definition, or action");
            return;
        }
        if (JUTSU_HUB.containsKey(id) && !DATA_DRIVEN.contains(id)) {
            NarutoShinobiCraft.LOGGER.warn("Datapack jutsu [{}] overrides Java registration", id);
        }
        JUTSU_HUB.put(id, new JutsuEntry(definition, action));
        DATA_DRIVEN.add(id);
    }

    public static void clearDataDriven() {
        DATA_DRIVEN.forEach(JUTSU_HUB::remove);
        DATA_DRIVEN.clear();
    }

    public static @Nullable JutsuEntry getJutsu(ResourceLocation id) {
        return id == null ? null : JUTSU_HUB.get(id);
    }

    public static Optional<JutsuEntry> findEntry(@Nullable ResourceLocation id) {
        return Optional.ofNullable(getJutsu(id));
    }

    public static Optional<JutsuEntry> findCurrentEntry(ItemStack stack) {
        return findEntry(JutsuStackOps.getCurrentJutsuId(stack));
    }

    public static Map<ResourceLocation, JutsuEntry> view() {
        return Collections.unmodifiableMap(JUTSU_HUB);
    }

    public static boolean isDataDriven(ResourceLocation id) {
        return DATA_DRIVEN.contains(id);
    }
}
