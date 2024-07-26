package com.refinedmods.refinedstorage.common.support;

import com.refinedmods.refinedstorage.common.content.BlockColorMap;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

public interface ColorableBlock<T extends Block & BlockItemProvider<I>, I extends BlockItem> {
    BlockColorMap<T, I> getBlockColorMap();

    DyeColor getColor();

    default boolean canAlwaysConnect() {
        return getBlockColorMap().isDefaultColor(getColor());
    }
}
