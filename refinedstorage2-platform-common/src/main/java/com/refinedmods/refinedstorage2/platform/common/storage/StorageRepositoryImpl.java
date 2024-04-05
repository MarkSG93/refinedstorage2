package com.refinedmods.refinedstorage2.platform.common.storage;

import com.refinedmods.refinedstorage2.api.core.CoreValidations;
import com.refinedmods.refinedstorage2.api.storage.Storage;
import com.refinedmods.refinedstorage2.platform.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageInfo;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageRepository;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageType;
import com.refinedmods.refinedstorage2.platform.api.support.registry.PlatformRegistry;
import com.refinedmods.refinedstorage2.platform.common.support.AbstractSafeSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageRepositoryImpl extends AbstractSafeSavedData implements StorageRepository {
    public static final String NAME = "refinedstorage2_storages";

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageRepositoryImpl.class);

    private static final String TAG_STORAGES = "storages";
    private static final String TAG_STORAGE_ID = "id";
    private static final String TAG_STORAGE_TYPE = "type";
    private static final String TAG_STORAGE_DATA = "data";

    private final Map<UUID, Storage> entries = new HashMap<>();
    private final PlatformRegistry<StorageType> storageTypeRegistry;

    public StorageRepositoryImpl(final PlatformRegistry<StorageType> storageTypeRegistry) {
        this.storageTypeRegistry = storageTypeRegistry;
    }

    @Override
    public Optional<Storage> get(final UUID id) {
        return Optional.ofNullable(entries.get(id));
    }

    @Override
    public void set(final UUID id, final Storage storage) {
        setSilently(id, storage);
        setDirty();
    }

    private void setSilently(final UUID id, final Storage storage) {
        CoreValidations.validateNotNull(storage, "Storage must not be null");
        if (!(storage instanceof SerializableStorage)) {
            throw new IllegalArgumentException("Storage is not serializable");
        }
        CoreValidations.validateNotNull(id, "ID must not be null");
        if (entries.containsKey(id)) {
            throw new IllegalArgumentException(id + " already exists");
        }
        entries.put(id, storage);
    }

    @Override
    public Optional<Storage> removeIfEmpty(final UUID id) {
        return get(id).map(storage -> {
            if (storage.getStored() == 0) {
                entries.remove(id);
                setDirty();
                return storage;
            }
            return null;
        });
    }

    @Override
    public StorageInfo getInfo(final UUID id) {
        return get(id).map(StorageInfo::of).orElse(StorageInfo.UNKNOWN);
    }

    @Override
    public void markAsChanged() {
        setDirty();
    }

    public void read(final CompoundTag tag) {
        final ListTag storages = tag.getList(TAG_STORAGES, Tag.TAG_COMPOUND);
        for (final Tag storageTag : storages) {
            final UUID id = ((CompoundTag) storageTag).getUUID(TAG_STORAGE_ID);
            final ResourceLocation typeId = new ResourceLocation(
                ((CompoundTag) storageTag).getString(TAG_STORAGE_TYPE)
            );
            final CompoundTag data = ((CompoundTag) storageTag).getCompound(TAG_STORAGE_DATA);

            storageTypeRegistry.get(typeId).ifPresentOrElse(
                type -> setSilently(id, type.fromTag(data, this::markAsChanged)),
                () -> LOGGER.warn("Cannot find storage type {} for storage {}", typeId, id)
            );
        }
    }

    @Override
    public CompoundTag save(final CompoundTag tag) {
        final ListTag storageList = new ListTag();
        for (final Map.Entry<UUID, Storage> entry : entries.entrySet()) {
            if (entry.getValue() instanceof SerializableStorage serializableStorage) {
                storageList.add(convertStorageToTag(entry.getKey(), entry.getValue(), serializableStorage));
            } else {
                LOGGER.warn("Tried to persist non-serializable storage {}", entry.getKey());
            }
        }
        tag.put(TAG_STORAGES, storageList);
        return tag;
    }

    private Tag convertStorageToTag(final UUID id,
                                    final Storage storage,
                                    final SerializableStorage serializableStorage) {
        final ResourceLocation typeIdentifier = storageTypeRegistry
            .getId(serializableStorage.getType())
            .orElseThrow(() -> new RuntimeException("Storage type is not registered"));

        final CompoundTag tag = new CompoundTag();
        tag.putUUID(TAG_STORAGE_ID, id);
        tag.put(TAG_STORAGE_DATA, serializableStorage.getType().toTag(storage));
        tag.putString(TAG_STORAGE_TYPE, typeIdentifier.toString());
        return tag;
    }
}
