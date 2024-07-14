package com.refinedmods.refinedstorage.network.test.nodefactory;

import com.refinedmods.refinedstorage.api.network.impl.node.AbstractNetworkNode;
import com.refinedmods.refinedstorage.api.network.impl.node.storage.StorageNetworkNode;

import java.util.Map;

public class StorageNetworkNodeFactory extends AbstractNetworkNodeFactory {
    public static final String PROPERTY_ENERGY_USAGE_PER_STORAGE = "energy_usage_per_storage";
    public static final String PROPERTY_SIZE = "size";

    @Override
    protected AbstractNetworkNode innerCreate(final Map<String, Object> properties) {
        final long energyUsagePerStorage = (long) properties.getOrDefault(PROPERTY_ENERGY_USAGE_PER_STORAGE, 0L);
        final int size = (int) properties.getOrDefault(PROPERTY_SIZE, 9);
        return new StorageNetworkNode(
            getEnergyUsage(properties),
            energyUsagePerStorage,
            size
        );
    }
}
