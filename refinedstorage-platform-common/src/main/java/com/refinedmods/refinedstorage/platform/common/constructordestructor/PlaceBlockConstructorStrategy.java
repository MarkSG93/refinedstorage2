package com.refinedmods.refinedstorage.platform.common.constructordestructor;

import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.platform.common.Platform;
import com.refinedmods.refinedstorage.platform.common.support.resource.ItemResource;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class PlaceBlockConstructorStrategy extends AbstractItemConstructorStrategy {
    public PlaceBlockConstructorStrategy(
        final ServerLevel level,
        final BlockPos pos,
        final Direction direction
    ) {
        super(level, pos, direction);
    }

    @Override
    protected boolean apply(
        final ItemResource itemResource,
        final ItemStack itemStack,
        final Actor actor,
        final Player actingPlayer
    ) {
        if (!(itemStack.getItem() instanceof BlockItem)) {
            return false;
        }
        return Platform.INSTANCE.placeBlock(level, pos, direction, actingPlayer, itemStack);
    }
}
