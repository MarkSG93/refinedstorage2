package com.refinedmods.refinedstorage2.network.test.nodefactory;

import com.refinedmods.refinedstorage2.api.network.impl.node.AbstractNetworkNode;
import com.refinedmods.refinedstorage2.api.network.impl.node.multistorage.MultiStorageNetworkNode;
import com.refinedmods.refinedstorage2.network.test.AddNetworkNode;

import java.util.Map;

public class MultiStorageNetworkNodeFactory extends AbstractNetworkNodeFactory {
    public static final String PROPERTY_ENERGY_USAGE_PER_STORAGE = "energy_usage_per_storage";
    public static final String PROPERTY_SIZE = "size";

    @Override
    protected AbstractNetworkNode innerCreate(final AddNetworkNode ctx, final Map<String, Object> properties) {
        final long energyUsagePerStorage = (long) properties.getOrDefault(PROPERTY_ENERGY_USAGE_PER_STORAGE, 0L);
        final int size = (int) properties.getOrDefault(PROPERTY_SIZE, 9);
        return new MultiStorageNetworkNode(
            getEnergyUsage(properties),
            energyUsagePerStorage,
            size
        );
    }
}
