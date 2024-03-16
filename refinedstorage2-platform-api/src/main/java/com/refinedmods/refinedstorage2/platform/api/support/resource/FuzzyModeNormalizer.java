package com.refinedmods.refinedstorage2.platform.api.support.resource;

import com.refinedmods.refinedstorage2.api.resource.ResourceKey;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.1.3")
public interface FuzzyModeNormalizer {
    ResourceKey normalize();
}
