package narutoshinobicraft.common.jutsu.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@SuppressWarnings("null")
public record JutsuDefinition(
    char rank,
    int requiredXp,
    double chakraCost,
    JutsuCategory type,
    float basePower,
    float powerupDelay,
    float maxPowerCap,
    long defaultCooldownTicks,
    int maxChargeTicks,
    List<ResourceLocation> onCast,
    Optional<String> nameKey
) {

    public JutsuDefinition {
        onCast = onCast == null ? List.of() : List.copyOf(onCast);
    }

    public static final Codec<JutsuCategory> CATEGORY_CODEC = Codec.STRING.comapFlatMap(
        raw -> {
            if (raw == null || raw.isBlank()) {
                return DataResult.error(() -> "Jutsu category is blank");
            }
            try {
                return DataResult.success(JutsuCategory.valueOf(raw.trim().toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException ex) {
                return DataResult.error(() -> "Unknown jutsu category: " + raw);
            }
        },
        category -> category.name().toLowerCase(Locale.ROOT)
    );

    public static final Codec<JutsuDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.xmap(
            value -> value == null || value.isEmpty() ? ' ' : value.charAt(0),
            value -> String.valueOf(value)
        ).optionalFieldOf("rank", ' ').forGetter(def -> def.rank),
        Codec.INT.optionalFieldOf("required_xp", 0).forGetter(def -> def.requiredXp),
        Codec.DOUBLE.optionalFieldOf("chakra_cost", 0.0d).forGetter(def -> def.chakraCost),
        CATEGORY_CODEC.fieldOf("type").forGetter(def -> def.type),
        Codec.FLOAT.optionalFieldOf("base_power", 0.0f).forGetter(def -> def.basePower),
        Codec.FLOAT.optionalFieldOf("powerup_delay", 50.0f).forGetter(def -> def.powerupDelay),
        Codec.FLOAT.optionalFieldOf("max_power_cap", 0.0f).forGetter(def -> def.maxPowerCap),
        Codec.LONG.optionalFieldOf("default_cooldown_ticks", 0L).forGetter(def -> def.defaultCooldownTicks),
        Codec.INT.optionalFieldOf("max_charge_ticks", 0).forGetter(def -> def.maxChargeTicks),
        ResourceLocation.CODEC.listOf().optionalFieldOf("on_cast", List.of()).forGetter(def -> def.onCast),
        Codec.STRING.optionalFieldOf("name_key").forGetter(def -> def.nameKey)
    ).apply(instance, JutsuDefinition::new));

    public static int requiredXpForRank(char rank) {
        return switch (Character.toUpperCase(rank)) {
            case 'S' -> 400;
            case 'A' -> 250;
            case 'B' -> 200;
            case 'C' -> 150;
            case 'D' -> 100;
            default -> 900;
        };
    }

    public static JutsuDefinition ofRank(
        char rank,
        double chakraCost,
        JutsuCategory type,
        float basePower,
        float powerupDelay,
        float maxPowerCap
    ) {
        return ofRank(rank, chakraCost, type, basePower, powerupDelay, maxPowerCap, 0L, 0);
    }

    public static JutsuDefinition ofRank(
        char rank,
        double chakraCost,
        JutsuCategory type,
        float basePower,
        float powerupDelay,
        float maxPowerCap,
        long defaultCooldownTicks
    ) {
        return ofRank(rank, chakraCost, type, basePower, powerupDelay, maxPowerCap, defaultCooldownTicks, 0);
    }

    public static JutsuDefinition ofRank(
        char rank,
        double chakraCost,
        JutsuCategory type,
        float basePower,
        float powerupDelay,
        float maxPowerCap,
        long defaultCooldownTicks,
        int maxChargeTicks
    ) {
        return ofRank(rank, chakraCost, type, basePower, powerupDelay, maxPowerCap,
            defaultCooldownTicks, maxChargeTicks, List.of());
    }

    public static JutsuDefinition ofRank(
        char rank,
        double chakraCost,
        JutsuCategory type,
        float basePower,
        float powerupDelay,
        float maxPowerCap,
        long defaultCooldownTicks,
        int maxChargeTicks,
        List<ResourceLocation> onCast
    ) {
        return new JutsuDefinition(
            rank,
            requiredXpForRank(rank),
            chakraCost,
            type,
            basePower,
            powerupDelay,
            maxPowerCap,
            Math.max(0L, defaultCooldownTicks),
            Math.max(0, maxChargeTicks),
            onCast,
            Optional.empty()
        );
    }

    public int resolvedMaxChargeTicks() {
        if (maxChargeTicks > 0) {
            return maxChargeTicks;
        }
        if (maxPowerCap > basePower && powerupDelay > 0.0f) {
            return Math.max(20, (int) Math.ceil((maxPowerCap - basePower) * powerupDelay));
        }
        return 100;
    }
}
