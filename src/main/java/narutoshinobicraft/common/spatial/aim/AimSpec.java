package narutoshinobicraft.common.spatial.aim;

import java.util.Locale;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.spatial.aim.filter.EntityTargetFilter;

@SuppressWarnings("null")
public record AimSpec(double range, double hitboxGrow, boolean stopOnLiquid, EntityTargetFilter targetFilter) {

    public static final double DEFAULT_RANGE = 5.0d;

    public static final Codec<EntityTargetFilter> TARGET_FILTER_CODEC = Codec.STRING.comapFlatMap(
        raw -> {
            if (raw == null || raw.isBlank()) {
                return DataResult.error(() -> "Entity target filter is blank");
            }
            try {
                return DataResult.success(EntityTargetFilter.valueOf(raw.trim().toLowerCase(Locale.ROOT)));
            } catch (IllegalArgumentException ex) {
                return DataResult.error(() -> "Unknown entity target filter: " + raw);
            }
        },
        filter -> filter.name().toLowerCase(Locale.ROOT)
    );

    public static final Codec<AimSpec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.DOUBLE.optionalFieldOf("range", DEFAULT_RANGE).forGetter(AimSpec::range),
        Codec.DOUBLE.optionalFieldOf("hitbox_grow", 0.0d).forGetter(AimSpec::hitboxGrow),
        Codec.BOOL.optionalFieldOf("stop_on_liquid", false).forGetter(AimSpec::stopOnLiquid),
        TARGET_FILTER_CODEC.optionalFieldOf("target", EntityTargetFilter.only_enemy).forGetter(AimSpec::targetFilter)
    ).apply(instance, AimSpec::new));
}
