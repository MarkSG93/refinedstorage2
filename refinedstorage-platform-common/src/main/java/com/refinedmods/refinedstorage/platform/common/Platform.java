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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.BlockHitResult;

public interface Platform {
    Platform INSTANCE = new PlatformProxy();

    MenuOpener getMenuOpener();

    long getBucketAmount();

    Config getConfig();

    boolean canEditBoxLoseFocus(EditBox editBox);

    boolean isKeyDown(KeyMapping keyMapping);

    GridResourceFactory getItemGridResourceFactory();

    GridResourceFactory getFluidGridResourceFactory();

    GridInsertionStrategyFactory getDefaultGridInsertionStrategyFactory();

    FluidRenderer getFluidRenderer();

    Optional<FluidOperationResult> drainContainer(ItemStack container);

    Optional<FluidOperationResult> fillContainer(ItemStack container, ResourceAmount resourceAmount);

    Optional<ItemStack> getFilledBucket(FluidResource fluidResource);

    TransferManager createTransferManager(AbstractContainerMenu containerMenu);

    long insertIntoContainer(Container container, ItemResource itemResource, long amount, Action action);

    ItemStack getCloneItemStack(BlockState state, Level level, BlockHitResult hitResult, Player player);

    NonNullList<ItemStack> getRemainingCraftingItems(Player player,
                                                     CraftingRecipe craftingRecipe,
                                                     CraftingInput input);

    void onItemCrafted(Player player, ItemStack craftedStack, CraftingContainer container);

    Player getFakePlayer(ServerLevel level, @Nullable UUID playerId);

    boolean canBreakBlock(Level level, BlockPos pos, BlockState state, Player player);

    boolean placeBlock(Level level, BlockPos pos, Direction direction, Player player, ItemStack stack);

    boolean placeFluid(Level level, BlockPos pos, Direction direction, Player player, FluidResource fluidResource);

    ItemStack getBlockAsItemStack(Block block,
                                  BlockState state,
                                  Direction direction,
                                  LevelReader level,
                                  BlockPos pos,
                                  Player player);

    Optional<SoundEvent> getBucketPickupSound(LiquidBlock liquidBlock, BlockState state);

    List<ClientTooltipComponent> processTooltipComponents(
        ItemStack stack,
        GuiGraphics graphics,
        int mouseX,
        Optional<TooltipComponent> imageComponent,
        List<Component> components
    );

    void renderTooltip(GuiGraphics graphics, List<ClientTooltipComponent> components, int x, int y);

    Optional<EnergyStorage> getEnergyStorage(ItemStack stack);

    <T extends CustomPacketPayload> void sendPacketToServer(T packet);

    <T extends CustomPacketPayload> void sendPacketToClient(ServerPlayer player, T packet);
}
