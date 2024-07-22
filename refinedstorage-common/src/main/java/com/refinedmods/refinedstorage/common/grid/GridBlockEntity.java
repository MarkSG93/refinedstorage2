package com.refinedmods.refinedstorage.common.grid;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.content.BlockEntities;
import com.refinedmods.refinedstorage.common.content.ContentNames;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class GridBlockEntity extends AbstractGridBlockEntity {
    public GridBlockEntity(final BlockPos pos, final BlockState state) {
        super(BlockEntities.INSTANCE.getGrid(), pos, state, Platform.INSTANCE.getConfig().getGrid().getEnergyUsage());
    }

    @Override
    public Component getDisplayName() {
        return ContentNames.GRID;
    }

    @Override
    @Nullable
    public AbstractGridContainerMenu createMenu(final int syncId, final Inventory inventory, final Player player) {
        return new GridContainerMenu(syncId, inventory, this);
    }
}
