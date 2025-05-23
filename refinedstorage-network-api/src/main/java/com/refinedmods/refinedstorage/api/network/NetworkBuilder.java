package com.refinedmods.refinedstorage.api.network;

import com.refinedmods.refinedstorage.api.network.node.container.NetworkNodeContainer;

import org.apiguardian.api.API;

/**
 * Manages the network state of a {@link com.refinedmods.refinedstorage.api.network.node.NetworkNode}.
 * Performs merge, split and remove operations as necessary.
 */
@API(status = API.Status.STABLE, since = "2.0.0-milestone.1.0")
public interface NetworkBuilder {
    /**
     * Initializes a not yet connected network node container.
     * If the network node already has an associated {@link Network}, calling this will do nothing.
     * This will perform a merge operation.
     *
     * @param container          the container
     * @param connectionProvider the connection provider
     * @return true if the container has no network yet, and the initialization succeeded, false otherwise
     */
    boolean initialize(NetworkNodeContainer container, ConnectionProvider connectionProvider);

    /**
     * Removes a container from its network.
     * Will remove the network if the container is the last container in the network, or otherwise it performs
     * a split operation.
     *
     * @param container          the container
     * @param connectionProvider the connection provider
     */
    void remove(NetworkNodeContainer container, ConnectionProvider connectionProvider);

    /**
     * Updates the network associated with the given container. Makes the network state adapt to connection changes
     * of an already connected container.
     * Will perform a split and/or a merge operation.
     *
     * @param container          the container
     * @param connectionProvider the connection provider
     */
    void update(NetworkNodeContainer container, ConnectionProvider connectionProvider);
}
