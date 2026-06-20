package narutoshinobicraft.common.jutsu.support;

import java.util.List;
import javax.annotation.Nullable;

import narutoshinobicraft.common.data.component.JutsuStackOps;
import narutoshinobicraft.common.jutsu.cast.JutsuCastValidator;
import narutoshinobicraft.common.jutsu.helpers.JutsuNames;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

@SuppressWarnings("null")
public final class JutsuScrollTooltip {
    private JutsuScrollTooltip() {}

    public static void append(ItemStack stack, List<Component> tooltip, @Nullable Player viewer, TooltipFlag flag) {
        if (!flag.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.general.shift").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }

        List<ResourceLocation> ids = JutsuStackOps.getJutsuIds(stack);
        int currentIndex = JutsuStackOps.getCurrentIndex(stack);
        boolean hasVisibleEntry = false;

        for (int i = 0; i < ids.size(); i++) {
            ResourceLocation id = ids.get(i);
            if (!JutsuCastValidator.isRegistered(id)) {
                if (flag.isAdvanced()) {
                    tooltip.add(Component.translatable("tooltip.jutsu.unregistered", id.toString())
                        .withStyle(ChatFormatting.RED));
                    hasVisibleEntry = true;
                }
                continue;
            }
            if (!JutsuCastValidator.canUseJutsu(stack, id, viewer)) {
                continue;
            }
            int requiredXp = JutsuCastValidator.getRequiredXp(stack, id);
            tooltip.add(Component.literal(
                (currentIndex == i ? ">" : " ")
                    + (i + 1)
                    + ": "
                    + JutsuNames.display(id).getString()
                    + " (XP: "
            ).withStyle(ChatFormatting.GRAY).append(
                Component.literal(String.valueOf(JutsuStackOps.getXp(stack, id))).withStyle(ChatFormatting.GREEN)
            ).append(
                Component.literal("/" + requiredXp).withStyle(ChatFormatting.GRAY)
            ).append(
                Component.literal(")")
            ));
            hasVisibleEntry = true;
        }

        if (!hasVisibleEntry) {
            tooltip.add(Component.translatable("tooltip.jutsu.empty").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}

