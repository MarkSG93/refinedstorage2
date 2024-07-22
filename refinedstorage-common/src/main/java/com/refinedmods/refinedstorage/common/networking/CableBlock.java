package com.refinedmods.refinedstorage.common.networking;

import com.refinedmods.refinedstorage.api.network.impl.node.SimpleNetworkNode;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.content.BlockColorMap;
import com.refinedmods.refinedstorage.common.content.BlockConstants;
import com.refinedmods.refinedstorage.common.content.BlockEntities;
import com.refinedmods.refinedstorage.common.content.Blocks;
import com.refinedmods.refinedstorage.common.support.AbstractBlockEntityTicker;
import com.refinedmods.refinedstorage.common.support.AbstractColoredBlock;
import com.refinedmods.refinedstorage.common.support.BaseBlockItem;
import com.refinedmods.refinedstorage.common.support.CableBlockSupport;
import com.refinedmods.refinedstorage.common.support.CableShapeCacheKey;
import com.refinedmods.refinedstorage.common.support.ColorableBlock;
import com.refinedmods.refinedstorage.common.support.NetworkNodeBlockItem;
import com.refinedmods.refinedstorage.common.support.network.BaseNetworkNodeContainerBlockEntity;
import com.refinedmods.refinedstorage.common.support.network.NetworkNodeBlockEntityTicker;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

public class CableBlock extends AbstractColoredBlock<CableBlock>
    implements ColorableBlock<CableBlock, BaseBlockItem>, SimpleWaterloggedBlock, EntityBlock {
    private static final AbstractBlockEntityTicker<BaseNetworkNodeContainerBlockEntity<SimpleNetworkNode>> TICKER =
        new NetworkNodeBlockEntityTicker<>(BlockEntities.INSTANCE::getCable);
    private static final Component HELP = createTranslation("item", "cable.help");

    public CableBlock(final DyeColor color, final MutableComponent name) {
        super(BlockConstants.CABLE_PROPERTIES, color, name);
    }

    @Override
    protected BlockState getDefaultState() {
        return CableBlockSupport.getDefaultState(super.getDefaultState());
    }

    @Override
    public boolean propagatesSkylightDown(final BlockState state, final BlockGetter blockGetter, final BlockPos pos) {
        return !state.getValue(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(final BlockState state) {
        return Boolean.TRUE.equals(state.getValue(BlockStateProperties.WATERLOGGED))
            ? Fluids.WATER.getSource(false)
            : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(final BlockState state,
                                  final Direction direction,
                                  final BlockState newState,
                                  final LevelAccessor level,
                                  final BlockPos pos,
                                  final BlockPos posFrom) {
        return CableBlockSupport.getState(state, level, pos, null);
    }

    @Override
    protected boolean isPathfindable(final BlockState state, final PathComputationType type) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext ctx) {
        return CableBlockSupport.getState(defaultBlockState(), ctx.getLevel(), ctx.getClickedPos(), null);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        CableBlockSupport.appendBlockStateProperties(builder);
    }

    @Override
    public VoxelShape getShape(final BlockState state,
                               final BlockGetter world,
                               final BlockPos pos,
                               final CollisionContext context) {
        final CableShapeCacheKey cacheKey = CableShapeCacheKey.of(state);
        return CableBlockSupport.getShape(cacheKey);
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new BaseNetworkNodeContainerBlockEntity<>(
            BlockEntities.INSTANCE.getCable(),
            pos,
            state,
            new SimpleNetworkNode(Platform.INSTANCE.getConfig().getCable().getEnergyUsage())
        );
    }

    @Override
    public BlockColorMap<CableBlock, BaseBlockItem> getBlockColorMap() {
        return Blocks.INSTANCE.getCable();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level level,
                                                                  final BlockState blockState,
                                                                  final BlockEntityType<T> type) {
        return TICKER.get(level, type);
    }

    @Override
    public BaseBlockItem createBlockItem() {
        return new NetworkNodeBlockItem(this, HELP);
    }
}
