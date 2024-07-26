package com.refinedmods.refinedstorage.fabric.storage.portablegrid;

import com.refinedmods.refinedstorage.common.storage.portablegrid.AbstractPortableGridBlockEntity;
import com.refinedmods.refinedstorage.common.storage.portablegrid.PortableGridType;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FabricPortableGridBlockEntity extends AbstractPortableGridBlockEntity {
    public FabricPortableGridBlockEntity(final PortableGridType type, final BlockPos pos, final BlockState state) {
        super(type, pos, state);
    }

    @Override
    @Nullable
    public Object getRenderData() {
        return disk;
    }
}
