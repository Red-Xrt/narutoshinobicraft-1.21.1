package narutoshinobicraft.common.jutsu.cast;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("null")
public enum CastOutcome {
    SUCCESS,
    INVALID_STATE,
    NOT_READY,
    NO_POWER,
    INSUFFICIENT_CHAKRA,
    ACTION_FAILED;

    public boolean succeeded() {
        return this == SUCCESS;
    }

    public void notifyFailure(Player player) {
        if (this == SUCCESS || player == null) {
            return;
        }
        player.displayClientMessage(
            Component.translatable(switch (this) {
                case NO_POWER -> "chattext.jutsu.no_power";
                case INSUFFICIENT_CHAKRA -> "chattext.jutsu.no_chakra";
                case NOT_READY -> "chattext.jutsu.not_ready";
                case ACTION_FAILED -> "chattext.jutsu.action_failed";
                case INVALID_STATE -> "chattext.jutsu.cast_failed";
                default -> "chattext.jutsu.cast_failed";
            }),
            true
        );
    }
}
