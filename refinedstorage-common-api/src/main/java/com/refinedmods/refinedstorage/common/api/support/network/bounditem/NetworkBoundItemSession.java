package com.refinedmods.refinedstorage.common.api.support.network.bounditem;

import com.refinedmods.refinedstorage.api.network.Network;

import java.util.Optional;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.3.1")
public interface NetworkBoundItemSession {
    Optional<Network> resolveNetwork();

    boolean isActive();

    void drainEnergy(long amount);
}
