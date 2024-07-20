package com.refinedmods.refinedstorage.platform.common;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.grid.view.GridResourceFactory;
import com.refinedmods.refinedstorage.api.network.energy.EnergyStorage;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.platform.api.grid.strategy.GridInsertionStrategyFactory;
import com.refinedmods.refinedstorage.platform.api.support.resource.FluidOperationResult;
import com.refinedmods.refinedstorage.platform.common.support.containermenu.MenuOpener;
import com.refinedmods.refinedstorage.platform.common.support.containermenu.TransferManager;
import com.refinedmods.refinedstorage.platform.common.support.render.FluidRenderer;
import com.refinedmods.refinedstorage.platform.common.support.resource.FluidResource;
import com.refinedmods.refinedstorage.platform.common.support.resource.ItemResource;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.BlockHitResult;

public class TestPlatform implements Platform {
    private final long bucketAmount;

    public TestPlatform(final long bucketAmount) {
        this.bucketAmount = bucketAmount;
    }

    @Override
    public MenuOpener getMenuOpener() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getBucketAmount() {
        return bucketAmount;
    }

    @Override
    public Config getConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canEditBoxLoseFocus(final EditBox editBox) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isKeyDown(final KeyMapping keyMapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GridResourceFactory getItemGridResourceFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GridResourceFactory getFluidGridResourceFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GridInsertionStrategyFactory getDefaultGridInsertionStrategyFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FluidRenderer getFluidRenderer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<FluidOperationResult> drainContainer(final ItemStack container) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<FluidOperationResult> fillContainer(final ItemStack container,
                                                        final ResourceAmount resourceAmount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<ItemStack> getFilledBucket(final FluidResource fluidResource) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TransferManager createTransferManager(final AbstractContainerMenu containerMenu) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long insertIntoContainer(final Container container, final ItemResource itemResource, final long amount,
                                    final Action action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemStack getCloneItemStack(final BlockState state, final Level level, final BlockHitResult hitResult,
                                       final Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NonNullList<ItemStack> getRemainingCraftingItems(final Player player, final CraftingRecipe craftingRecipe,
                                                            final CraftingInput input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onItemCrafted(final Player player, final ItemStack craftedStack, final CraftingContainer container) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Player getFakePlayer(final ServerLevel level, @Nullable final UUID playerId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canBreakBlock(final Level level, final BlockPos pos, final BlockState state, final Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean placeBlock(final Level level, final BlockPos pos, final Direction direction, final Player player,
                              final ItemStack stack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean placeFluid(final Level level, final BlockPos pos, final Direction direction, final Player player,
                              final FluidResource fluidResource) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemStack getBlockAsItemStack(final Block block, final BlockState state, final Direction direction,
                                         final LevelReader level,
                                         final BlockPos pos, final Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SoundEvent> getBucketPickupSound(final LiquidBlock liquidBlock, final BlockState state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ClientTooltipComponent> processTooltipComponents(final ItemStack stack, final GuiGraphics graphics,
                                                                 final int mouseX,
                                                                 final Optional<TooltipComponent> imageComponent,
                                                                 final List<Component> components) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void renderTooltip(final GuiGraphics graphics, final List<ClientTooltipComponent> components, final int x,
                              final int y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<EnergyStorage> getEnergyStorage(final ItemStack stack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends CustomPacketPayload> void sendPacketToServer(final T packet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends CustomPacketPayload> void sendPacketToClient(final ServerPlayer player, final T packet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveSavedData(final SavedData savedData, final File file, final HolderLookup.Provider provider,
                              final BiConsumer<File, HolderLookup.Provider> defaultSaveFunction) {
        throw new UnsupportedOperationException();
    }
}
