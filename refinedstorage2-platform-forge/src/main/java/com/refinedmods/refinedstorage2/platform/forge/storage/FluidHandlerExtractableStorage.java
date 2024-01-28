package com.refinedmods.refinedstorage2.platform.forge.storage;

import com.refinedmods.refinedstorage2.api.core.Action;
import com.refinedmods.refinedstorage2.api.storage.Actor;
import com.refinedmods.refinedstorage2.api.storage.ExtractableStorage;
import com.refinedmods.refinedstorage2.platform.api.exporter.AmountOverride;
import com.refinedmods.refinedstorage2.platform.api.support.resource.FluidResource;

import net.neoforged.neoforge.fluids.FluidStack;

import static com.refinedmods.refinedstorage2.platform.forge.support.resource.VariantUtil.toFluidAction;
import static com.refinedmods.refinedstorage2.platform.forge.support.resource.VariantUtil.toFluidStack;

public class FluidHandlerExtractableStorage implements ExtractableStorage<FluidResource> {
    private final CapabilityCache capabilityCache;
    private final AmountOverride amountOverride;

    public FluidHandlerExtractableStorage(final CapabilityCache capabilityCache,
                                          final AmountOverride amountOverride) {
        this.capabilityCache = capabilityCache;
        this.amountOverride = amountOverride;
    }

    @Override
    public long extract(final FluidResource resource, final long amount, final Action action, final Actor actor) {
        return capabilityCache.getFluidHandler().map(fluidHandler -> {
            final long correctedAmount = amountOverride.overrideAmount(
                resource,
                amount,
                () -> ForgeHandlerUtil.getCurrentAmount(fluidHandler, resource)
            );
            if (correctedAmount == 0) {
                return 0L;
            }
            final FluidStack toExtractStack = toFluidStack(resource, correctedAmount);
            return (long) fluidHandler.drain(toExtractStack, toFluidAction(action)).getAmount();
        }).orElse(0L);
    }
}
