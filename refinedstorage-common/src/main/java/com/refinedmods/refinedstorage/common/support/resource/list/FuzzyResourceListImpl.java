package com.refinedmods.refinedstorage.common.support.resource.list;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.resource.list.AbstractProxyResourceList;
import com.refinedmods.refinedstorage.api.resource.list.ResourceList;
import com.refinedmods.refinedstorage.common.api.support.resource.FuzzyModeNormalizer;
import com.refinedmods.refinedstorage.common.api.support.resource.list.FuzzyResourceList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FuzzyResourceListImpl extends AbstractProxyResourceList implements FuzzyResourceList {
    private final Map<ResourceKey, Set<ResourceAmount>> normalizedFuzzyMap = new HashMap<>();

    public FuzzyResourceListImpl(final ResourceList delegate) {
        super(delegate);
    }

    @Override
    public OperationResult add(final ResourceKey resource, final long amount) {
        final OperationResult result = super.add(resource, amount);
        addToIndex(resource, result);
        return result;
    }

    private void addToIndex(final ResourceKey resource, final OperationResult result) {
        if (resource instanceof FuzzyModeNormalizer normalizer) {
            normalizedFuzzyMap.computeIfAbsent(normalizer.normalize(), k -> new HashSet<>())
                .add(result.resourceAmount());
        }
    }

    @Override
    public Optional<OperationResult> remove(final ResourceKey resource, final long amount) {
        return super.remove(resource, amount).map(result -> {
            if (!result.available()) {
                removeFromIndex(resource, result);
            }
            return result;
        });
    }

    private void removeFromIndex(final ResourceKey resource, final OperationResult result) {
        if (!(resource instanceof FuzzyModeNormalizer normalizer)) {
            return;
        }
        final ResourceKey normalized = normalizer.normalize();
        final Collection<ResourceAmount> index = normalizedFuzzyMap.get(normalized);
        if (index == null) {
            return;
        }
        index.remove(result.resourceAmount());
        if (index.isEmpty()) {
            normalizedFuzzyMap.remove(normalized);
        }
    }

    @Override
    public Collection<ResourceAmount> getFuzzy(final ResourceKey resource) {
        if (resource instanceof FuzzyModeNormalizer normalizer) {
            return normalizedFuzzyMap.getOrDefault(normalizer.normalize(), Collections.emptySet());
        }
        return normalizedFuzzyMap.getOrDefault(resource, Collections.emptySet());
    }
}
