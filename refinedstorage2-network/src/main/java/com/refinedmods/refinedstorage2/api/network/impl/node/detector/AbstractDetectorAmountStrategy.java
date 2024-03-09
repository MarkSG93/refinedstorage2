package com.refinedmods.refinedstorage2.api.network.impl.node.detector;

import com.refinedmods.refinedstorage2.api.network.Network;
import com.refinedmods.refinedstorage2.api.network.component.StorageNetworkComponent;
import com.refinedmods.refinedstorage2.api.storage.ResourceTemplate;
import com.refinedmods.refinedstorage2.api.storage.channel.StorageChannel;

public abstract class AbstractDetectorAmountStrategy implements DetectorAmountStrategy {
    protected StorageChannel getStorageChannel(final Network network, final ResourceTemplate template) {
        return network.getComponent(StorageNetworkComponent.class).getStorageChannel(template.storageChannelType());
    }
}
