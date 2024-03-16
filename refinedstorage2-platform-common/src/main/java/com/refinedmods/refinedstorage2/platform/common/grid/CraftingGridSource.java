package com.refinedmods.refinedstorage2.platform.common.grid;

import com.refinedmods.refinedstorage2.platform.common.support.resource.ItemResource;

import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

interface CraftingGridSource {
    CraftingMatrix getCraftingMatrix();

    ResultContainer getCraftingResult();

    NonNullList<ItemStack> getRemainingItems(Player player);

    CraftingGridRefillContext openRefillContext();

    CraftingGridRefillContext openSnapshotRefillContext(Player player);

    boolean clearMatrix(Player player, boolean toPlayerInventory);

    void transferRecipe(Player player, List<List<ItemResource>> recipe);

    void acceptQuickCraft(Player player, ItemStack craftedStack);
}
