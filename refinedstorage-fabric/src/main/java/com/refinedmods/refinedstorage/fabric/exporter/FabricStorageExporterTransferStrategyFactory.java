package com.refinedmods.refinedstorage.fabric.exporter;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.api.network.impl.node.exporter.ExporterTransferStrategyImpl;
import com.refinedmods.refinedstorage.api.network.impl.node.exporter.MissingResourcesListeningExporterTransferStrategy;
import com.refinedmods.refinedstorage.api.network.node.exporter.ExporterTransferStrategy;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.api.exporter.ExporterTransferStrategyFactory;
import com.refinedmods.refinedstorage.common.api.storage.root.FuzzyRootStorage;
import com.refinedmods.refinedstorage.common.api.support.network.AmountOverride;
import com.refinedmods.refinedstorage.common.api.upgrade.UpgradeState;
import com.refinedmods.refinedstorage.common.content.Items;
import com.refinedmods.refinedstorage.fabric.storage.FabricStorageInsertableStorage;

import java.util.function.Function;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

import static com.refinedmods.refinedstorage.api.network.impl.node.exporter.MissingResourcesListeningExporterTransferStrategy.OnMissingResources.scheduleAutocrafting;

public class FabricStorageExporterTransferStrategyFactory<T> implements ExporterTransferStrategyFactory {
    private final Class<? extends ResourceKey> resourceType;
    private final BlockApiLookup<Storage<T>, Direction> lookup;
    private final Function<ResourceKey, @NullableType T> toPlatformMapper;
    private final long singleAmount;

    public FabricStorageExporterTransferStrategyFactory(final Class<? extends ResourceKey> resourceType,
                                                        final BlockApiLookup<Storage<T>, Direction> lookup,
                                                        final Function<ResourceKey, @NullableType T> toPlatformMapper,
                                                        final long singleAmount) {
        this.resourceType = resourceType;
        this.lookup = lookup;
        this.toPlatformMapper = toPlatformMapper;
        this.singleAmount = singleAmount;
    }

    @Override
    public Class<? extends ResourceKey> getResourceType() {
        return resourceType;
    }

    @Override
    public ExporterTransferStrategy create(final ServerLevel level,
                                           final BlockPos pos,
                                           final Direction direction,
                                           final UpgradeState upgradeState,
                                           final AmountOverride amountOverride,
                                           final boolean fuzzyMode) {
        final FabricStorageInsertableStorage<T> destination = new FabricStorageInsertableStorage<>(
            lookup,
            toPlatformMapper,
            level,
            pos,
            direction,
            amountOverride
        );
        final long transferQuota = upgradeState.has(Items.INSTANCE.getStackUpgrade())
            ? singleAmount * 64
            : singleAmount;
        final ExporterTransferStrategy strategy = create(fuzzyMode, destination, transferQuota);
        if (upgradeState.has(Items.INSTANCE.getAutocraftingUpgrade())) {
            return new MissingResourcesListeningExporterTransferStrategy(strategy,
                scheduleAutocrafting(resource -> transferQuota));
        }
        return strategy;
    }

    private ExporterTransferStrategy create(final boolean fuzzyMode,
                                            final FabricStorageInsertableStorage<T> destination,
                                            final long transferQuota) {
        if (fuzzyMode) {
            return new ExporterTransferStrategyImpl(destination, transferQuota, FuzzyRootStorage.expander());
        }
        return new ExporterTransferStrategyImpl(destination, transferQuota);
    }
}
