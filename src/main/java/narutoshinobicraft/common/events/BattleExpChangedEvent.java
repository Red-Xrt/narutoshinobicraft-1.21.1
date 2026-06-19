package narutoshinobicraft.common.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class BattleExpChangedEvent extends Event{
    private final Player player;
    private final double newExp;

    public BattleExpChangedEvent(Player player, double newExp) {
        this.player = player;
        this.newExp = newExp;
    }

    public Player getPlayer() { return this.player; }
    public double getNewExp() { return this.newExp; }
}
