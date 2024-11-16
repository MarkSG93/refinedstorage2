package com.refinedmods.refinedstorage.common.storagemonitor;

import com.refinedmods.refinedstorage.api.autocrafting.preview.AutocraftingPreview;
import com.refinedmods.refinedstorage.api.autocrafting.preview.AutocraftingPreviewProvider;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.autocrafting.preview.AutocraftingPreviewContainerMenu;
import com.refinedmods.refinedstorage.common.autocrafting.preview.AutocraftingRequest;
import com.refinedmods.refinedstorage.common.content.Menus;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public class AutocraftingStorageMonitorContainerMenu extends AutocraftingPreviewContainerMenu
    implements AutocraftingPreviewProvider {
    @Nullable
    private final StorageMonitorBlockEntity storageMonitor;

    public AutocraftingStorageMonitorContainerMenu(final int syncId, final PlatformResourceKey resource) {
        super(Menus.INSTANCE.getAutocraftingStorageMonitor(), syncId, getRequests(resource));
        this.storageMonitor = null;
    }

    AutocraftingStorageMonitorContainerMenu(final int syncId,
                                            final PlatformResourceKey resource,
                                            final StorageMonitorBlockEntity storageMonitor) {
        super(Menus.INSTANCE.getAutocraftingStorageMonitor(), syncId, getRequests(resource));
        this.storageMonitor = storageMonitor;
    }

    private static List<AutocraftingRequest> getRequests(final PlatformResourceKey resource) {
        return List.of(AutocraftingRequest.of(
            new ResourceAmount(resource, resource.getResourceType().normalizeAmount(1.0D))
        ));
    }

    @Override
    public Optional<AutocraftingPreview> getPreview(final ResourceKey resource, final long amount) {
        return requireNonNull(storageMonitor).getPreview(resource, amount);
    }

    @Override
    public boolean startTask(final ResourceKey resource, final long amount) {
        return requireNonNull(storageMonitor).startTask(resource, amount);
    }
}
