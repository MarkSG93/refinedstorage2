package com.refinedmods.refinedstorage.api.network.impl.node.importer;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.ExtractableStorage;
import com.refinedmods.refinedstorage.api.storage.InsertableStorage;

import java.util.Iterator;

import org.apiguardian.api.API;

/**
 * Represents a source for the importer.
 * A valid source for the importer needs to be an {@link ExtractableStorage}, so the resources can be extracted
 * for insertion into the target network.
 * A valid source for the importer needs to be a {@link InsertableStorage} as well, so that transfers that end up
 * failing can be given back to the source.
 */
@API(status = API.Status.STABLE, since = "2.0.0-milestone.2.1")
public interface ImporterSource extends ExtractableStorage, InsertableStorage {
    /**
     * @return the resources that this source has
     */
    Iterator<ResourceKey> getResources();
}
