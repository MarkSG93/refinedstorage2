package com.refinedmods.refinedstorage.common.constructordestructor;

import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.impl.node.SimpleNetworkNode;
import com.refinedmods.refinedstorage.api.network.node.NetworkNodeActor;
import com.refinedmods.refinedstorage.api.network.node.task.Task;
import com.refinedmods.refinedstorage.api.network.node.task.TaskExecutor;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.constructordestructor.ConstructorStrategy;
import com.refinedmods.refinedstorage.common.api.constructordestructor.ConstructorStrategyFactory;
import com.refinedmods.refinedstorage.common.content.BlockEntities;
import com.refinedmods.refinedstorage.common.content.ContentNames;
import com.refinedmods.refinedstorage.common.support.AbstractDirectionalBlock;
import com.refinedmods.refinedstorage.common.support.network.AbstractSchedulingNetworkNodeContainerBlockEntity;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeDestinations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ConstructorBlockEntity
    extends AbstractSchedulingNetworkNodeContainerBlockEntity<SimpleNetworkNode, ConstructorBlockEntity.TaskContext> {
    private static final String TAG_DROP_ITEMS = "di";

    private final Actor actor;
    private final List<TaskImpl> tasks = new ArrayList<>();

    @Nullable
    private TaskExecutor<TaskContext> taskExecutor;
    @Nullable
    private ConstructorStrategy strategy;
    private boolean dropItems;

    public ConstructorBlockEntity(final BlockPos pos, final BlockState state) {
        super(
            BlockEntities.INSTANCE.getConstructor(),
            pos,
            state,
            new SimpleNetworkNode(Platform.INSTANCE.getConfig().getConstructor().getEnergyUsage()),
            UpgradeDestinations.CONSTRUCTOR
        );
        this.actor = new NetworkNodeActor(mainNetworkNode);
    }

    @Override
    protected void setFilters(final List<ResourceKey> filters) {
        this.tasks.clear();
        this.tasks.addAll(filters.stream().map(TaskImpl::new).toList());
    }

    @Override
    protected void initialize(final ServerLevel level, final Direction direction) {
        this.strategy = createStrategy(level, direction);
    }

    private ConstructorStrategy createStrategy(final ServerLevel serverLevel, final Direction direction) {
        final Direction incomingDirection = direction.getOpposite();
        final BlockPos sourcePosition = worldPosition.relative(direction);
        final Collection<ConstructorStrategyFactory> factories = RefinedStorageApi.INSTANCE
            .getConstructorStrategyFactories();
        final List<ConstructorStrategy> strategies = factories
            .stream()
            .flatMap(factory -> factory.create(
                serverLevel,
                sourcePosition,
                incomingDirection,
                upgradeContainer,
                dropItems
            ).stream())
            .toList();
        return new CompositeConstructorStrategy(strategies);
    }

    @Override
    public void postDoWork() {
        if (taskExecutor == null
            || mainNetworkNode.getNetwork() == null
            || !mainNetworkNode.isActive()
            || !(level instanceof ServerLevel serverLevel)) {
            return;
        }
        final Player fakePlayer = getFakePlayer(serverLevel);
        taskExecutor.execute(tasks, new TaskContext(
            mainNetworkNode.getNetwork(),
            fakePlayer
        ));
    }

    @Override
    public void writeConfiguration(final CompoundTag tag, final HolderLookup.Provider provider) {
        super.writeConfiguration(tag, provider);
        tag.putBoolean(TAG_DROP_ITEMS, dropItems);
    }

    @Override
    public void readConfiguration(final CompoundTag tag, final HolderLookup.Provider provider) {
        super.readConfiguration(tag, provider);
        if (tag.contains(TAG_DROP_ITEMS)) {
            dropItems = tag.getBoolean(TAG_DROP_ITEMS);
        }
    }

    boolean isDropItems() {
        return dropItems;
    }

    void setDropItems(final boolean dropItems) {
        this.dropItems = dropItems;
        setChanged();
        if (level instanceof ServerLevel serverLevel) {
            initialize(serverLevel);
        }
    }

    @Override
    public Component getDisplayName() {
        return getName(ContentNames.CONSTRUCTOR);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(final int syncId, final Inventory inventory, final Player player) {
        return new ConstructorContainerMenu(syncId, player, this, filter.getFilterContainer(), upgradeContainer);
    }

    @Override
    protected void setEnergyUsage(final long upgradeEnergyUsage) {
        final long baseEnergyUsage = Platform.INSTANCE.getConfig().getConstructor().getEnergyUsage();
        mainNetworkNode.setEnergyUsage(baseEnergyUsage + upgradeEnergyUsage);
    }

    @Override
    protected void setTaskExecutor(final TaskExecutor<TaskContext> taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    protected boolean doesBlockStateChangeWarrantNetworkNodeUpdate(final BlockState oldBlockState,
                                                                   final BlockState newBlockState) {
        return AbstractDirectionalBlock.doesBlockStateChangeWarrantNetworkNodeUpdate(oldBlockState, newBlockState);
    }

    protected record TaskContext(Network network, Player player) {
    }

    private class TaskImpl implements Task<TaskContext> {
        private final ResourceKey filter;

        private TaskImpl(final ResourceKey filter) {
            this.filter = filter;
        }

        @Override
        public boolean run(final TaskContext context) {
            if (strategy == null) {
                return false;
            }
            strategy.apply(filter, actor, context.player, context.network);
            return true;
        }
    }
}
