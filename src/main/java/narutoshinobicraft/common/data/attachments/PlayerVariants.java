package narutoshinobicraft.common.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

@SuppressWarnings("null")
public class PlayerVariants {
    public static final Codec<PlayerVariants> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("kamui_intangible").forGetter(PlayerVariants::IsKamui)
        ).apply(instance, PlayerVariants::new)
    );

    private boolean kamui_intangible;

    public PlayerVariants(boolean kamui){
        this.kamui_intangible = kamui;
    }

    public boolean IsKamui(){
        return this.kamui_intangible;
    }
}

