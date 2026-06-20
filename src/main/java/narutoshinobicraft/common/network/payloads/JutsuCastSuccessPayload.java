package narutoshinobicraft.common.network.payloads;

import narutoshinobicraft.common.resources.ResourceLocateNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public record JutsuCastSuccessPayload(ResourceLocation jutsuId, int casterId) implements CustomPacketPayload {
    public static final Type<JutsuCastSuccessPayload> TYPE = new Type<>(ResourceLocateNetwork.jutsu_cast_success);
    public static final StreamCodec<RegistryFriendlyByteBuf, JutsuCastSuccessPayload> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, JutsuCastSuccessPayload::jutsuId,
        ByteBufCodecs.VAR_INT, JutsuCastSuccessPayload::casterId,
        JutsuCastSuccessPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}

