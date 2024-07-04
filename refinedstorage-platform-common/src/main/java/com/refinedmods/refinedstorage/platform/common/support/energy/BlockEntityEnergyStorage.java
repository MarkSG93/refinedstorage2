package com.refinedmods.refinedstorage.platform.common.support.energy;

import com.refinedmods.refinedstorage.api.network.energy.EnergyStorage;
import com.refinedmods.refinedstorage.api.network.impl.energy.AbstractListeningEnergyStorage;

import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityEnergyStorage extends AbstractListeningEnergyStorage {
    private final BlockEntity blockEntity;

    public BlockEntityEnergyStorage(final EnergyStorage delegate, final BlockEntity blockEntity) {
        super(delegate);
        this.blockEntity = blockEntity;
    }

    @Override
    protected void onStoredChanged(final long stored) {
        blockEntity.setChanged();
    }
}
