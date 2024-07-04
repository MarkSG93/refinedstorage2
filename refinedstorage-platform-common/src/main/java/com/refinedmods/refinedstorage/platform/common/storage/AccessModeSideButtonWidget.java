package com.refinedmods.refinedstorage.platform.common.storage;

import com.refinedmods.refinedstorage.api.storage.AccessMode;
import com.refinedmods.refinedstorage.platform.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.platform.common.support.widget.AbstractSideButtonWidget;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static com.refinedmods.refinedstorage.platform.common.util.IdentifierUtil.createTranslation;

public class AccessModeSideButtonWidget extends AbstractSideButtonWidget {
    private static final MutableComponent TITLE = createTranslation("gui", "access_mode");
    private static final MutableComponent SUBTEXT_INSERT = createTranslation("gui", "access_mode.insert");
    private static final MutableComponent SUBTEXT_EXTRACT = createTranslation("gui", "access_mode.extract");
    private static final MutableComponent SUBTEXT_INSERT_EXTRACT =
        createTranslation("gui", "access_mode.insert_extract");
    private static final Component HELP_INSERT =
        createTranslation("gui", "access_mode.insert.help");
    private static final Component HELP_EXTRACT =
        createTranslation("gui", "access_mode.extract.help");
    private static final Component HELP_INSERT_EXTRACT =
        createTranslation("gui", "access_mode.insert_extract.help");

    private final ClientProperty<AccessMode> property;

    public AccessModeSideButtonWidget(final ClientProperty<AccessMode> property) {
        super(createPressAction(property));
        this.property = property;
    }

    private static OnPress createPressAction(final ClientProperty<AccessMode> property) {
        return btn -> property.setValue(toggle(property.getValue()));
    }

    private static AccessMode toggle(final AccessMode accessMode) {
        return switch (accessMode) {
            case INSERT_EXTRACT -> AccessMode.INSERT;
            case INSERT -> AccessMode.EXTRACT;
            case EXTRACT -> AccessMode.INSERT_EXTRACT;
        };
    }

    @Override
    protected int getXTexture() {
        return switch (property.getValue()) {
            case INSERT_EXTRACT -> 0;
            case INSERT -> 16;
            case EXTRACT -> 32;
        };
    }

    @Override
    protected int getYTexture() {
        return 240;
    }

    @Override
    protected MutableComponent getTitle() {
        return TITLE;
    }

    @Override
    protected MutableComponent getSubText() {
        return switch (property.getValue()) {
            case INSERT_EXTRACT -> SUBTEXT_INSERT_EXTRACT;
            case INSERT -> SUBTEXT_INSERT;
            case EXTRACT -> SUBTEXT_EXTRACT;
        };
    }

    @Override
    protected Component getHelpText() {
        return switch (property.getValue()) {
            case INSERT_EXTRACT -> HELP_INSERT_EXTRACT;
            case INSERT -> HELP_INSERT;
            case EXTRACT -> HELP_EXTRACT;
        };
    }
}
