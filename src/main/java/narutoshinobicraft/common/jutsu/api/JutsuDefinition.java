package narutoshinobicraft.common.jutsu.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Locale;
import java.util.Optional;
import narutoshinobicraft.common.jutsu.helpers.JutsuPowerCalculator;
import net.minecraft.resources.ResourceLocation;

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
    Optional<ResourceLocation> vfxId
) {
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
        ResourceLocation.CODEC.optionalFieldOf("vfx").forGetter(def -> def.vfxId)
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
            defaultCooldownTicks, maxChargeTicks, null);
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
        ResourceLocation vfxId
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
            Optional.ofNullable(vfxId)
        );
    }

    public int resolvedMaxChargeTicks() {
        return maxChargeTicks > 0 ? maxChargeTicks : JutsuPowerCalculator.DEFAULT_MAX_USE_DURATION;
    }
}
