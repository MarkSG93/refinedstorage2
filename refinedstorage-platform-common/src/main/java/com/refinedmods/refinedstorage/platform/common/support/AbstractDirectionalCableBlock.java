package com.refinedmods.refinedstorage.platform.common.support;

import com.refinedmods.refinedstorage.platform.common.content.BlockConstants;
import com.refinedmods.refinedstorage.platform.common.support.direction.DefaultDirectionType;
import com.refinedmods.refinedstorage.platform.common.support.direction.DirectionType;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractDirectionalCableBlock
    extends AbstractDirectionalBlock<Direction>
    implements SimpleWaterloggedBlock {
    private final Map<DirectionalCacheShapeCacheKey, VoxelShape> shapeCache;

    protected AbstractDirectionalCableBlock(final Map<DirectionalCacheShapeCacheKey, VoxelShape> shapeCache) {
        super(BlockConstants.CABLE_PROPERTIES);
        this.shapeCache = shapeCache;
    }

    @Override
    protected DirectionType<Direction> getDirectionType() {
        return DefaultDirectionType.FACE_CLICKED;
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
        return CableBlockSupport.getState(state, level, pos, getDirection(state));
    }

    @Override
    protected BlockState getRotatedBlockState(final BlockState state, final Level level, final BlockPos pos) {
        final BlockState rotated = super.getRotatedBlockState(state, level, pos);
        return CableBlockSupport.getState(rotated, level, pos, getDirection(rotated));
    }

    @Override
    protected boolean isPathfindable(final BlockState state, final PathComputationType type) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext ctx) {
        final BlockState stateWithDirection = Objects.requireNonNull(super.getStateForPlacement(ctx));
        final Direction direction = getDirection(stateWithDirection);
        return CableBlockSupport.getState(
            stateWithDirection,
            ctx.getLevel(),
            ctx.getClickedPos(),
            direction
        );
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
        final CableShapeCacheKey cableShapeCacheKey = CableShapeCacheKey.of(state);
        final Direction direction = getDirection(state);
        if (direction == null) {
            return CableBlockSupport.getShape(cableShapeCacheKey);
        }
        final DirectionalCacheShapeCacheKey directionalCacheShapeCacheKey = new DirectionalCacheShapeCacheKey(
            cableShapeCacheKey,
            direction
        );
        return shapeCache.computeIfAbsent(directionalCacheShapeCacheKey, this::calculateShape);
    }

    private VoxelShape calculateShape(final DirectionalCacheShapeCacheKey cacheKey) {
        return Shapes.or(
            CableBlockSupport.getShape(cacheKey.cableShapeCacheKey),
            getExtensionShape(cacheKey.direction)
        );
    }

    @Override
    @Nullable
    protected VoxelShape getScreenOpenableShape(final BlockState state) {
        final Direction direction = getDirection(state);
        if (direction == null) {
            return Shapes.empty();
        }
        return getExtensionShape(direction);
    }

    protected abstract VoxelShape getExtensionShape(Direction direction);

    protected record DirectionalCacheShapeCacheKey(CableShapeCacheKey cableShapeCacheKey, Direction direction) {
    }
}
