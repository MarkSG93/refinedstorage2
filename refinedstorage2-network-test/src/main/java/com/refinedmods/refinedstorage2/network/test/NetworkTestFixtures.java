package com.refinedmods.refinedstorage2.network.test;

import com.refinedmods.refinedstorage2.api.core.component.ComponentMapFactory;
import com.refinedmods.refinedstorage2.api.network.Network;
import com.refinedmods.refinedstorage2.api.network.NetworkComponent;
import com.refinedmods.refinedstorage2.api.network.energy.EnergyNetworkComponent;
import com.refinedmods.refinedstorage2.api.network.impl.energy.EnergyNetworkComponentImpl;
import com.refinedmods.refinedstorage2.api.network.impl.node.GraphNetworkComponentImpl;
import com.refinedmods.refinedstorage2.api.network.impl.security.SecurityNetworkComponentImpl;
import com.refinedmods.refinedstorage2.api.network.impl.storage.StorageNetworkComponentImpl;
import com.refinedmods.refinedstorage2.api.network.node.GraphNetworkComponent;
import com.refinedmods.refinedstorage2.api.network.security.SecurityNetworkComponent;
import com.refinedmods.refinedstorage2.api.network.security.SecurityPolicy;
import com.refinedmods.refinedstorage2.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage2.api.resource.list.ResourceListImpl;
import com.refinedmods.refinedstorage2.network.test.fake.FakePermissions;

public final class NetworkTestFixtures {
    public static final ComponentMapFactory<NetworkComponent, Network> NETWORK_COMPONENT_MAP_FACTORY =
        new ComponentMapFactory<>();

    static {
        NETWORK_COMPONENT_MAP_FACTORY.addFactory(
            EnergyNetworkComponent.class,
            network -> new EnergyNetworkComponentImpl()
        );
        NETWORK_COMPONENT_MAP_FACTORY.addFactory(
            GraphNetworkComponent.class,
            GraphNetworkComponentImpl::new
        );
        NETWORK_COMPONENT_MAP_FACTORY.addFactory(
            StorageNetworkComponent.class,
            network -> new StorageNetworkComponentImpl(new ResourceListImpl())
        );
        NETWORK_COMPONENT_MAP_FACTORY.addFactory(
            SecurityNetworkComponent.class,
            network -> new SecurityNetworkComponentImpl(SecurityPolicy.of(FakePermissions.ALLOW_BY_DEFAULT))
        );
    }

    private NetworkTestFixtures() {
    }
}
