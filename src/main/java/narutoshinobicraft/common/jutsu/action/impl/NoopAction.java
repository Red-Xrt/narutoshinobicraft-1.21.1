package narutoshinobicraft.common.jutsu.action.impl;

import com.mojang.serialization.MapCodec;

import narutoshinobicraft.common.data.component.JutsuContext;
import narutoshinobicraft.common.jutsu.action.api.JutsuAction;

@SuppressWarnings("null")
public final class NoopAction implements JutsuAction {
    public static final NoopAction INSTANCE = new NoopAction();
    public static final MapCodec<NoopAction> CODEC = MapCodec.unit(INSTANCE);

    private NoopAction() {}

    @Override
    public boolean execute(JutsuContext context) {
        return true;
    }

    @Override
    public MapCodec<? extends JutsuAction> codec() {
        return CODEC;
    }
}
