package com.refinedmods.refinedstorage.network.test.nodefactory;

import com.refinedmods.refinedstorage.api.network.impl.node.AbstractNetworkNode;
import com.refinedmods.refinedstorage.api.network.impl.node.exporter.ExporterNetworkNode;

import java.util.Map;

public class ExporterNetworkNodeFactory extends AbstractNetworkNodeFactory {
    @Override
    protected AbstractNetworkNode innerCreate(final Map<String, Object> properties) {
        return new ExporterNetworkNode(getEnergyUsage(properties));
    }
}
