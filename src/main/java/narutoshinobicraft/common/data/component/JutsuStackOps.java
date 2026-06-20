package narutoshinobicraft.common.data.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import narutoshinobicraft.common.registry.DataComponentRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SuppressWarnings("null")
public final class JutsuStackOps {
    /**
     * Per-jutsu cooldown slot. The value is overloaded on purpose (single field, two meanings):
     * <ul>
     *   <li>{@code < 0} (== {@link #DEFAULT_COOLDOWN}) &rarr; the jutsu is <b>DISABLED</b> on this scroll
     *       (not yet unlocked / toggled off). {@link #isJutsuEnabled} returns false.</li>
     *   <li>{@code == 0} &rarr; enabled and <b>ready</b> (no active cooldown).</li>
     *   <li>{@code > 0} &rarr; enabled; the value is the absolute game-time tick at which the cooldown
     *       <b>ends</b>. On cooldown while {@code value > level.getGameTime()} (see {@link #isOnCooldown}).</li>
     * </ul>
     * Keep this contract in mind: enabling/disabling and "ready vs on cooldown" all share this one number.
     */
    public static final long DEFAULT_COOLDOWN = -1L;

    private JutsuStackOps() {}

    public static JutsuStackState getState(ItemStack stack) {
        JutsuStackState state = stack.get(DataComponentRegistry.JUTSU_STACK_STATE);
        return state != null ? state : JutsuStackState.EMPTY_JUTSU_STATE;
    }

    public static void setState(ItemStack stack, JutsuStackState state) {
        stack.set(DataComponentRegistry.JUTSU_STACK_STATE, state != null ? state : JutsuStackState.EMPTY_JUTSU_STATE);
    }

    public static List<ResourceLocation> getJutsuIds(ItemStack stack) {
        List<ResourceLocation> ids = stack.get(DataComponentRegistry.JUTSU_SKILL_IDS);
        return ids != null ? List.copyOf(ids) : List.of();
    }

    public static void setJutsuIds(ItemStack stack, List<ResourceLocation> jutsuIds) {
        List<ResourceLocation> ids = jutsuIds != null ? List.copyOf(jutsuIds) : List.of();
        stack.set(DataComponentRegistry.JUTSU_SKILL_IDS, ids);
        if (ids.isEmpty()) {
            return;
        }
        updateState(stack, state -> {
            Map<ResourceLocation, Long> cooldowns = new HashMap<>();
            Map<ResourceLocation, Integer> xp = new HashMap<>();
            for (ResourceLocation id : ids) {
                cooldowns.put(id, state.coolDown().getOrDefault(id, DEFAULT_COOLDOWN));
                xp.put(id, state.xp().getOrDefault(id, 0));
            }
            return new JutsuStackState(
                clampIndex(state.currentIndex(), ids.size()),
                cooldowns,
                xp,
                state.ownerID(),
                state.hasAffinity()
            );
        });
    }

    public static int getCurrentIndex(ItemStack stack) {
        return clampIndex(getState(stack).currentIndex(), getJutsuIds(stack).size());
    }

    public static void setCurrentIndex(ItemStack stack, int index) {
        List<ResourceLocation> ids = getJutsuIds(stack);
        if (ids.isEmpty()) {
            return;
        }
        updateState(stack, state -> new JutsuStackState(
            clampIndex(index, ids.size()),
            state.coolDown(),
            state.xp(),
            state.ownerID(),
            state.hasAffinity()
        ));
    }

    public static @Nullable ResourceLocation getCurrentJutsuId(ItemStack stack) {
        List<ResourceLocation> ids = getJutsuIds(stack);
        if (ids.isEmpty()) {
            return null;
        }
        return ids.get(getCurrentIndex(stack));
    }

    public static boolean switchNextJutsu(ItemStack stack) {
        List<ResourceLocation> ids = getJutsuIds(stack);
        if (ids.isEmpty()) {
            return false;
        }
        int next = (getCurrentIndex(stack) + 1) % ids.size();
        setCurrentIndex(stack, next);
        return true;
    }

    public static boolean switchNextUsableJutsu(ItemStack stack, BiPredicate<ItemStack, ResourceLocation> canUse) {
        return switchUsableJutsu(stack, 1, canUse);
    }

    public static boolean switchUsableJutsu(ItemStack stack, int direction,
                                            BiPredicate<ItemStack, ResourceLocation> canUse) {
        List<ResourceLocation> ids = getJutsuIds(stack);
        if (ids.isEmpty()) {
            return false;
        }
        int step = direction < 0 ? -1 : 1;
        int next = getCurrentIndex(stack);
        for (int attempts = 0; attempts < ids.size(); attempts++) {
            next = Math.floorMod(next + step, ids.size());
            if (canUse.test(stack, ids.get(next))) {
                setCurrentIndex(stack, next);
                return true;
            }
        }
        return false;
    }

    public static long getCooldownEnd(ItemStack stack, ResourceLocation jutsuId) {
        if (jutsuId == null) {
            return DEFAULT_COOLDOWN;
        }
        return getState(stack).coolDown().getOrDefault(jutsuId, DEFAULT_COOLDOWN);
    }

    public static boolean isOnCooldown(ItemStack stack, ResourceLocation jutsuId, long gameTime) {
        long end = getCooldownEnd(stack, jutsuId);
        return end > gameTime;
    }

    public static void setCooldown(ItemStack stack, ResourceLocation jutsuId, long endTick) {
        if (jutsuId == null) {
            return;
        }
        updateState(stack, state -> withCooldown(state, jutsuId, endTick));
    }

    public static boolean isJutsuEnabled(ItemStack stack, ResourceLocation jutsuId) {
        return getCooldownEnd(stack, jutsuId) >= 0L;
    }

