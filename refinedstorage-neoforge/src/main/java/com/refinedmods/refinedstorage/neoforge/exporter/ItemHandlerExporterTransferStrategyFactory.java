package com.refinedmods.refinedstorage.neoforge.exporter;

import com.refinedmods.refinedstorage.api.network.impl.node.exporter.ExporterTransferStrategyImpl;
import com.refinedmods.refinedstorage.api.network.node.exporter.ExporterTransferStrategy;
import com.refinedmods.refinedstorage.common.api.exporter.AmountOverride;
import com.refinedmods.refinedstorage.common.api.exporter.ExporterTransferStrategyFactory;
import com.refinedmods.refinedstorage.common.api.upgrade.UpgradeState;
import com.refinedmods.refinedstorage.common.content.Items;
import com.refinedmods.refinedstorage.common.exporter.FuzzyExporterTransferStrategy;
import com.refinedmods.refinedstorage.neoforge.storage.CapabilityCacheImpl;
import com.refinedmods.refinedstorage.neoforge.storage.ItemHandlerInsertableStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class ItemHandlerExporterTransferStrategyFactory implements ExporterTransferStrategyFactory {
    @Override
    public ExporterTransferStrategy create(final ServerLevel level,
                                           final BlockPos pos,
                                           final Direction direction,
                                           final UpgradeState upgradeState,
                                           final AmountOverride amountOverride,
                                           final boolean fuzzyMode) {
        final CapabilityCacheImpl coordinates = new CapabilityCacheImpl(level, pos, direction);
        final ItemHandlerInsertableStorage destination = new ItemHandlerInsertableStorage(coordinates, amountOverride);
        final int transferQuota = upgradeState.has(Items.INSTANCE.getStackUpgrade()) ? 64 : 1;
        if (fuzzyMode) {
            return new FuzzyExporterTransferStrategy(destination, transferQuota);
        }
        return new ExporterTransferStrategyImpl(destination, transferQuota);
    }
}
