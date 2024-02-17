package com.refinedmods.refinedstorage2.platform.common.support.energy;

import com.refinedmods.refinedstorage2.api.core.Action;
import com.refinedmods.refinedstorage2.api.network.energy.EnergyStorage;

import net.minecraft.world.item.ItemStack;

public class ItemEnergyStorage implements EnergyStorage {
    private static final String TAG_STORED = "stored";

    private final ItemStack stack;
    private final EnergyStorage delegate;

    public ItemEnergyStorage(final ItemStack stack, final EnergyStorage delegate) {
        this.stack = stack;
        this.delegate = delegate;
        delegate.receive(getStored(stack), Action.EXECUTE);
    }

    public static long getStored(final ItemStack stack) {
        return stack.getTag() != null ? stack.getTag().getLong(TAG_STORED) : 0;
    }

    @Override
    public long getStored() {
        return delegate.getStored();
    }

    @Override
    public long getCapacity() {
        return delegate.getCapacity();
    }

    @Override
    public long receive(final long amount, final Action action) {
        final long received = delegate.receive(amount, action);
        if (received > 0 && action == Action.EXECUTE) {
            stack.getOrCreateTag().putLong(TAG_STORED, getStored());
        }
        return received;
    }

    @Override
    public long extract(final long amount, final Action action) {
        final long extracted = delegate.extract(amount, action);
        if (extracted > 0 && action == Action.EXECUTE) {
            stack.getOrCreateTag().putLong(TAG_STORED, getStored());
        }
        return extracted;
    }
}
