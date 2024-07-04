package com.refinedmods.refinedstorage.platform.common.iface;

import com.refinedmods.refinedstorage.platform.common.support.AbstractBaseScreen;
import com.refinedmods.refinedstorage.platform.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.platform.common.support.widget.FuzzyModeSideButtonWidget;
import com.refinedmods.refinedstorage.platform.common.support.widget.RedstoneModeSideButtonWidget;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.refinedmods.refinedstorage.platform.common.util.IdentifierUtil.createIdentifier;

public class InterfaceScreen extends AbstractBaseScreen<InterfaceContainerMenu> {
    private static final ResourceLocation TEXTURE = createIdentifier("textures/gui/interface.png");

    public InterfaceScreen(final InterfaceContainerMenu menu,
                           final Inventory playerInventory,
                           final Component text) {
        super(menu, playerInventory, text);
        this.inventoryLabelY = 88;
        this.imageWidth = 176;
        this.imageHeight = 182;
    }

    @Override
    protected void init() {
        super.init();
        addSideButton(new RedstoneModeSideButtonWidget(getMenu().getProperty(PropertyTypes.REDSTONE_MODE)));
        addSideButton(new FuzzyModeSideButtonWidget(
            getMenu().getProperty(PropertyTypes.FUZZY_MODE),
            () -> FuzzyModeSideButtonWidget.Type.EXTRACTING_STORAGE_NETWORK
        ));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
