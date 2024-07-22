package com.refinedmods.refinedstorage.common.api.constructordestructor;

import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.resource.filter.Filter;
import com.refinedmods.refinedstorage.api.storage.Actor;

import java.util.function.Supplier;

import net.minecraft.world.entity.player.Player;
import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.2.10")
@FunctionalInterface
public interface DestructorStrategy {
    /**
     * Tries to perform the destructor operation.
     *
     * @param filter          the filter
     * @param actor           the actor
     * @param networkSupplier the network, this network can change due to a destruction operation hence the supplier
     * @param actingPlayer    the fake player
     * @return true if the operation was successful and lower prioritized strategies should be ignored, false otherwise
     */
    boolean apply(Filter filter, Actor actor, Supplier<Network> networkSupplier, Player actingPlayer);
}
