package narutoshinobicraft.common.data.attachments;

import java.util.ArrayList;
import java.util.List;
import narutoshinobicraft.common.data.record.DeathRecord;
import narutoshinobicraft.common.registry.AttachmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SuppressWarnings("null")
public class DeathTracker {
    private static final List<DeathRecord> recentDeaths = new ArrayList<>();

    public void addDeath(Player player) {
        long expireTime = player.level().getGameTime() + (5 * 60 * 20);

        recentDeaths.add(new DeathRecord(
            player.getUUID(),
            player.blockPosition(),
            expireTime,
            player.getTeam() != null ? player.getTeam().getName() : "",
            player.getData(AttachmentRegistry.PLAYER_PROCESS).getBattleExp()
        ));
    }

    public void tickCleanup(Level level) {
        if (level.getGameTime() % 100 == 0) {
            recentDeaths.removeIf(death -> death.isExpired(level.getGameTime()));
        }
    }

    public boolean hasTeammateDiedNearby(Player sharinganUser, double radius) {
        String myTeam = sharinganUser.getTeam() != null ? sharinganUser.getTeam().getName() : "";
        BlockPos myPos = sharinganUser.blockPosition();

        for (DeathRecord death : recentDeaths) {
            if (death.teamName().equals(myTeam) && myPos.distSqr(death.pos()) <= (radius * radius)) {
                return true;
            }
        }
        return false;
    }

    public void clearList(){
        recentDeaths.clear();
    }

    public static DeathRecord getLatestDeath(){
        if(!recentDeaths.isEmpty()){
            return recentDeaths.get(recentDeaths.size());
        }
        return null;
    }

    public boolean isPlayerCurrentlyDead(Player player){
        if(!recentDeaths.isEmpty()){
            for(int i = recentDeaths.size(); --i >= 0;){
                DeathRecord deathRecord = recentDeaths.get(i);
                if(deathRecord.playerId().equals(player.getUUID())){
                    return true;
                }
            }
        }
        return false;
    }

    public static long getPlayerDeathTimestamp(Player player) {
		if (!recentDeaths.isEmpty()) {
			for (int i = recentDeaths.size(); --i >= 0;) {
				DeathRecord deathRecord = recentDeaths.get(i);
				if (deathRecord.playerId().equals(player.getUUID()))
					return deathRecord.expireTime() - (5 * 60 * 20);
			}
		}
		return 0L;
	}

    public static double getXpBeforeDeath(Player player){
        if(!recentDeaths.isEmpty()){
            for(int i = recentDeaths.size(); --i >= 0;){
                DeathRecord deathRecord = recentDeaths.get(i);
                if(deathRecord.playerId().equals(player.getUUID())){
                    return deathRecord.lastExp();
                }
            }
        }
        return 0.0d;
    }
}

