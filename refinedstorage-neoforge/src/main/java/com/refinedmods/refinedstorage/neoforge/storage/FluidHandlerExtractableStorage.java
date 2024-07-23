package com.refinedmods.refinedstorage.neoforge.storage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;
import com.refinedmods.refinedstorage.common.api.support.network.AmountOverride;
import com.refinedmods.refinedstorage.common.support.resource.FluidResource;

import net.neoforged.neoforge.fluids.FluidStack;

import static com.refinedmods.refinedstorage.neoforge.support.resource.VariantUtil.toFluidAction;
import static com.refinedmods.refinedstorage.neoforge.support.resource.VariantUtil.toFluidStack;

public class FluidHandlerExtractableStorage implements ExtractableStorage {
    private final CapabilityCache capabilityCache;
    private final AmountOverride amountOverride;

    public FluidHandlerExtractableStorage(final CapabilityCache capabilityCache,
                                          final AmountOverride amountOverride) {
        this.capabilityCache = capabilityCache;
        this.amountOverride = amountOverride;
    }

    @Override
    public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        if (!(resource instanceof FluidResource fluidResource)) {
            return 0;
        }
        return capabilityCache.getFluidHandler().map(fluidHandler -> {
            final long correctedAmount = amountOverride.overrideAmount(
                resource,
                amount,
                () -> ForgeHandlerUtil.getCurrentAmount(fluidHandler, fluidResource)
            );
            if (correctedAmount == 0) {
                return 0L;
            }
            final FluidStack toExtractStack = toFluidStack(fluidResource, correctedAmount);
            return (long) fluidHandler.drain(toExtractStack, toFluidAction(action)).getAmount();
        }).orElse(0L);
    }
}
