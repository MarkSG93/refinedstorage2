package com.refinedmods.refinedstorage2.platform.common.storage;

import com.refinedmods.refinedstorage2.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage2.api.resource.ResourceKey;
import com.refinedmods.refinedstorage2.api.storage.InMemoryStorageImpl;
import com.refinedmods.refinedstorage2.api.storage.Storage;
import com.refinedmods.refinedstorage2.api.storage.limited.LimitedStorage;
import com.refinedmods.refinedstorage2.api.storage.limited.LimitedStorageImpl;
import com.refinedmods.refinedstorage2.api.storage.tracked.InMemoryTrackedStorageRepository;
import com.refinedmods.refinedstorage2.api.storage.tracked.TrackedStorage;
import com.refinedmods.refinedstorage2.api.storage.tracked.TrackedStorageImpl;
import com.refinedmods.refinedstorage2.api.storage.tracked.TrackedStorageRepository;
import com.refinedmods.refinedstorage2.platform.api.storage.PlayerActor;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageType;
import com.refinedmods.refinedstorage2.platform.common.support.resource.ItemResource;
import com.refinedmods.refinedstorage2.platform.common.support.resource.ResourceTypes;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class ItemStorageType implements StorageType {
    private static final String TAG_CAPACITY = "cap";
    private static final String TAG_STACKS = "stacks";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_CHANGED_BY = "cb";
    private static final String TAG_CHANGED_AT = "ca";

    ItemStorageType() {
    }

    @Override
    public Storage create(@Nullable final Long capacity, final Runnable listener) {
        return innerCreate(capacity, listener);
    }

    @Override
    public Storage fromTag(final CompoundTag tag, final Runnable listener) {
        final PlatformStorage storage = innerCreate(
            tag.contains(TAG_CAPACITY) ? tag.getLong(TAG_CAPACITY) : null,
            listener
        );
        final ListTag stacks = tag.getList(TAG_STACKS, Tag.TAG_COMPOUND);
        for (final Tag stackTag : stacks) {
            ResourceTypes.ITEM.fromTag((CompoundTag) stackTag).ifPresent(resource -> storage.load(
                resource,
                ((CompoundTag) stackTag).getLong(TAG_AMOUNT),
                ((CompoundTag) stackTag).getString(TAG_CHANGED_BY),
                ((CompoundTag) stackTag).getLong(TAG_CHANGED_AT)
            ));
        }
        return storage;
    }

    private PlatformStorage innerCreate(@Nullable final Long capacity, final Runnable listener) {
        final TrackedStorageRepository trackingRepository = new InMemoryTrackedStorageRepository();
        if (capacity != null) {
            final LimitedStorageImpl delegate = new LimitedStorageImpl(
                new TrackedStorageImpl(
                    new InMemoryStorageImpl(),
                    trackingRepository,
                    System::currentTimeMillis
                ),
                capacity
            );
            return new LimitedPlatformStorage(
                delegate,
                StorageTypes.ITEM,
                trackingRepository,
                listener
            );
        }
        return new PlatformStorage(
            new TrackedStorageImpl(new InMemoryStorageImpl(), trackingRepository, System::currentTimeMillis),
            StorageTypes.ITEM,
            trackingRepository,
            listener
        );
    }

    @Override
    public CompoundTag toTag(final Storage storage) {
        final CompoundTag tag = new CompoundTag();
        if (storage instanceof LimitedStorage limitedStorage) {
            tag.putLong(TAG_CAPACITY, limitedStorage.getCapacity());
        }
        final ListTag stacks = new ListTag();
        for (final ResourceAmount resourceAmount : storage.getAll()) {
            stacks.add(toTag(storage, resourceAmount));
        }
        tag.put(TAG_STACKS, stacks);
        return tag;
    }

    private CompoundTag toTag(final Storage storage, final ResourceAmount resourceAmount) {
        if (!(resourceAmount.getResource() instanceof ItemResource itemResource)) {
            throw new UnsupportedOperationException();
        }
        final CompoundTag tag = itemResource.toTag();
        tag.putLong(TAG_AMOUNT, resourceAmount.getAmount());
        if (storage instanceof TrackedStorage trackedStorage) {
            trackedStorage
                .findTrackedResourceByActorType(resourceAmount.getResource(), PlayerActor.class)
                .ifPresent(trackedResource -> {
                    tag.putString(TAG_CHANGED_BY, trackedResource.getSourceName());
                    tag.putLong(TAG_CHANGED_AT, trackedResource.getTime());
                });
        }
        return tag;
    }

    @Override
    public boolean isAllowed(final ResourceKey resource) {
        return resource instanceof ItemResource;
    }

    public enum Variant {
        ONE_K("1k", 1024L),
        FOUR_K("4k", 1024 * 4L),
        SIXTEEN_K("16k", 1024 * 4 * 4L),
        SIXTY_FOUR_K("64k", 1024 * 4 * 4 * 4L),
        CREATIVE("creative", null);

        private final String name;
        @Nullable
        private final Long capacity;

        Variant(final String name, @Nullable final Long capacity) {
            this.name = name;
            this.capacity = capacity;
        }

        public String getName() {
            return name;
        }

        @Nullable
        public Long getCapacity() {
            return capacity;
        }

        public boolean hasCapacity() {
            return capacity != null;
        }
    }
}
