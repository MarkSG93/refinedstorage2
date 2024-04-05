package com.refinedmods.refinedstorage2.platform.common.storage.storageblock;

import com.refinedmods.refinedstorage2.api.network.impl.node.storage.StorageNetworkNode;
import com.refinedmods.refinedstorage2.api.storage.Storage;
import com.refinedmods.refinedstorage2.platform.api.PlatformApi;
import com.refinedmods.refinedstorage2.platform.api.storage.ItemTransferableStorageBlockEntity;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageRepository;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ResourceFactory;
import com.refinedmods.refinedstorage2.platform.common.storage.StorageConfigurationContainerImpl;
import com.refinedmods.refinedstorage2.platform.common.support.FilterWithFuzzyMode;
import com.refinedmods.refinedstorage2.platform.common.support.containermenu.NetworkNodeMenuProvider;
import com.refinedmods.refinedstorage2.platform.common.support.network.AbstractRedstoneModeNetworkNodeContainerBlockEntity;
import com.refinedmods.refinedstorage2.platform.common.support.resource.ResourceContainerImpl;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractStorageBlockBlockEntity
    extends AbstractRedstoneModeNetworkNodeContainerBlockEntity<StorageNetworkNode>
    implements NetworkNodeMenuProvider, ItemTransferableStorageBlockEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStorageBlockBlockEntity.class);

    private static final String TAG_STORAGE_ID = "sid";

    protected final StorageConfigurationContainerImpl configContainer;
    private final FilterWithFuzzyMode filter;

    @Nullable
    private UUID storageId;

    protected AbstractStorageBlockBlockEntity(final BlockEntityType<?> type,
                                              final BlockPos pos,
                                              final BlockState state,
                                              final StorageNetworkNode node,
                                              final ResourceFactory resourceFactory) {
        super(type, pos, state, node);
        this.filter = FilterWithFuzzyMode.createAndListenForUniqueFilters(
            ResourceContainerImpl.createForFilter(resourceFactory),
            this::setChanged,
            mainNode::setFilters
        );
        this.configContainer = new StorageConfigurationContainerImpl(
            mainNode,
            filter,
            this::setChanged,
            this::getRedstoneMode,
            this::setRedstoneMode
        );
        mainNode.setNormalizer(filter.createNormalizer());
    }

    protected abstract Storage createStorage(Runnable listener);

    @Override
    public void setLevel(final Level level) {
        super.setLevel(level);
        if (level.isClientSide()) {
            return;
        }
        final StorageRepository storageRepository = PlatformApi.INSTANCE.getStorageRepository(level);
        if (storageId == null) {
            // We are a new block entity, or:
            // - We are placed through NBT
            //   (#setLevel(Level) -> #load(CompoundTag)),
            // - We are placed with an existing storage ID
            //   (#setLevel(Level) -> #modifyStorageAfterAlreadyInitialized(UUID)).
            // In both cases listed above we need to clean up the storage we create here.
            storageId = UUID.randomUUID();
            final Storage storage = createStorage(storageRepository::markAsChanged);
            storageRepository.set(storageId, storage);
            mainNode.setStorage(storage);
        } else {
            // The existing block entity got loaded in the level (#load(CompoundTag) -> #setLevel(Level)).
            storageRepository.get(storageId).ifPresentOrElse(
                mainNode::setStorage,
                () -> LOGGER.warn("Storage {} could not be resolved", storageId)
            );
        }
    }

    @Override
    public void modifyStorageIdAfterAlreadyInitialized(final UUID actualStorageId) {
        LOGGER.debug(
            "Storage {} got placed through NBT, replacing with actual storage {}",
            storageId,
            actualStorageId
        );
        cleanupUnneededInitialStorageAndReinitialize(actualStorageId);
        this.storageId = actualStorageId;
    }

    @Override
    public void load(final CompoundTag tag) {
        if (tag.contains(TAG_STORAGE_ID)) {
            final UUID actualStorageId = tag.getUUID(TAG_STORAGE_ID);
            if (isPlacedThroughNbtPlacement(actualStorageId)) {
                LOGGER.debug(
                    "Storage {} got placed through nbt, replacing with actual storage {}",
                    storageId,
                    actualStorageId
                );
                cleanupUnneededInitialStorageAndReinitialize(actualStorageId);
            }
            storageId = actualStorageId;
        }
        super.load(tag);
    }

    @Override
    public void readConfiguration(final CompoundTag tag) {
        super.readConfiguration(tag);
        configContainer.load(tag);
        filter.load(tag);
    }

    private void cleanupUnneededInitialStorageAndReinitialize(final UUID actualStorageId) {
        // We got placed through NBT (#setLevel(Level) -> #load(CompoundTag)), or,
        // we got placed with an existing storage ID (#setLevel(Level) -> modifyStorageAfterAlreadyInitialized(UUID)).
        // Clean up the storage created earlier in #setLevel(Level).
        final StorageRepository storageRepository = PlatformApi.INSTANCE
            .getStorageRepository(Objects.requireNonNull(level));
        storageRepository.removeIfEmpty(Objects.requireNonNull(storageId)).ifPresentOrElse(
            storage -> LOGGER.debug("Unneeded storage {} successfully removed", storageId),
            () -> LOGGER.warn("Unneeded storage {} could not be removed", storageId)
        );
        storageRepository.get(actualStorageId).ifPresentOrElse(
            mainNode::setStorage,
            () -> LOGGER.warn("Actual storage ID {} could not be resolved!", actualStorageId)
        );
    }

    private boolean isPlacedThroughNbtPlacement(final UUID otherStorageId) {
        // When placed through nbt, the level is already set and a default new storage was created.
        return level != null && storageId != null && !storageId.equals(otherStorageId);
    }

    @Override
    public void saveAdditional(final CompoundTag tag) {
        super.saveAdditional(tag);
        if (storageId != null) {
            tag.putUUID(TAG_STORAGE_ID, storageId);
        }
    }

    @Override
    public void writeConfiguration(final CompoundTag tag) {
        super.writeConfiguration(tag);
        configContainer.save(tag);
        filter.save(tag);
    }

    @Override
    @Nullable
    public UUID getStorageId() {
        return storageId;
    }

    protected final ResourceContainer getFilterContainer() {
        return filter.getFilterContainer();
    }

    @Override
    public void writeScreenOpeningData(final ServerPlayer player, final FriendlyByteBuf buf) {
        buf.writeLong(mainNode.getStored());
        buf.writeLong(mainNode.getCapacity());
        filter.getFilterContainer().writeToUpdatePacket(buf);
    }
}
