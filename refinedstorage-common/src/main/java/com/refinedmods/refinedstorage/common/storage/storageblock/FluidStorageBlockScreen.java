package com.refinedmods.refinedstorage.common.storage.storageblock;

import com.refinedmods.refinedstorage.common.support.resource.FluidResourceRendering;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FluidStorageBlockScreen extends AbstractStorageBlockScreen {
    public FluidStorageBlockScreen(final AbstractStorageBlockContainerMenu menu,
                                   final Inventory inventory,
                                   final Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected String formatQuantity(final long qty) {
        return FluidResourceRendering.format(qty);
    }
}
