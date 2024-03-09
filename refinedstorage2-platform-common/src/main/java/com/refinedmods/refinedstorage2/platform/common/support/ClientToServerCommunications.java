package com.refinedmods.refinedstorage2.platform.common.support;

import com.refinedmods.refinedstorage2.api.grid.operations.GridExtractMode;
import com.refinedmods.refinedstorage2.api.grid.operations.GridInsertMode;
import com.refinedmods.refinedstorage2.api.resource.ResourceKey;
import com.refinedmods.refinedstorage2.platform.api.grid.GridScrollMode;
import com.refinedmods.refinedstorage2.platform.api.storage.channel.PlatformStorageChannelType;
import com.refinedmods.refinedstorage2.platform.api.support.network.bounditem.SlotReference;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ItemResource;
import com.refinedmods.refinedstorage2.platform.common.support.containermenu.PropertyType;

import java.util.List;
import java.util.UUID;

public interface ClientToServerCommunications {
    void sendGridExtract(PlatformStorageChannelType storageChannelType,
                         ResourceKey resource,
                         GridExtractMode mode,
                         boolean cursor);

    void sendGridScroll(PlatformStorageChannelType storageChannelType,
                        ResourceKey resource,
                        GridScrollMode mode,
                        int slotIndex);

    void sendGridInsert(GridInsertMode mode, boolean tryAlternatives);

    void sendCraftingGridClear(boolean toPlayerInventory);

    void sendCraftingGridRecipeTransfer(List<List<ItemResource>> recipe);

    <T> void sendPropertyChange(PropertyType<T> type, T value);

    void sendStorageInfoRequest(UUID storageId);

    void sendResourceSlotChange(int slotIndex, boolean tryAlternatives);

    void sendResourceFilterSlotChange(
        PlatformStorageChannelType storageChannelType,
        ResourceKey resource,
        int slotIndex
    );

    void sendResourceSlotAmountChange(int slotIndex, long amount);

    void sendSingleAmountChange(double amount);

    void sendUseNetworkBoundItem(SlotReference slotReference);
}
