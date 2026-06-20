package narutoshinobicraft.common.network.payloads;

import narutoshinobicraft.common.data.attachments.PlayerProcess;
import narutoshinobicraft.common.resources.ResourceLocateNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@SuppressWarnings("null")
public record SyncBattleExpPayload (PlayerProcess playerProcess) implements CustomPacketPayload {
    public static final Type<SyncBattleExpPayload> TYPE = new Type<>(ResourceLocateNetwork.sync_battle_exp);

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBattleExpPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(PlayerProcess.CODEC), SyncBattleExpPayload::playerProcess,
            SyncBattleExpPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

