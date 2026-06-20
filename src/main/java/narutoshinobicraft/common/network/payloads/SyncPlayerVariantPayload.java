package narutoshinobicraft.common.network.payloads;

import narutoshinobicraft.common.data.attachments.PlayerVariants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import narutoshinobicraft.common.resources.ResourceLocateNetwork;

@SuppressWarnings("null")
public record SyncPlayerVariantPayload(PlayerVariants playerVariants) implements CustomPacketPayload {

    public static final Type<SyncPlayerVariantPayload> TYPE = new Type<>(ResourceLocateNetwork.sync_ninja_variant);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlayerVariantPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(PlayerVariants.CODEC), SyncPlayerVariantPayload::playerVariants,
            SyncPlayerVariantPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

