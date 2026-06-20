package narutoshinobicraft.common.jutsu.projectile.motion.math;

import java.util.Locale;
import java.util.function.DoubleUnaryOperator;

import com.mojang.serialization.Codec;

import net.minecraft.util.Mth;

@SuppressWarnings("null")
public enum EasingCurves {
    LINEAR(t -> t),
    EASE_IN_OUT_CUBIC(t -> t < 0.5D ? 4.0D * t * t * t : 1.0D - Math.pow(-2.0D * t + 2.0D, 3.0D) / 2.0D),
    EASE_OUT_QUAD(t -> 1.0D - (1.0D - t) * (1.0D - t)),
    EASE_IN_QUAD(t -> t * t);

    public static final Codec<EasingCurves> CODEC = Codec.STRING.xmap(
        raw -> {
            if (raw == null || raw.isBlank()) {
                return EASE_IN_OUT_CUBIC;
            }
            String normalized = raw.trim().toLowerCase(Locale.ROOT);
            for (EasingCurves curve : values()) {
                if (curve.serializedName().equals(normalized)) {
                    return curve;
                }
            }
            throw new IllegalArgumentException("Unknown easing curve: " + raw);
        },
        EasingCurves::serializedName
    );

    private final DoubleUnaryOperator fn;

    EasingCurves(DoubleUnaryOperator fn) {
        this.fn = fn;
    }

    public double apply(double t) {
        return this.fn.applyAsDouble(Mth.clamp(t, 0.0D, 1.0D));
    }

    public String serializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
