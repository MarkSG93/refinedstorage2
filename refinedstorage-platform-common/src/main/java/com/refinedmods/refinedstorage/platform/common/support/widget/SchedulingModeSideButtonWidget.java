package com.refinedmods.refinedstorage.platform.common.support.widget;

import com.refinedmods.refinedstorage.platform.common.support.SchedulingModeType;
import com.refinedmods.refinedstorage.platform.common.support.containermenu.ClientProperty;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static com.refinedmods.refinedstorage.platform.common.util.IdentifierUtil.createTranslation;

public class SchedulingModeSideButtonWidget extends AbstractSideButtonWidget {
    private static final MutableComponent TITLE = createTranslation("gui", "scheduling_mode");

    private final ClientProperty<SchedulingModeType> property;

    public SchedulingModeSideButtonWidget(final ClientProperty<SchedulingModeType> property) {
        super(createPressAction(property));
        this.property = property;
    }

    private static OnPress createPressAction(final ClientProperty<SchedulingModeType> property) {
        return btn -> property.setValue(toggle(property.getValue()));
    }

    private static SchedulingModeType toggle(final SchedulingModeType modeSettings) {
        return switch (modeSettings) {
            case DEFAULT -> SchedulingModeType.ROUND_ROBIN;
            case ROUND_ROBIN -> SchedulingModeType.RANDOM;
            case RANDOM -> SchedulingModeType.DEFAULT;
        };
    }

    @Override
    protected int getXTexture() {
        return switch (property.getValue()) {
            case DEFAULT -> 0;
            case ROUND_ROBIN -> 16;
            case RANDOM -> 32;
        };
    }

    @Override
    protected int getYTexture() {
        return 144;
    }

    @Override
    protected MutableComponent getTitle() {
        return TITLE;
    }

    @Override
    protected MutableComponent getSubText() {
        return property.getValue().getName();
    }

    @Override
    protected Component getHelpText() {
        return property.getValue().getHelpText();
    }
}
