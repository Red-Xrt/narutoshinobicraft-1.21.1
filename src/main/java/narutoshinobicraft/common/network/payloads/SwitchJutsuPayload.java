package narutoshinobicraft.common.network.payloads;

import narutoshinobicraft.common.resources.ResourceLocateNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@SuppressWarnings("null")
public record SwitchJutsuPayload(int direction) implements CustomPacketPayload {
    public static final Type<SwitchJutsuPayload> TYPE = new Type<>(ResourceLocateNetwork.switch_jutsu);

    public static final StreamCodec<RegistryFriendlyByteBuf, SwitchJutsuPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, SwitchJutsuPayload::direction,
        SwitchJutsuPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
