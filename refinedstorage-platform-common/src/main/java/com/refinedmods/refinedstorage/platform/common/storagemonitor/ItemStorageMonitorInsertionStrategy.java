package com.refinedmods.refinedstorage.platform.common.storagemonitor;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.platform.api.storagemonitor.StorageMonitorInsertionStrategy;
import com.refinedmods.refinedstorage.platform.common.support.resource.ItemResource;

import java.util.Optional;

import net.minecraft.world.item.ItemStack;

public class ItemStorageMonitorInsertionStrategy implements StorageMonitorInsertionStrategy {
    @Override
    public Optional<ItemStack> insert(final ResourceKey configuredResource,
                                      final ItemStack stack,
                                      final Actor actor,
                                      final Network network) {
        if (!(configuredResource instanceof ItemResource configuredItemResource)) {
            return Optional.empty();
        }
        final ItemResource resource = ItemResource.ofItemStack(stack);
        if (!configuredItemResource.equals(resource)) {
            return Optional.empty();
        }
        final long inserted = network.getComponent(StorageNetworkComponent.class).insert(
            resource,
            stack.getCount(),
            Action.EXECUTE,
            actor
        );
        final long remainder = stack.getCount() - inserted;
        if (remainder > 0) {
            return Optional.of(resource.toItemStack(remainder));
        }
        return Optional.of(ItemStack.EMPTY);
    }
}
