package com.refinedmods.refinedstorage2.platform.api.recipemod;

import com.refinedmods.refinedstorage2.api.storage.ResourceTemplate;

import java.util.Optional;

import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.2.5")
public interface IngredientConverter {
    Optional<ResourceTemplate> convertToResource(Object ingredient);

    Optional<Object> convertToIngredient(Object resource);
}
