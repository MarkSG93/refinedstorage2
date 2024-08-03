package com.refinedmods.refinedstorage.api.storage;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.resource.list.ResourceList;
import com.refinedmods.refinedstorage.api.resource.list.ResourceListImpl;

import java.util.Collection;

import org.apiguardian.api.API;

/**
 * An implementation of a {@link Storage} which has an in-memory resource list as a backing list.
 */
@API(status = API.Status.STABLE, since = "2.0.0-milestone.1.0")
public class InMemoryStorageImpl implements Storage {
    private final ResourceList list;
    private long stored;

    public InMemoryStorageImpl(final ResourceList list) {
        this.list = list;
    }

    public InMemoryStorageImpl() {
        this(ResourceListImpl.create());
    }

    @Override
    public long extract(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        ResourceAmount.validate(resource, amount);
        final long availableAmount = list.get(resource);
        if (availableAmount == 0) {
            return 0;
        }
        final long maxExtract = Math.min(availableAmount, amount);
        return doExtract(resource, maxExtract, action);
    }

    private long doExtract(final ResourceKey resource, final long amount, final Action action) {
        if (action == Action.EXECUTE) {
            list.remove(resource, amount);
            stored -= amount;
        }
        return amount;
    }

    @Override
    public long insert(final ResourceKey resource, final long amount, final Action action, final Actor actor) {
        ResourceAmount.validate(resource, amount);
        if (action == Action.EXECUTE) {
            stored += amount;
            list.add(resource, amount);
        }
        return amount;
    }

    @Override
    public Collection<ResourceAmount> getAll() {
        return list.copyState();
    }

    @Override
    public long getStored() {
        return stored;
    }
}
