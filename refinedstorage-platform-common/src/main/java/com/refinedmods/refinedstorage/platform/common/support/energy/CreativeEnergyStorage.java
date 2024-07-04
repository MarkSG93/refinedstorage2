package com.refinedmods.refinedstorage.platform.common.support.energy;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.energy.EnergyStorage;

public class CreativeEnergyStorage implements EnergyStorage {
    public static final EnergyStorage INSTANCE = new CreativeEnergyStorage();

    private CreativeEnergyStorage() {
    }

    @Override
    public long getStored() {
        return Long.MAX_VALUE;
    }

    @Override
    public long getCapacity() {
        return Long.MAX_VALUE;
    }

    @Override
    public long receive(final long amount, final Action action) {
        return 0;
    }

    @Override
    public long extract(final long amount, final Action action) {
        return amount;
    }
}
