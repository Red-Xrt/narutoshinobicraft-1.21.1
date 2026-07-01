package narutoshinobicraft.common.spatial.impulse.spec;

import java.util.Locale;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public enum ImpulseMode {
    push,
    pull;

    public static final Codec<ImpulseMode> CODEC = Codec.STRING.comapFlatMap(
        raw -> {
            if (raw == null || raw.isBlank()) {
                return DataResult.error(() -> "Impulse mode is blank");
            }
            try {
                return DataResult.success(ImpulseMode.valueOf(raw.trim().toLowerCase(Locale.ROOT)));
            } catch (IllegalArgumentException ex) {
                return DataResult.error(() -> "Unknown impulse mode: " + raw);
            }
        },
        mode -> mode.name().toLowerCase(Locale.ROOT)
    );
}
