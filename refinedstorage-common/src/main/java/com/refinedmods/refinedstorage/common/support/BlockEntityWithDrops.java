package com.refinedmods.refinedstorage.common.support;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface BlockEntityWithDrops {
    NonNullList<ItemStack> getDrops();
}
