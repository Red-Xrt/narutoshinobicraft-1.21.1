package narutoshinobicraft.common.registry;

import net.minecraft.world.level.GameRules;

@SuppressWarnings("null")
public class GameRuleRegistry {
    public static GameRules.Key<GameRules.BooleanValue> KEEP_BATTLE_EXP_RULE;

    public static void register(){
        KEEP_BATTLE_EXP_RULE = GameRules.register("keepBattleExp", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
    }
}

