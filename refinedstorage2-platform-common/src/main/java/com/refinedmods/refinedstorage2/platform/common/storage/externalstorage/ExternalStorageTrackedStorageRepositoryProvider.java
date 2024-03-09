package com.refinedmods.refinedstorage2.platform.common.storage.externalstorage;

import com.refinedmods.refinedstorage2.api.network.impl.node.externalstorage.TrackedStorageRepositoryProvider;
import com.refinedmods.refinedstorage2.api.storage.channel.StorageChannelType;
import com.refinedmods.refinedstorage2.api.storage.tracked.TrackedStorageRepository;
import com.refinedmods.refinedstorage2.platform.api.storage.channel.PlatformStorageChannelType;
import com.refinedmods.refinedstorage2.platform.api.support.registry.PlatformRegistry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

class ExternalStorageTrackedStorageRepositoryProvider implements TrackedStorageRepositoryProvider {
    private static final String TAG_TYPE = "type";
    private static final String TAG_ITEMS = "items";

    private final PlatformRegistry<PlatformStorageChannelType> storageChannelTypeRegistry;
    private final Map<PlatformStorageChannelType, ExternalStorageTrackedStorageRepository> repositoryMap
        = new HashMap<>();

    ExternalStorageTrackedStorageRepositoryProvider(
        final PlatformRegistry<PlatformStorageChannelType> storageChannelTypeRegistry,
        final Runnable listener
    ) {
        this.storageChannelTypeRegistry = storageChannelTypeRegistry;
        storageChannelTypeRegistry.getAll().forEach(type -> repositoryMap.put(
            type,
            new ExternalStorageTrackedStorageRepository(listener, type)
        ));
    }

    ListTag toTag() {
        final ListTag items = new ListTag();
        repositoryMap.forEach((type, repo) -> storageChannelTypeRegistry.getId(type)
            .ifPresent(id -> items.add(toTag(repo, id))));
        return items;
    }

    private CompoundTag toTag(final ExternalStorageTrackedStorageRepository repo, final ResourceLocation id) {
        final CompoundTag tag = new CompoundTag();
        tag.putString(TAG_TYPE, id.toString());
        tag.put(TAG_ITEMS, repo.toTag());
        return tag;
    }

    void fromTag(final ListTag tag) {
        tag.forEach(item -> {
            final String id = ((CompoundTag) item).getString(TAG_TYPE);
            storageChannelTypeRegistry.get(new ResourceLocation(id)).ifPresent(
                type -> repositoryMap.get(type).fromTag(((CompoundTag) item).getList(TAG_ITEMS, Tag.TAG_COMPOUND))
            );
        });
    }

    @Override
    public TrackedStorageRepository getRepository(final StorageChannelType type) {
        return repositoryMap.get(type);
    }
}
