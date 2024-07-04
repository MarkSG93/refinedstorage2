package com.refinedmods.refinedstorage.platform.common.grid;

import com.refinedmods.refinedstorage.platform.common.support.resource.ItemResource;

import net.minecraft.world.entity.player.Player;

class CraftingGridRefillContextImpl implements CraftingGridRefillContext {
    private final CraftingGridBlockEntity blockEntity;

    CraftingGridRefillContextImpl(final CraftingGridBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean extract(final ItemResource resource, final Player player) {
        return blockEntity.extract(resource, player) == 1;
    }

    @Override
    public void close() {
        // no op
    }
}
