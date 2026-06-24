package narutoshinobicraft.common.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.resources.ResourceLocation;

/**
 * Reusable "tagged-union" registry for data-driven, composable systems.
 *
 * <p>It keeps a bidirectional map between a string {@link ResourceLocation} id and the
 * {@link MapCodec} of one variant of {@code T}, and exposes a single dispatch {@link Codec} that
 * (de)serializes any {@code T} by reading/writing a {@code "type"} field in JSON.
 *
 * <p>This is the one place the polymorphic-codec boilerplate lives. The particle motion and
 * behavior registries are built on it, and future composable systems (e.g. jutsu effects) should
 * reuse the exact same pattern instead of re-implementing the two maps + dispatch by hand.
 *
 * <p>Network note: this is a {@link Codec} (JSON/NBT), not a {@code StreamCodec}. Variants are
 * resolved locally from registered codecs by id, so nothing heavy travels over the wire.
 *
 * @param <T> the base type of the union; every variant must expose its own {@code MapCodec} via the
 *            {@code codecGetter} supplied at construction.
 */
@SuppressWarnings("null")
public final class DispatchRegistry<T> {
    private final Map<ResourceLocation, MapCodec<? extends T>> byName = new HashMap<>();
    private final Map<MapCodec<? extends T>, ResourceLocation> byCodec = new HashMap<>();
    public final Codec<T> dispatchCodec;

    public DispatchRegistry(String label, Function<T, MapCodec<? extends T>> codecGetter) {
        this.dispatchCodec = ResourceLocation.CODEC.dispatch(
            "type",
            value -> {
                ResourceLocation id = byCodec.get(codecGetter.apply(value));
                if (id == null) {
                    throw new IllegalStateException(
                        "Unregistered " + label + " type for class -> " + value.getClass().getName());
                }
                return id;
            },
            id -> {
                MapCodec<? extends T> codec = byName.get(id);
                if (codec == null) {
                    throw new IllegalArgumentException(
                        "Unknown " + label + " type id (not registered) -> " + id);
                }
                return codec;
            }
        );
    }

    public void register(String name, MapCodec<? extends T> codec) {
        ResourceLocation id = ResourceLocation.parse(name);
        byName.put(id, codec);
        byCodec.put(codec, id);
    }
}
