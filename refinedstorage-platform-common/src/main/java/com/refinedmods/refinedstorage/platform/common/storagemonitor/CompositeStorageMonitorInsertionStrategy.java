package com.refinedmods.refinedstorage.platform.common.storagemonitor;

import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.platform.api.storagemonitor.StorageMonitorInsertionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.world.item.ItemStack;

public class CompositeStorageMonitorInsertionStrategy implements StorageMonitorInsertionStrategy {
    private final List<StorageMonitorInsertionStrategy> strategies = new ArrayList<>();

    public void addStrategy(final StorageMonitorInsertionStrategy strategy) {
        strategies.add(strategy);
    }

    @Override
    public Optional<ItemStack> insert(
        final ResourceKey configuredResource,
        final ItemStack stack,
        final Actor actor,
        final Network network
    ) {
        return strategies.stream()
            .flatMap(strategy -> strategy.insert(configuredResource, stack, actor, network).stream())
            .findFirst();
    }
}
