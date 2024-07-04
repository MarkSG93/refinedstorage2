package com.refinedmods.refinedstorage.platform.common.exporter;

import com.refinedmods.refinedstorage.platform.common.content.BlockColorMap;
import com.refinedmods.refinedstorage.platform.common.content.BlockEntities;
import com.refinedmods.refinedstorage.platform.common.content.Blocks;
import com.refinedmods.refinedstorage.platform.common.support.AbstractBlockEntityTicker;
import com.refinedmods.refinedstorage.platform.common.support.AbstractDirectionalCableBlock;
import com.refinedmods.refinedstorage.platform.common.support.BaseBlockItem;
import com.refinedmods.refinedstorage.platform.common.support.BlockItemProvider;
import com.refinedmods.refinedstorage.platform.common.support.ColorableBlock;
import com.refinedmods.refinedstorage.platform.common.support.DirectionalCableBlockShapes;
import com.refinedmods.refinedstorage.platform.common.support.NetworkNodeBlockItem;
import com.refinedmods.refinedstorage.platform.common.support.network.NetworkNodeBlockEntityTicker;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.refinedmods.refinedstorage.platform.common.util.IdentifierUtil.createTranslation;

public class ExporterBlock extends AbstractDirectionalCableBlock
    implements ColorableBlock<ExporterBlock, BaseBlockItem>, EntityBlock, BlockItemProvider<BaseBlockItem> {
    private static final Component HELP = createTranslation("item", "exporter.help");
    private static final Map<DirectionalCacheShapeCacheKey, VoxelShape> SHAPE_CACHE = new HashMap<>();
    private static final AbstractBlockEntityTicker<ExporterBlockEntity> TICKER =
        new NetworkNodeBlockEntityTicker<>(BlockEntities.INSTANCE::getExporter);
    private final DyeColor color;
    private final MutableComponent name;

    public ExporterBlock(final DyeColor color, final MutableComponent name) {
        super(SHAPE_CACHE);
        this.color = color;
        this.name = name;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new ExporterBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level level,
                                                                  final BlockState blockState,
                                                                  final BlockEntityType<T> type) {
        return TICKER.get(level, type);
    }

    @Override
    public BlockColorMap<ExporterBlock, BaseBlockItem> getBlockColorMap() {
        return Blocks.INSTANCE.getExporter();
    }

    @Override
    protected VoxelShape getExtensionShape(final Direction direction) {
        return switch (direction) {
            case NORTH -> DirectionalCableBlockShapes.EXPORTER_NORTH;
            case EAST -> DirectionalCableBlockShapes.EXPORTER_EAST;
            case SOUTH -> DirectionalCableBlockShapes.EXPORTER_SOUTH;
            case WEST -> DirectionalCableBlockShapes.EXPORTER_WEST;
            case UP -> DirectionalCableBlockShapes.EXPORTER_UP;
            case DOWN -> DirectionalCableBlockShapes.EXPORTER_DOWN;
        };
    }

    @Override
    public MutableComponent getName() {
        return name;
    }

    @Override
    public BaseBlockItem createBlockItem() {
        return new NetworkNodeBlockItem(this, HELP);
    }
}
