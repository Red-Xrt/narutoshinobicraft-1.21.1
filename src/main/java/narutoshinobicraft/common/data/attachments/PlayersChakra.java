package narutoshinobicraft.common.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.registry.AttachmentRegistry;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("null")
public class PlayersChakra {
    public static final Codec<PlayersChakra> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("_chakraCurrent").forGetter(PlayersChakra::getCurrentChakra),
            Codec.DOUBLE.fieldOf("_chakraMax").forGetter(PlayersChakra::getCharkaMax)
        ).apply(instance, PlayersChakra::new)
    );

    private double _chakraCurrent;
    private double _chakraMax;
    private double _motionlessTime = 0;

    public PlayersChakra(double _chakraCurrent, double _chakraMax){
        this._chakraCurrent = _chakraCurrent;
        this._chakraMax = _chakraMax;
    }

    public double getCurrentChakra(){return this._chakraCurrent;}
    public void setCurrentChakra(double charkaSetMin){this._chakraCurrent = charkaSetMin;}
    
    public double getCharkaMax(){return this._chakraMax;}
    public void setCharkaMax(double charkaSetMax){this._chakraMax = charkaSetMax;}

    public double getMotionLessTime(){return this._motionlessTime;}
    public void setMotionLessTime(double motionLessTime){this._motionlessTime = motionLessTime;}

    public void addCharka(double charkaAdded) {
        this._chakraCurrent = Math.min(this.getCharkaMax(), this.getCurrentChakra() + charkaAdded);
    }

    public double getChakraModifier(double modifier){
        return 1.0d / (0.5d + 0.02d * modifier);
    }

    public double getChakraModifier(LivingEntity player){
        return getChakraModifier(player.getData(AttachmentRegistry.PLAYER_PROCESS).getBattleExp());
    }

    public void addMotionLessTime(double motionLessTime) {
        this._motionlessTime += motionLessTime;
    }

    public boolean consumeChakra(double amountIn, boolean ignoreMax) {
        double oldChakra = this.getCurrentChakra();
        double newChakra = oldChakra - amountIn;

        if (!ignoreMax && newChakra > this.getCharkaMax()) {
            newChakra = this.getCharkaMax();
        }

        if (newChakra < 0.0d) {
            newChakra = 0.0d;
        }

        this.setCurrentChakra(newChakra);
        
        return oldChakra != newChakra;
    }

    public boolean consumeChakra(double amountIn) {
        return this.consumeChakra(amountIn, false);
    }

    public void consumeChakra(float percent, boolean ignoreMax) {
        if (percent > 1.0f) {
            percent = percent / 100.0f;
        }
        
        double amountToConsume = this.getCharkaMax() * (double) percent;
        this.consumeChakra(amountToConsume, ignoreMax);
    }

    public void consumeChakra(float percent) {
        this.consumeChakra(percent, false);
    }

    public void clear() {
        this.setCurrentChakra(0.0d);
    }

    public boolean isFull() {
        return this.getCurrentChakra() >= this.getCharkaMax();
    }
}
