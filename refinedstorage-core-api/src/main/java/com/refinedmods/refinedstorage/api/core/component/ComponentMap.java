package com.refinedmods.refinedstorage.api.core.component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.1.4")
public class ComponentMap<C> implements ComponentAccessor<C> {
    private final Map<Class<? extends C>, C> map;

    public ComponentMap(final Map<Class<? extends C>, C> map) {
        this.map = Collections.unmodifiableMap(map);
    }

    public Collection<C> getComponents() {
        return map.values();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends C> I getComponent(final Class<I> componentType) {
        if (!map.containsKey(componentType)) {
            throw new IllegalArgumentException("Component not present: " + componentType);
        }
        return (I) map.get(componentType);
    }
}
