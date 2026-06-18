package narutoshinobicraft.common.network.payloads;

import narutoshinobicraft.common.data.attachments.PlayersChakra;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import narutoshinobicraft.common.resources.ResourceLocateNetwork;

@SuppressWarnings("null")
public record SyncChakraPayload(PlayersChakra chakra) implements CustomPacketPayload {
    
    public static final Type<SyncChakraPayload> TYPE = new Type<>(ResourceLocateNetwork.sync_ninja_data);
    
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncChakraPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(PlayersChakra.CODEC), SyncChakraPayload::chakra,
            SyncChakraPayload::new
    );
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}