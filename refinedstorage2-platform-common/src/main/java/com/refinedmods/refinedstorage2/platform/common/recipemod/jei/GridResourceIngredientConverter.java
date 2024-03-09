package com.refinedmods.refinedstorage2.platform.common.recipemod.jei;

import com.refinedmods.refinedstorage2.api.storage.ResourceTemplate;
import com.refinedmods.refinedstorage2.platform.api.recipemod.IngredientConverter;
import com.refinedmods.refinedstorage2.platform.api.support.resource.FluidResource;
import com.refinedmods.refinedstorage2.platform.common.grid.view.FluidGridResource;
import com.refinedmods.refinedstorage2.platform.common.grid.view.ItemGridResource;

import java.util.Optional;

import mezz.jei.api.helpers.IPlatformFluidHelper;

class GridResourceIngredientConverter implements IngredientConverter {
    private final IPlatformFluidHelper<?> fluidHelper;

    GridResourceIngredientConverter(final IPlatformFluidHelper<?> fluidHelper) {
        this.fluidHelper = fluidHelper;
    }

    @Override
    public Optional<ResourceTemplate> convertToResource(final Object ingredient) {
        return Optional.empty();
    }

    @Override
    public Optional<Object> convertToIngredient(final Object resource) {
        if (resource instanceof ItemGridResource itemGridResource) {
            return Optional.of(itemGridResource.copyItemStack());
        }
        if (resource instanceof FluidGridResource fluidGridResource) {
            final FluidResource fluidResource = (FluidResource) fluidGridResource.getResource();
            return Optional.of(fluidHelper.create(
                fluidResource.fluid(),
                fluidHelper.bucketVolume(),
                fluidResource.tag()
            ));
        }
        return Optional.empty();
    }
}
