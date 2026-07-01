package narutoshinobicraft.common.spatial.impulse.spec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Parameters for {@link ImpulseMath} push/pull formulas (ported from legacy {@code ProcedureUtils}).
 *
 * <p>{@code range} and falloff apply to push only. Pull scales by distance to origin via {@code distanceScale}.
 */
@SuppressWarnings("null")
public record ImpulseSpec(
    float strength,
    double range,
    boolean additive,
    boolean liftOnGround,
    double distanceScale
) {
    public static final float DEFAULT_STRENGTH = 1.0f;
    public static final double DEFAULT_RANGE = 3.0d;
    public static final double DEFAULT_DISTANCE_SCALE = 0.1d;

    public static final MapCodec<ImpulseSpec> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.FLOAT.optionalFieldOf("strength", DEFAULT_STRENGTH).forGetter(ImpulseSpec::strength),
        Codec.DOUBLE.optionalFieldOf("range", DEFAULT_RANGE).forGetter(ImpulseSpec::range),
        Codec.BOOL.optionalFieldOf("additive", true).forGetter(ImpulseSpec::additive),
        Codec.BOOL.optionalFieldOf("lift_on_ground", true).forGetter(ImpulseSpec::liftOnGround),
        Codec.DOUBLE.optionalFieldOf("distance_scale", DEFAULT_DISTANCE_SCALE).forGetter(ImpulseSpec::distanceScale)
    ).apply(inst, ImpulseSpec::new));

    public static ImpulseSpec pushDefaults(float strength, double range) {
        return new ImpulseSpec(strength, range, true, true, DEFAULT_DISTANCE_SCALE);
    }

    public static ImpulseSpec pullDefaults(float strength) {
        return new ImpulseSpec(strength, DEFAULT_RANGE, false, false, DEFAULT_DISTANCE_SCALE);
    }
}
