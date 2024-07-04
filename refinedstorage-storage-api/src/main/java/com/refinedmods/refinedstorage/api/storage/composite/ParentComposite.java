package com.refinedmods.refinedstorage.api.storage.composite;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Storage;

import org.apiguardian.api.API;

/**
 * Represents the parent storage that a {@link CompositeAwareChild} can use to propagate changes to.
 */
@API(status = API.Status.STABLE, since = "2.0.0-milestone.1.4")
public interface ParentComposite {
    /**
     * Called by the {@link CompositeAwareChild} to notify to this parent composite storage
     * that a source has been added.
     *
     * @param source the source
     */
    void onSourceAddedToChild(Storage source);

    /**
     * Called by the {@link CompositeAwareChild} to notify to this parent composite storage
     * that a source has been removed.
     *
     * @param source the source
     */
    void onSourceRemovedFromChild(Storage source);

    /**
     * Adds a resource to the composite storage cache.
     *
     * @param resource the resource
     * @param amount   the amount
     */
    void addToCache(ResourceKey resource, long amount);

    /**
     * Removes a resource from the composite storage cache.
     *
     * @param resource the resource
     * @param amount   the amount
     */
    void removeFromCache(ResourceKey resource, long amount);
}
