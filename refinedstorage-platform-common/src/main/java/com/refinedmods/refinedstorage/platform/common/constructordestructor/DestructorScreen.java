package com.refinedmods.refinedstorage.platform.common.constructordestructor;

import com.refinedmods.refinedstorage.platform.common.storage.FilterModeSideButtonWidget;
import com.refinedmods.refinedstorage.platform.common.support.AbstractFilterScreen;
import com.refinedmods.refinedstorage.platform.common.support.containermenu.PropertyTypes;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.refinedmods.refinedstorage.platform.common.util.IdentifierUtil.createTranslation;

public class DestructorScreen extends AbstractFilterScreen<DestructorContainerMenu> {
    public DestructorScreen(final DestructorContainerMenu menu, final Inventory playerInventory, final Component text) {
        super(menu, playerInventory, text);
    }

    @Override
    protected void init() {
        super.init();
        addSideButton(new FilterModeSideButtonWidget(
            getMenu().getProperty(PropertyTypes.FILTER_MODE),
            createTranslation("gui", "destructor.filter_mode.allow.help"),
            createTranslation("gui", "destructor.filter_mode.block.help")
        ));
        addSideButton(new DestructorPickupItemsSideButtonWidget(
            getMenu().getProperty(ConstructorDestructorPropertyTypes.PICKUP_ITEMS)
        ));
    }
}
