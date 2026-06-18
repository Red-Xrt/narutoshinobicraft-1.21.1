package narutoshinobicraft.common.data.record;

import java.util.UUID;
import net.minecraft.core.BlockPos;

public record DeathRecord(UUID playerId, BlockPos pos, long expireTime, String teamName, double lastExp) {
    public boolean isExpired(long currentTime) {
        return currentTime > expireTime;
    }
}
