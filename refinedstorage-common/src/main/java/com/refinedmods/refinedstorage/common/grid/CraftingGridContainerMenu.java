package com.refinedmods.refinedstorage.common.grid;

import com.refinedmods.refinedstorage.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.api.resource.list.ResourceList;
import com.refinedmods.refinedstorage.common.content.Menus;
import com.refinedmods.refinedstorage.common.grid.view.ItemGridResource;
import com.refinedmods.refinedstorage.common.support.RedstoneMode;
import com.refinedmods.refinedstorage.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.containermenu.ServerProperty;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.apiguardian.api.API;

public class CraftingGridContainerMenu extends AbstractGridContainerMenu {
    private static final int Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT = 69;

    private final Player player;
    private final CraftingGridSource source;
    private final List<Slot> craftingMatrixSlots = new ArrayList<>();

    @Nullable
    private Consumer<Boolean> activenessListener;
    @Nullable
    private Predicate<GridResource> filterBeforeFilteringBasedOnCraftingMatrixItems;

    public CraftingGridContainerMenu(final int syncId,
                                     final Inventory playerInventory,
                                     final GridData gridData) {
        super(Menus.INSTANCE.getCraftingGrid(), syncId, playerInventory, gridData);
        this.source = new ClientCraftingGridSource();
        this.player = playerInventory.player;
        onScreenReady(0);
        registerProperty(new ClientProperty<>(PropertyTypes.REDSTONE_MODE, RedstoneMode.IGNORE));
    }

    CraftingGridContainerMenu(final int syncId,
                              final Inventory playerInventory,
                              final CraftingGridBlockEntity grid) {
        super(Menus.INSTANCE.getCraftingGrid(), syncId, playerInventory, grid);
        this.source = new CraftingGridSourceImpl(grid);
        this.player = playerInventory.player;
        onScreenReady(0);
        registerProperty(new ServerProperty<>(
            PropertyTypes.REDSTONE_MODE,
            grid::getRedstoneMode,
            grid::setRedstoneMode
        ));
    }

    public void setActivenessListener(@Nullable final Consumer<Boolean> activenessListener) {
        this.activenessListener = activenessListener;
    }

    @Override
    public void onActiveChanged(final boolean newActive) {
        super.onActiveChanged(newActive);
        if (activenessListener != null) {
            activenessListener.accept(newActive);
        }
    }

    @Override
    public boolean canTakeItemForPickAll(final ItemStack stack, final Slot slot) {
        if (slot instanceof CraftingGridResultSlot) {
            return false;
        }
        return super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    @SuppressWarnings("resource")
    public ItemStack quickMoveStack(final Player actor, final int slotIndex) {
        final Slot slot = getSlot(slotIndex);
        if (!actor.level().isClientSide()
            && slot instanceof CraftingGridResultSlot resultSlot
            && resultSlot.hasItem()) {
            final ItemStack craftedStack = resultSlot.onQuickCraft(actor);
            source.acceptQuickCraft(actor, craftedStack);
            return ItemStack.EMPTY;
        }
        return super.quickMoveStack(actor, slotIndex);
    }

    @Override
    public void onScreenReady(final int playerInventoryY) {
        super.onScreenReady(playerInventoryY);
        craftingMatrixSlots.clear();
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                final int slotX = 26 + ((x % 3) * 18);
                final int slotY = playerInventoryY
                    - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT
                    + ((y % 3) * 18);
                craftingMatrixSlots.add(addSlot(new Slot(source.getCraftingMatrix(), x + y * 3, slotX, slotY)));
            }
        }
        addSlot(new CraftingGridResultSlot(
            player,
            source,
            130 + 4,
            playerInventoryY - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT + 18
        ));
    }

    public List<Slot> getCraftingMatrixSlots() {
        return craftingMatrixSlots;
    }

    public void clear(final boolean toPlayerInventory) {
        source.clearMatrix(player, toPlayerInventory);
    }

    @API(status = API.Status.INTERNAL)
    public ResourceList getAvailableListForRecipeTransfer() {
        final ResourceList available = getView().copyBackingList();
        addContainerToList(source.getCraftingMatrix(), available);
        addContainerToList(player.getInventory(), available);
        return available;
    }

    private void addContainerToList(final Container container, final ResourceList available) {
        for (int i = 0; i < container.getContainerSize(); ++i) {
            final ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            available.add(ItemResource.ofItemStack(stack), stack.getCount());
        }
    }

    public void transferRecipe(final List<List<ItemResource>> recipe) {
        source.transferRecipe(player, recipe);
    }

    public void filterBasedOnCraftingMatrixItems() {
        final Set<ItemResource> craftingMatrixItems = getCraftingMatrixItems();
        filterBeforeFilteringBasedOnCraftingMatrixItems = getView().setFilterAndSort(
            gridResource -> gridResource instanceof ItemGridResource itemGridResource
                && itemGridResource.getUnderlyingResource() != null
                && craftingMatrixItems.contains((ItemResource) itemGridResource.getUnderlyingResource())
        );
    }

    private Set<ItemResource> getCraftingMatrixItems() {
        final Set<ItemResource> craftingMatrixItems = new HashSet<>();
        for (int i = 0; i < source.getCraftingMatrix().getContainerSize(); ++i) {
            final ItemStack craftingMatrixStack = source.getCraftingMatrix().getItem(i);
            if (craftingMatrixStack.isEmpty()) {
                continue;
            }
            craftingMatrixItems.add(ItemResource.ofItemStack(craftingMatrixStack));
        }
        return craftingMatrixItems;
    }

    public void stopFilteringBasedOnCraftingMatrixItems() {
        if (filterBeforeFilteringBasedOnCraftingMatrixItems == null) {
            return;
        }
        getView().setFilterAndSort(filterBeforeFilteringBasedOnCraftingMatrixItems);
        filterBeforeFilteringBasedOnCraftingMatrixItems = null;
    }
}
