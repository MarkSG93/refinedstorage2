package com.refinedmods.refinedstorage.platform.api.grid.view;

import com.refinedmods.refinedstorage.api.grid.view.GridResourceAttributeKey;
import com.refinedmods.refinedstorage.api.grid.view.GridView;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.storage.tracked.TrackedResource;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.3.0")
public abstract class AbstractPlatformGridResource implements PlatformGridResource {
    protected final ResourceAmount resourceAmount;
    private final String name;
    private final Map<GridResourceAttributeKey, Set<String>> attributes;
    private boolean zeroed;

    protected AbstractPlatformGridResource(final ResourceAmount resourceAmount,
                                           final String name,
                                           final Map<GridResourceAttributeKey, Set<String>> attributes) {
        this.resourceAmount = resourceAmount;
        this.name = name;
        this.attributes = attributes;
    }

    @Override
    public Optional<TrackedResource> getTrackedResource(final GridView view) {
        return view.getTrackedResource(resourceAmount.getResource());
    }

    @Override
    public long getAmount() {
        return resourceAmount.getAmount();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getAttribute(final GridResourceAttributeKey key) {
        return attributes.getOrDefault(key, Collections.emptySet());
    }

    @Override
    public boolean isZeroed() {
        return zeroed;
    }

    @Override
    public void setZeroed(final boolean zeroed) {
        this.zeroed = zeroed;
    }

    @Override
    public String toString() {
        return "AbstractPlatformGridResource{"
            + "resourceAmount=" + resourceAmount
            + ", name='" + name + '\''
            + ", attributes=" + attributes
            + ", zeroed=" + zeroed
            + '}';
    }
}
