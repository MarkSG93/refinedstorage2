package com.refinedmods.refinedstorage2.platform.common.storage.diskdrive;

import com.refinedmods.refinedstorage2.platform.api.storage.StorageInfo;

import java.util.Optional;

import net.minecraft.world.item.ItemStack;

class EmptyStorageDiskInfoAccessor implements StorageDiskInfoAccessor {
    @Override
    public Optional<StorageInfo> getInfo(final ItemStack stack) {
        return Optional.empty();
    }
}
