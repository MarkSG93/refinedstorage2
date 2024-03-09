package com.refinedmods.refinedstorage2.platform.common.storage;

import com.refinedmods.refinedstorage2.api.resource.filter.FilterMode;
import com.refinedmods.refinedstorage2.api.storage.AccessMode;
import com.refinedmods.refinedstorage2.platform.common.support.RedstoneMode;

public interface StorageConfigurationContainer {
    int getPriority();

    void setPriority(int priority);

    FilterMode getFilterMode();

    void setFilterMode(FilterMode filterMode);

    boolean isFuzzyMode();

    void setFuzzyMode(boolean fuzzyMode);

    AccessMode getAccessMode();

    void setAccessMode(AccessMode accessMode);

    RedstoneMode getRedstoneMode();

    void setRedstoneMode(RedstoneMode redstoneMode);
}
