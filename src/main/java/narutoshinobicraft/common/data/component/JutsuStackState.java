package narutoshinobicraft.common.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public record JutsuStackState(int currentIndex, Map<ResourceLocation, Long> coolDown, Map<ResourceLocation, Integer> xp, UUID ownerID, boolean hasAffinity) {
    public static final Codec<JutsuStackState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.optionalFieldOf("current_index", 0).forGetter(JutsuStackState::currentIndex),
        Codec.unboundedMap(ResourceLocation.CODEC, Codec.LONG).optionalFieldOf("cooldown", Map.of()).forGetter(JutsuStackState::coolDown),
        Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).optionalFieldOf("xp", Map.of()).forGetter(JutsuStackState::xp),
        Codec.STRING.optionalFieldOf("owner_id").xmap(
            opt -> opt.filter(id -> !id.isBlank()).map(UUID::fromString),
            owner -> owner.map(UUID::toString)
        ).forGetter(state -> Optional.ofNullable(state.ownerID())),
        Codec.BOOL.optionalFieldOf("has_affinity", false).forGetter(JutsuStackState::hasAffinity)
    ).apply(instance, (currentIndex, coolDown, xp, ownerId, hasAffinity) ->
        new JutsuStackState(currentIndex, coolDown, xp, ownerId.orElse(null), hasAffinity)
    ));

    public static final JutsuStackState EMPTY_JUTSU_STATE = new JutsuStackState(0, Map.of(), Map.of(), null, false);

    public JutsuStackState {
        coolDown = coolDown == null ? Map.of() : Map.copyOf(coolDown);
        xp = xp == null ? Map.of() : Map.copyOf(xp);
    }
}
