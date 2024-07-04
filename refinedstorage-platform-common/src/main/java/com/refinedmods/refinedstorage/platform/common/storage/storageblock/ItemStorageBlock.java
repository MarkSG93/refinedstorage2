package com.refinedmods.refinedstorage.platform.common.storage.storageblock;

import com.refinedmods.refinedstorage.platform.common.content.BlockConstants;
import com.refinedmods.refinedstorage.platform.common.content.BlockEntities;
import com.refinedmods.refinedstorage.platform.common.storage.ItemStorageType;
import com.refinedmods.refinedstorage.platform.common.support.network.NetworkNodeBlockEntityTicker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ItemStorageBlock extends AbstractStorageBlock<ItemStorageBlockBlockEntity> {
    private final ItemStorageType.Variant variant;

    public ItemStorageBlock(final ItemStorageType.Variant variant) {
        super(
            BlockConstants.PROPERTIES,
            new NetworkNodeBlockEntityTicker<>(() -> BlockEntities.INSTANCE.getItemStorageBlock(variant))
        );
        this.variant = variant;
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new ItemStorageBlockBlockEntity(pos, state, variant);
    }
}
