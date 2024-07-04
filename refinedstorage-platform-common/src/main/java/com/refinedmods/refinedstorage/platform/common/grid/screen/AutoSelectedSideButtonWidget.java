package com.refinedmods.refinedstorage.platform.common.grid.screen;

import com.refinedmods.refinedstorage.platform.common.grid.AbstractGridContainerMenu;
import com.refinedmods.refinedstorage.platform.common.support.widget.AbstractSideButtonWidget;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static com.refinedmods.refinedstorage.platform.common.util.IdentifierUtil.NO;
import static com.refinedmods.refinedstorage.platform.common.util.IdentifierUtil.YES;
import static com.refinedmods.refinedstorage.platform.common.util.IdentifierUtil.createTranslation;

class AutoSelectedSideButtonWidget extends AbstractSideButtonWidget {
    private static final MutableComponent TITLE = createTranslation("gui", "grid.auto_selected");
    private static final Component HELP = createTranslation("gui", "grid.auto_selected.help");

    private final AbstractGridContainerMenu menu;

    AutoSelectedSideButtonWidget(final AbstractGridContainerMenu menu) {
        super(createPressAction(menu));
        this.menu = menu;
    }

    private static OnPress createPressAction(final AbstractGridContainerMenu menu) {
        return btn -> menu.setAutoSelected(!menu.isAutoSelected());
    }

    @Override
    protected int getXTexture() {
        return menu.isAutoSelected() ? 16 : 0;
    }

    @Override
    protected int getYTexture() {
        return 96;
    }

    @Override
    protected MutableComponent getTitle() {
        return TITLE;
    }

    @Override
    protected MutableComponent getSubText() {
        return menu.isAutoSelected() ? YES : NO;
    }

    @Override
    protected Component getHelpText() {
        return HELP;
    }
}
