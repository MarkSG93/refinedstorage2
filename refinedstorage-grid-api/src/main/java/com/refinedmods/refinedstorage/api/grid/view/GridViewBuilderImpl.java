package com.refinedmods.refinedstorage.api.grid.view;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.resource.list.ResourceList;
import com.refinedmods.refinedstorage.api.resource.list.ResourceListImpl;
import com.refinedmods.refinedstorage.api.storage.tracked.TrackedResource;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.2.4")
public class GridViewBuilderImpl implements GridViewBuilder {
    private final GridResourceFactory resourceFactory;
    private final ResourceList backingList = new ResourceListImpl();
    private final Map<ResourceKey, TrackedResource> trackedResources = new HashMap<>();
    private final GridSortingType identitySortingType;
    private final GridSortingType defaultSortingType;

    public GridViewBuilderImpl(final GridResourceFactory resourceFactory,
                               final GridSortingType identitySortingType,
                               final GridSortingType defaultSortingType) {
        this.resourceFactory = resourceFactory;
        this.identitySortingType = identitySortingType;
        this.defaultSortingType = defaultSortingType;
    }

    @Override
    public GridViewBuilder withResource(final ResourceKey resource,
                                        final long amount,
                                        @Nullable final TrackedResource trackedResource) {
        backingList.add(resource, amount);
        trackedResources.put(resource, trackedResource);
        return this;
    }

    @Override
    public GridView build() {
        return new GridViewImpl(
            resourceFactory,
            backingList,
            trackedResources,
            identitySortingType,
            defaultSortingType
        );
    }
}
