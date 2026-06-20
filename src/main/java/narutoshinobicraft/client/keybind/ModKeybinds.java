package narutoshinobicraft.client.keybind;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.util.Lazy;

public class ModKeybinds {
    public static final Lazy<KeyMapping> SPECIAL_JUSTSU_1_KEYBIND = Lazy.of(() -> new KeyMapping(
        "key.narutoshinobicraft.special_justsu_1",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R,
        "key.categories.narutoshinobicraft"
    ));

    public static final Lazy<KeyMapping> SPECIAL_JUSTSU_2_KEYBIND = Lazy.of(() -> new KeyMapping(
        "key.narutoshinobicraft.special_justsu_2",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F,
        "key.categories.narutoshinobicraft"
    ));
    public static final Lazy<KeyMapping> SPECIAL_JUSTSU_3_KEYBIND = Lazy.of(() -> new KeyMapping(
        "key.narutoshinobicraft.special_justsu_3",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C,
        "key.categories.narutoshinobicraft"
    ));
}

