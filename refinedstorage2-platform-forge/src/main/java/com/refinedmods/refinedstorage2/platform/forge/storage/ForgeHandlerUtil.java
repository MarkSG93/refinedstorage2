package com.refinedmods.refinedstorage2.platform.forge.storage;

import com.refinedmods.refinedstorage2.platform.common.support.resource.FluidResource;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import static com.refinedmods.refinedstorage2.platform.forge.support.resource.VariantUtil.isSame;

final class ForgeHandlerUtil {
    private ForgeHandlerUtil() {
    }

    static long getCurrentAmount(final IItemHandler itemHandler, final ItemStack stack) {
        long amount = 0;
        for (int i = 0; i < itemHandler.getSlots(); ++i) {
            final ItemStack slot = itemHandler.getStackInSlot(i);
            if (ItemStack.isSameItemSameComponents(slot, stack)) {
                amount += slot.getCount();
            }
        }
        return amount;
    }

    static long getCurrentAmount(final IFluidHandler fluidHandler, final FluidResource resource) {
        long amount = 0;
        for (int i = 0; i < fluidHandler.getTanks(); ++i) {
            final FluidStack tank = fluidHandler.getFluidInTank(i);
            if (isSame(resource, tank)) {
                amount += tank.getAmount();
            }
        }
        return amount;
    }
}