    public static void enableJutsu(ItemStack stack, ResourceLocation jutsuId, boolean enabled) {
        if (jutsuId == null || !getJutsuIds(stack).contains(jutsuId)) {
            return;
        }
        long current = getCooldownEnd(stack, jutsuId);
        if (enabled) {
            setCooldown(stack, jutsuId, current < 0L ? 0L : current);
        } else {
            setCooldown(stack, jutsuId, DEFAULT_COOLDOWN);
        }
    }

    public static void setCooldownFromNow(ItemStack stack, ResourceLocation jutsuId, Level level, long cooldownTicks) {
        if (jutsuId == null || level == null || !getJutsuIds(stack).contains(jutsuId) || !isJutsuEnabled(stack, jutsuId)) {
            return;
        }
        setCooldown(stack, jutsuId, level.getGameTime() + cooldownTicks);
    }

    public static int getXp(ItemStack stack, ResourceLocation jutsuId) {
        if (jutsuId == null) {
            return 0;
        }
        return getState(stack).xp().getOrDefault(jutsuId, 0);
    }

    public static void addXp(ItemStack stack, ResourceLocation jutsuId, int amount) {
        if (jutsuId == null || amount == 0) {
            return;
        }
        updateState(stack, state -> withXp(state, jutsuId, state.xp().getOrDefault(jutsuId, 0) + amount));
    }

    public static int getCurrentJutsuXp(ItemStack stack) {
        ResourceLocation currentId = getCurrentJutsuId(stack);
        return currentId == null ? 0 : getXp(stack, currentId);
    }

    public static void addCurrentJutsuXp(ItemStack stack, int amount) {
        ResourceLocation currentId = getCurrentJutsuId(stack);
        if (currentId != null) {
            addXp(stack, currentId, amount);
        }
    }

    public static boolean hasAffinity(ItemStack stack) {
        return getState(stack).hasAffinity();
    }

    public static void setAffinity(ItemStack stack, boolean hasAffinity) {
        updateState(stack, state -> new JutsuStackState(
            state.currentIndex(),
            state.coolDown(),
            state.xp(),
            state.ownerID(),
            hasAffinity
        ));
    }

    public static @Nullable UUID getOwnerId(ItemStack stack) {
        return getState(stack).ownerID();
    }

    public static void setOwnerIfAbsent(ItemStack stack, Player player) {
        if (player == null || getOwnerId(stack) != null) {
            return;
        }
        setOwner(stack, player);
    }

    public static void setOwner(ItemStack stack, Player player) {
        if (player == null) {
            return;
        }
        updateState(stack, state -> new JutsuStackState(
            state.currentIndex(),
            state.coolDown(),
            state.xp(),
            player.getUUID(),
            state.hasAffinity()
        ));
        Component displayName = stack.getHoverName();
        String ownerSuffix = " (" + player.getName().getString() + ")";
        if (!displayName.getString().endsWith(ownerSuffix)) {
            stack.set(DataComponents.CUSTOM_NAME, displayName.copy().append(ownerSuffix));
        }
    }

    public static boolean matchesOwner(ItemStack stack, Player player) {
        if (player == null) {
            return false;
        }
        UUID ownerId = getOwnerId(stack);
        return ownerId == null || ownerId.equals(player.getUUID());
    }

    public static void claimOwnerIfAbsent(ItemStack stack, Player player) {
        if (player == null || player.level().isClientSide() || getOwnerId(stack) != null) {
            return;
        }
        setOwner(stack, player);
        resetJutsuMaps(stack);
    }

    public static void resetJutsuMaps(ItemStack stack) {
        List<ResourceLocation> ids = getJutsuIds(stack);
        if (ids.isEmpty()) {
            return;
        }
        JutsuStackState state = getState(stack);
        Map<ResourceLocation, Long> cooldowns = new HashMap<>();
        Map<ResourceLocation, Integer> xp = new HashMap<>();
        for (ResourceLocation id : ids) {
            long previous = state.coolDown().getOrDefault(id, DEFAULT_COOLDOWN);
            cooldowns.put(id, previous >= 0L ? 0L : DEFAULT_COOLDOWN);
            xp.put(id, 0);
        }
        setState(stack, new JutsuStackState(
            state.currentIndex(),
            cooldowns,
            xp,
            state.ownerID(),
            state.hasAffinity()
        ));
    }

    public static boolean isOwner(ItemStack stack, Player player) {
        if (player == null) {
            return false;
        }
        if (!player.level().isClientSide()) {
            claimOwnerIfAbsent(stack, player);
        }
        return matchesOwner(stack, player);
    }

    private static void updateState(ItemStack stack, UnaryOperator<JutsuStackState> updater) {
        setState(stack, updater.apply(getState(stack)));
    }

    private static int clampIndex(int index, int size) {
        if (size <= 0) {
            return 0;
        }
        if (index < 0) {
            return 0;
        }
        if (index >= size) {
            return size - 1;
        }
        return index;
    }

    private static JutsuStackState withCooldown(JutsuStackState state, ResourceLocation jutsuId, long endTick) {
        Map<ResourceLocation, Long> cooldowns = new HashMap<>(state.coolDown());
        cooldowns.put(jutsuId, endTick);
        return new JutsuStackState(state.currentIndex(), cooldowns, state.xp(), state.ownerID(), state.hasAffinity());
    }

    private static JutsuStackState withXp(JutsuStackState state, ResourceLocation jutsuId, int xp) {
        Map<ResourceLocation, Integer> xpMap = new HashMap<>(state.xp());
        xpMap.put(jutsuId, Math.max(0, xp));
        return new JutsuStackState(state.currentIndex(), state.coolDown(), xpMap, state.ownerID(), state.hasAffinity());
    }
}
