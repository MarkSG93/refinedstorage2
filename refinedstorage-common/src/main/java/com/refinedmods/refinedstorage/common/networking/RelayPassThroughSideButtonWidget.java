package com.refinedmods.refinedstorage.common.networking;

import com.refinedmods.refinedstorage.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.common.support.widget.AbstractYesNoSideButtonWidget;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

class RelayPassThroughSideButtonWidget extends AbstractYesNoSideButtonWidget {
    private static final MutableComponent TITLE = createTranslation("gui", "relay.pass_through");
    private static final MutableComponent HELP = createTranslation("gui", "relay.pass_through.help");

    RelayPassThroughSideButtonWidget(final ClientProperty<Boolean> property) {
        super(property, TITLE);
    }

    @Override
    protected int getXTexture() {
        return Boolean.TRUE.equals(property.getValue()) ? 96 : 112;
    }

    @Override
    protected int getYTexture() {
        return 0;
    }

    @Nullable
    @Override
    protected Component getHelpText() {
        return HELP;
    }
}
