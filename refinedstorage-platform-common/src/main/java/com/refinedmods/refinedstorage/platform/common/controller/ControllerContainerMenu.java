package com.refinedmods.refinedstorage.platform.common.controller;

import com.refinedmods.refinedstorage.platform.common.content.Menus;
import com.refinedmods.refinedstorage.platform.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.platform.common.support.RedstoneMode;
import com.refinedmods.refinedstorage.platform.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.platform.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.platform.common.support.containermenu.ServerProperty;
import com.refinedmods.refinedstorage.platform.common.support.energy.EnergyContainerMenu;
import com.refinedmods.refinedstorage.platform.common.support.energy.EnergyInfo;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class ControllerContainerMenu extends AbstractBaseContainerMenu implements EnergyContainerMenu {
    private final EnergyInfo energyInfo;

    public ControllerContainerMenu(final int syncId,
                                   final Inventory playerInventory,
                                   final ControllerData controllerData) {
        super(Menus.INSTANCE.getController(), syncId);
        addPlayerInventory(playerInventory, 8, 107);
        this.energyInfo = EnergyInfo.forClient(
            playerInventory.player,
            controllerData.stored(),
            controllerData.capacity()
        );
        registerProperty(new ClientProperty<>(PropertyTypes.REDSTONE_MODE, RedstoneMode.IGNORE));
    }

    ControllerContainerMenu(final int syncId,
                            final Inventory playerInventory,
                            final ControllerBlockEntity controller,
                            final Player player) {
        super(Menus.INSTANCE.getController(), syncId);
        this.energyInfo = EnergyInfo.forServer(
            player,
            controller::getActualStored,
            controller::getActualCapacity
        );
        addPlayerInventory(playerInventory, 8, 107);
        registerProperty(new ServerProperty<>(
            PropertyTypes.REDSTONE_MODE,
            controller::getRedstoneMode,
            controller::setRedstoneMode
        ));
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        energyInfo.detectChanges();
    }

    @Override
    public EnergyInfo getEnergyInfo() {
        return energyInfo;
    }
}
