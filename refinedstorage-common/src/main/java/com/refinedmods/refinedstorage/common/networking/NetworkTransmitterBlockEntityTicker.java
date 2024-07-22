package com.refinedmods.refinedstorage.common.networking;

import com.refinedmods.refinedstorage.api.network.impl.node.SimpleNetworkNode;
import com.refinedmods.refinedstorage.common.content.BlockEntities;
import com.refinedmods.refinedstorage.common.support.network.NetworkNodeBlockEntityTicker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NetworkTransmitterBlockEntityTicker
    extends NetworkNodeBlockEntityTicker<SimpleNetworkNode, NetworkTransmitterBlockEntity> {
    NetworkTransmitterBlockEntityTicker() {
        super(BlockEntities.INSTANCE::getNetworkTransmitter);
    }

    @Override
    public void tick(final Level level,
                     final BlockPos pos,
                     final BlockState state,
                     final NetworkTransmitterBlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        blockEntity.updateStateInLevel(state);
    }
}
