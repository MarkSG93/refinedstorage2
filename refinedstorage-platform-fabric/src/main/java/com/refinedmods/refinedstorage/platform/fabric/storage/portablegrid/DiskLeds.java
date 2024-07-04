package com.refinedmods.refinedstorage.platform.fabric.storage.portablegrid;

import com.refinedmods.refinedstorage.api.storage.StorageState;

import net.minecraft.client.resources.model.BakedModel;

record DiskLeds(
    BakedModel inactiveBaker,
    BakedModel normalBaker,
    BakedModel nearCapacityBaker,
    BakedModel fullBaker
) {
    BakedModel forState(final StorageState state) {
        return switch (state) {
            case INACTIVE -> inactiveBaker;
            case NEAR_CAPACITY -> nearCapacityBaker;
            case FULL -> fullBaker;
            default -> normalBaker;
        };
    }
}
