package com.refinedmods.refinedstorage.api.network.impl.node.iface;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.root.RootStorage;

import java.util.Collection;
import javax.annotation.Nullable;

public interface InterfaceExportState {
    int getSlots();

    Collection<ResourceKey> expandExportCandidates(RootStorage rootStorage, ResourceKey resource);

    boolean isExportedResourceValid(ResourceKey want, ResourceKey got);

    @Nullable
    ResourceKey getRequestedResource(int slotIndex);

    long getRequestedAmount(int slotIndex);

    @Nullable
    ResourceKey getExportedResource(int slotIndex);

    long getExportedAmount(int slotIndex);

    void setExportSlot(int slotIndex, ResourceKey resource, long amount);

    void shrinkExportedAmount(int slotIndex, long amount);

    void growExportedAmount(int slotIndex, long amount);

    long insert(ResourceKey resource, long amount, Action action);

    long extract(ResourceKey resource, long amount, Action action);
}
