package com.refinedmods.refinedstorage2.api.network.impl.storage;

import com.refinedmods.refinedstorage2.api.network.impl.node.AbstractNetworkNode;
import com.refinedmods.refinedstorage2.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage2.api.resource.ResourceKey;
import com.refinedmods.refinedstorage2.api.resource.filter.Filter;
import com.refinedmods.refinedstorage2.api.resource.filter.FilterMode;
import com.refinedmods.refinedstorage2.api.storage.AccessMode;

import java.util.Set;
import java.util.function.UnaryOperator;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.2.4")
public abstract class AbstractStorageNetworkNode extends AbstractNetworkNode implements StorageConfiguration {
    private final Filter filter = new Filter();
    private int priority;
    private AccessMode accessMode = AccessMode.INSERT_EXTRACT;
    private boolean voidExcess;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public AccessMode getAccessMode() {
        return accessMode;
    }

    @Override
    public void setAccessMode(final AccessMode accessMode) {
        this.accessMode = accessMode;
    }

    @Override
    public FilterMode getFilterMode() {
        return filter.getMode();
    }

    @Override
    public boolean isAllowed(final ResourceKey resource) {
        return filter.isAllowed(resource);
    }

    @Override
    public void setFilterMode(final FilterMode mode) {
        filter.setMode(mode);
    }

    @Override
    public void setPriority(final int priority) {
        this.priority = priority;
        trySortSources();
    }

    @Override
    public boolean isVoidExcess() {
        return voidExcess;
    }

    @Override
    public void setVoidExcess(final boolean voidExcess) {
        this.voidExcess = voidExcess;
    }

    private void trySortSources() {
        if (network == null) {
            return;
        }
        final StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
        storage.sortSources();
    }

    public void setFilters(final Set<ResourceKey> filters) {
        filter.setFilters(filters);
    }

    public void setNormalizer(final UnaryOperator<ResourceKey> normalizer) {
        filter.setNormalizer(normalizer);
    }
}
