package com.refinedmods.refinedstorage2.platform.forge.grid.strategy;

import com.refinedmods.refinedstorage2.api.core.Action;
import com.refinedmods.refinedstorage2.api.grid.operations.GridInsertMode;
import com.refinedmods.refinedstorage2.api.grid.operations.GridOperations;
import com.refinedmods.refinedstorage2.platform.api.grid.Grid;
import com.refinedmods.refinedstorage2.platform.api.grid.strategy.GridInsertionStrategy;
import com.refinedmods.refinedstorage2.platform.api.storage.PlayerActor;
import com.refinedmods.refinedstorage2.platform.api.support.resource.FluidResource;
import com.refinedmods.refinedstorage2.platform.common.storage.channel.StorageChannelTypes;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import static com.refinedmods.refinedstorage2.platform.forge.support.resource.VariantUtil.ofFluidStack;
import static com.refinedmods.refinedstorage2.platform.forge.support.resource.VariantUtil.toFluidAction;
import static com.refinedmods.refinedstorage2.platform.forge.support.resource.VariantUtil.toFluidStack;

public class FluidGridInsertionStrategy implements GridInsertionStrategy {
    private final AbstractContainerMenu menu;
    private final GridOperations<FluidResource> gridOperations;

    public FluidGridInsertionStrategy(final AbstractContainerMenu menu, final Player player, final Grid grid) {
        this.menu = menu;
        this.gridOperations = grid.createOperations(StorageChannelTypes.FLUID, new PlayerActor(player));
    }

    @Override
    public boolean onInsert(final GridInsertMode insertMode, final boolean tryAlternatives) {
        final IFluidHandlerItem cursorStorage = getFluidCursorStorage();
        if (cursorStorage == null) {
            return false;
        }
        final FluidStack extractableResource = cursorStorage.getFluidInTank(0);
        if (extractableResource.isEmpty()) {
            return false;
        }
        final FluidResource fluidResource = ofFluidStack(extractableResource);
        gridOperations.insert(fluidResource, insertMode, (resource, amount, action, source) -> {
            final FluidStack toDrain = toFluidStack(resource, amount == Long.MAX_VALUE ? Integer.MAX_VALUE : amount);
            final FluidStack drained = cursorStorage.drain(toDrain, toFluidAction(action));
            if (action == Action.EXECUTE) {
                menu.setCarried(cursorStorage.getContainer());
            }
            return drained.getAmount();
        });
        return true;
    }

    @Nullable
    private IFluidHandlerItem getFluidCursorStorage() {
        return getFluidStorage(menu.getCarried());
    }

    @Nullable
    private IFluidHandlerItem getFluidStorage(final ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM);
    }

    @Override
    public boolean onTransfer(final int slotIndex) {
        throw new UnsupportedOperationException();
    }
}
