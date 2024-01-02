package com.refinedmods.refinedstorage2.api.network.impl.component;

import com.refinedmods.refinedstorage2.api.network.component.StorageNetworkComponent;
import com.refinedmods.refinedstorage2.api.network.component.StorageProvider;
import com.refinedmods.refinedstorage2.api.network.node.container.NetworkNodeContainer;
import com.refinedmods.refinedstorage2.api.storage.Actor;
import com.refinedmods.refinedstorage2.api.storage.Storage;
import com.refinedmods.refinedstorage2.api.storage.TrackedResourceAmount;
import com.refinedmods.refinedstorage2.api.storage.channel.StorageChannel;
import com.refinedmods.refinedstorage2.api.storage.channel.StorageChannelType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageNetworkComponentImpl implements StorageNetworkComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageNetworkComponentImpl.class);

    private final Map<StorageChannelType<?>, StorageChannel<?>> channels;

    public StorageNetworkComponentImpl(final Collection<? extends StorageChannelType<?>> storageChannelTypes) {
        this.channels = storageChannelTypes.stream().collect(Collectors.toUnmodifiableMap(
            type -> type,
            StorageChannelType::create
        ));
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void onContainerAdded(final NetworkNodeContainer container) {
        if (container.getNode() instanceof StorageProvider provider) {
            for (final Map.Entry<StorageChannelType<?>, StorageChannel<?>> entry : channels.entrySet()) {
                tryAddStorageFromProviderToChannel(provider, (StorageChannelType) entry.getKey(), entry.getValue());
            }
        }
    }

    private <T> void tryAddStorageFromProviderToChannel(final StorageProvider provider,
                                                        final StorageChannelType<T> type,
                                                        final StorageChannel<T> channel) {
        provider.getStorageForChannel(type).ifPresent(storage -> {
            LOGGER.debug("Adding source {} to channel {} from provider {}", storage, type, provider);
            channel.addSource(storage);
        });
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void onContainerRemoved(final NetworkNodeContainer container) {
        if (container.getNode() instanceof StorageProvider provider) {
            for (final Map.Entry<StorageChannelType<?>, StorageChannel<?>> entry : channels.entrySet()) {
                final StorageChannelType storageChannelType = entry.getKey();
                final StorageChannel<?> storageChannel = entry.getValue();
                tryRemoveStorageFromProviderFromChannel(provider, storageChannelType, storageChannel);
            }
        }
    }

    private <T> void tryRemoveStorageFromProviderFromChannel(final StorageProvider provider,
                                                             final StorageChannelType<T> type,
                                                             final StorageChannel<T> channel) {
        provider.getStorageForChannel(type).ifPresent(storage -> {
            LOGGER.debug("Removing source {} from channel {} of provider {}", storage, type, provider);
            channel.removeSource(storage);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> StorageChannel<T> getStorageChannel(final StorageChannelType<T> type) {
        return (StorageChannel<T>) channels.get(type);
    }

    @Override
    public Set<StorageChannelType<?>> getStorageChannelTypes() {
        return channels.keySet();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean hasSource(final Predicate<Storage<?>> matcher) {
        for (final Map.Entry<StorageChannelType<?>, StorageChannel<?>> entry : channels.entrySet()) {
            final StorageChannel storageChannel = entry.getValue();
            if (storageChannel.hasSource(matcher)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> List<TrackedResourceAmount<T>> getResources(final StorageChannelType<T> type,
                                                           final Class<? extends Actor> actorType) {
        final StorageChannel<T> storageChannel = getStorageChannel(type);
        return storageChannel.getAll().stream().map(resourceAmount -> new TrackedResourceAmount<>(
            resourceAmount,
            storageChannel.findTrackedResourceByActorType(resourceAmount.getResource(), actorType).orElse(null)
        )).toList();
    }
}
