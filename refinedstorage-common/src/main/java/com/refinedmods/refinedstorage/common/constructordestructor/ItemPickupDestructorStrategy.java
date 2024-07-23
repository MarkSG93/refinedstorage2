package com.refinedmods.refinedstorage.common.constructordestructor;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.filter.Filter;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.channel.StorageChannel;
import com.refinedmods.refinedstorage.common.api.constructordestructor.DestructorStrategy;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

class ItemPickupDestructorStrategy implements DestructorStrategy {
    private final ServerLevel level;
    private final BlockPos pos;

    ItemPickupDestructorStrategy(final ServerLevel level, final BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    @Override
    public boolean apply(final Filter filter,
                         final Actor actor,
                         final Supplier<Network> networkSupplier,
                         final Player player) {
        if (!level.isLoaded(pos)) {
            return false;
        }
        final StorageChannel storageChannel = networkSupplier.get().getComponent(StorageNetworkComponent.class);
        final List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos));
        for (final ItemEntity itemEntity : items) {
            tryInsert(filter, actor, storageChannel, itemEntity);
        }
        return true;
    }

    private void tryInsert(final Filter filter,
                           final Actor actor,
                           final StorageChannel storageChannel,
                           final ItemEntity itemEntity) {
        if (itemEntity.isRemoved()) {
            return;
        }
        final ItemStack itemStack = itemEntity.getItem();
        final ItemResource itemResource = ItemResource.ofItemStack(itemStack);
        if (!filter.isAllowed(itemResource)) {
            return;
        }
        final int amount = itemStack.getCount();
        final long inserted = storageChannel.insert(itemResource, amount, Action.EXECUTE, actor);
        itemStack.shrink((int) inserted);
        if (itemStack.isEmpty()) {
            itemEntity.discard();
        }
    }
}
