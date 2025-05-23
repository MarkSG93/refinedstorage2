package com.refinedmods.refinedstorage.fabric.storage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;

import java.util.function.Function;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class FabricStorageInsertableStorage<T> implements InsertableStorage {
    private final BlockApiCache<Storage<T>, Direction> cache;
    private final Function<ResourceKey, @NullableType T> toPlatformMapper;
    private final Direction direction;

    public FabricStorageInsertableStorage(final BlockApiLookup<Storage<T>, Direction> lookup,
                                          final Function<ResourceKey, @NullableType T> toPlatformMapper,
                                          final ServerLevel serverLevel,
                                          final BlockPos pos,
                                          final Direction direction) {
        this.cache = BlockApiCache.create(lookup, serverLevel, pos);
        this.toPlatformMapper = toPlatformMapper;
        this.direction = direction;
    }

    public long getAmount(final ResourceKey resource) {
        final Storage<T> storage = cache.find(direction);
        if (storage == null) {
            return 0;
        }
        final T platformResource = toPlatformMapper.apply(resource);
        if (platformResource == null) {
            return 0;
        }
        return FabricStorageUtil.getCurrentAmount(storage, platformResource);
    }

    @Override
    public long insert(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        final Storage<T> storage = cache.find(direction);
        if (storage == null) {
            return 0;
        }
        final T platformResource = toPlatformMapper.apply(resource);
        if (platformResource == null) {
            return 0;
        }
        return doInsert(platformResource, amount, action, storage);
    }

    private long doInsert(final T platformResource, final long amount, final Action action, final Storage<T> storage) {
        try (Transaction tx = Transaction.openOuter()) {
            final long inserted = storage.insert(platformResource, amount, tx);
            if (action == Action.EXECUTE) {
                tx.commit();
            }
            return inserted;
        }
    }
}
