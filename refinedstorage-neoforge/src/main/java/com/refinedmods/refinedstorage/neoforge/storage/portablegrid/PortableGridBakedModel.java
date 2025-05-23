package com.refinedmods.refinedstorage.neoforge.storage.portablegrid;

import com.refinedmods.refinedstorage.api.storage.StorageState;
import com.refinedmods.refinedstorage.common.storage.Disk;
import com.refinedmods.refinedstorage.common.storage.portablegrid.PortableGridBlock;
import com.refinedmods.refinedstorage.common.storage.portablegrid.PortableGridBlockItem;
import com.refinedmods.refinedstorage.common.storage.portablegrid.PortableGridBlockItemRenderInfo;
import com.refinedmods.refinedstorage.common.support.direction.BiDirection;
import com.refinedmods.refinedstorage.neoforge.support.render.DiskModelBaker;
import com.refinedmods.refinedstorage.neoforge.support.render.ItemBakedModel;
import com.refinedmods.refinedstorage.neoforge.support.render.RotationTranslationModelBaker;
import com.refinedmods.refinedstorage.neoforge.support.render.TransformationBuilder;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Vector3f;

class PortableGridBakedModel extends BakedModelWrapper<BakedModel> {
    private static final Vector3f MOVE_TO_DISK_LOCATION = new Vector3f(0, -12 / 16F, 9 / 16F);
    private static final Vector3f MOVE_TO_DISK_LED_LOCATION = new Vector3f(0, -12 / 16F, 9 / 16F);

    private final LoadingCache<CacheKey, List<BakedQuad>> cache;
    private final PortableGridItemOverrides itemOverrides = new PortableGridItemOverrides();

    PortableGridBakedModel(final BakedModel baseModel,
                           final RotationTranslationModelBaker activeModelBaker,
                           final RotationTranslationModelBaker inactiveModelBaker,
                           final DiskModelBaker diskModelBaker,
                           final DiskLedBakers diskLedBakers) {
        super(baseModel);
        this.cache = CacheBuilder.newBuilder().build(CacheLoader.from(cacheKey -> {
            final RotationTranslationModelBaker baseModelBaker = cacheKey.active
                ? activeModelBaker
                : inactiveModelBaker;
            final List<BakedQuad> quads = baseModelBaker.bake(
                TransformationBuilder.create().rotate(cacheKey.direction).build(),
                cacheKey.side(),
                RandomSource.create()
            );
            if (cacheKey.disk.item() == null) {
                return quads;
            }
            final RotationTranslationModelBaker diskBaker = diskModelBaker.forDisk(cacheKey.disk.item());
            if (diskBaker != null) {
                quads.addAll(diskBaker.bake(TransformationBuilder.create()
                    .rotate(cacheKey.direction)
                    .translate(MOVE_TO_DISK_LOCATION)
                    .rotate(BiDirection.WEST)
                    .build(), cacheKey.side(), RandomSource.create()));
            }
            if (cacheKey.includeLed && cacheKey.disk.state() != StorageState.NONE) {
                quads.addAll(diskLedBakers.forState(cacheKey.disk.state()).bake(TransformationBuilder.create()
                    .translate(MOVE_TO_DISK_LED_LOCATION)
                    .rotate(BiDirection.WEST)
                    .build(), cacheKey.side(), RandomSource.create()));
            }
            return quads;
        }));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable final BlockState state,
                                    @Nullable final Direction side,
                                    final RandomSource randomSource,
                                    final ModelData extraData,
                                    @Nullable final RenderType renderType) {
        if (state == null || !(state.getBlock() instanceof PortableGridBlock portableGridBlock)) {
            return super.getQuads(state, side, randomSource);
        }
        final BiDirection direction = portableGridBlock.getDirection(state);
        if (direction == null) {
            return super.getQuads(state, side, randomSource);
        }
        final Disk disk = extraData.get(ForgePortableGridBlockEntity.DISK_PROPERTY);
        if (disk == null) {
            return super.getQuads(state, side, randomSource);
        }
        final boolean active = state.getValue(PortableGridBlock.ACTIVE);
        return cache.getUnchecked(new CacheKey(side, direction, active, disk, false));
    }

    @Override
    public ItemOverrides getOverrides() {
        return itemOverrides;
    }

    private class PortableGridItemOverrides extends ItemOverrides {
        private final LoadingCache<CacheKey, BakedModel> itemCache = CacheBuilder.newBuilder().build(
            CacheLoader.from(cacheKey -> new ItemBakedModel(
                originalModel,
                cache.getUnchecked(cacheKey),
                Collections.emptyMap()
            ))
        );

        @Override
        @Nullable
        public BakedModel resolve(final BakedModel bakedModel,
                                  final ItemStack stack,
                                  @Nullable final ClientLevel level,
                                  @Nullable final LivingEntity entity,
                                  final int seed) {
            if (level == null) {
                return null;
            }
            final PortableGridBlockItemRenderInfo renderInfo = PortableGridBlockItem.getRenderInfo(stack, level);
            return itemCache.getUnchecked(new CacheKey(
                null,
                BiDirection.NORTH,
                renderInfo.active(),
                renderInfo.disk(),
                true
            ));
        }
    }

    private record CacheKey(@Nullable Direction side,
                            BiDirection direction,
                            boolean active,
                            Disk disk,
                            boolean includeLed) {
    }
}
