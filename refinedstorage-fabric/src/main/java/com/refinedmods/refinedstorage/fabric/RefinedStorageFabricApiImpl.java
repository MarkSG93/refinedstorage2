package com.refinedmods.refinedstorage.fabric;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.support.network.NetworkNodeContainerProvider;
import com.refinedmods.refinedstorage.fabric.api.FabricStorageExternalPatternInputSinkStrategyFactory;
import com.refinedmods.refinedstorage.fabric.api.RefinedStorageFabricApi;
import com.refinedmods.refinedstorage.fabric.autocrafting.FabricStoragePatternProviderExternalPatternInputSinkFactory;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.Direction;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;

public class RefinedStorageFabricApiImpl implements RefinedStorageFabricApi {
    private final BlockApiLookup<NetworkNodeContainerProvider, @NullableType Direction> networkNodeContainerProvider =
        BlockApiLookup.get(
            createIdentifier("network_node_container_provider"),
            NetworkNodeContainerProvider.class,
            Direction.class
        );
    private final FabricStoragePatternProviderExternalPatternInputSinkFactory
        storagePatternProviderExternalPatternInputSinkFactory =
        new FabricStoragePatternProviderExternalPatternInputSinkFactory();

    public RefinedStorageFabricApiImpl(final RefinedStorageApi refinedStorageApi) {
        refinedStorageApi.addPatternProviderExternalPatternInputSinkFactory(
            storagePatternProviderExternalPatternInputSinkFactory
        );
    }

    @Override
    public BlockApiLookup<NetworkNodeContainerProvider, Direction> getNetworkNodeContainerProviderLookup() {
        return networkNodeContainerProvider;
    }

    @Override
    public void addStorageExternalPatternInputSinkStrategyFactory(
        final FabricStorageExternalPatternInputSinkStrategyFactory factory) {
        storagePatternProviderExternalPatternInputSinkFactory.addFactory(factory);
    }
}
