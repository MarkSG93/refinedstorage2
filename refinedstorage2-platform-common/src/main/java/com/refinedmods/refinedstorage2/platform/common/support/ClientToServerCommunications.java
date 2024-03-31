package com.refinedmods.refinedstorage2.platform.common.support;

import com.refinedmods.refinedstorage2.api.grid.operations.GridExtractMode;
import com.refinedmods.refinedstorage2.api.grid.operations.GridInsertMode;
import com.refinedmods.refinedstorage2.platform.api.grid.GridScrollMode;
import com.refinedmods.refinedstorage2.platform.api.support.network.bounditem.SlotReference;
import com.refinedmods.refinedstorage2.platform.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage2.platform.common.support.containermenu.PropertyType;
import com.refinedmods.refinedstorage2.platform.common.support.resource.ItemResource;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;

public interface ClientToServerCommunications {
    void sendGridExtract(PlatformResourceKey resource, GridExtractMode mode, boolean cursor);

    void sendGridScroll(PlatformResourceKey resource, GridScrollMode mode, int slotIndex);

    void sendGridInsert(GridInsertMode mode, boolean tryAlternatives);

    void sendCraftingGridClear(boolean toPlayerInventory);

    void sendCraftingGridRecipeTransfer(List<List<ItemResource>> recipe);

    <T> void sendPropertyChange(PropertyType<T> type, T value);

    void sendStorageInfoRequest(UUID storageId);

    void sendResourceSlotChange(int slotIndex, boolean tryAlternatives);

    void sendResourceFilterSlotChange(PlatformResourceKey resource, int slotIndex);

    void sendResourceSlotAmountChange(int slotIndex, long amount);

    void sendSingleAmountChange(double amount);

    void sendUseNetworkBoundItem(SlotReference slotReference);

    void sendSecurityCardPermission(ResourceLocation permissionId, boolean allowed);

    void sendSecurityCardResetPermission(ResourceLocation permissionId);

    void sendSecurityCardBoundPlayer(@Nullable UUID playerId);
}
