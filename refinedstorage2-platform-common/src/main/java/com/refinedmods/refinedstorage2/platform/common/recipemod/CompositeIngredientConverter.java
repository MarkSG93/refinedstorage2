package com.refinedmods.refinedstorage2.platform.common.recipemod;

import com.refinedmods.refinedstorage2.api.storage.ResourceTemplate;
import com.refinedmods.refinedstorage2.platform.api.recipemod.IngredientConverter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class CompositeIngredientConverter implements IngredientConverter {
    private final Collection<IngredientConverter> converters = new HashSet<>();

    @Override
    public Optional<ResourceTemplate> convertToResource(final Object ingredient) {
        return converters.stream()
            .flatMap(converter -> converter.convertToResource(ingredient).stream())
            .findFirst();
    }

    @Override
    public Optional<Object> convertToIngredient(final Object resource) {
        return converters.stream()
            .flatMap(converter -> converter.convertToIngredient(resource).stream())
            .findFirst();
    }

    public void addConverter(final IngredientConverter converter) {
        this.converters.add(converter);
    }
}
