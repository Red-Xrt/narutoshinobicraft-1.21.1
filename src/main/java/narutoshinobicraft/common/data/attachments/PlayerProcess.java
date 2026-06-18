package narutoshinobicraft.common.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

@SuppressWarnings("null")
public class PlayerProcess {
    public static final Codec<PlayerProcess> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("_battleExp").forGetter(PlayerProcess::getBattleExp)
        ).apply(instance, PlayerProcess::new)
    );

    private double _battleExp;

    public PlayerProcess(double _battleExp){
        this._battleExp = _battleExp;
    }

    public double getBattleExp(){return this._battleExp;}
    public void setBattleExp(double battleExp){this._battleExp = battleExp;}
    
    public boolean isNinja(){
        return getBattleExp() > 0.0d;
    }

    public double getNinjaLevel(){
        return Math.sqrt(getBattleExp());
    }

    public boolean addBattleExp(double battleExpAdded){
        double expTemp = Math.min(getBattleExp() + battleExpAdded, 100000.0d);
        setBattleExp(expTemp);
        return getBattleExp() != expTemp;
    }
}
