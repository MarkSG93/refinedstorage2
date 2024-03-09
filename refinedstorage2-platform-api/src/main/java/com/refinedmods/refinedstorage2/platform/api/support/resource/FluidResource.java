package com.refinedmods.refinedstorage2.platform.api.support.resource;

import com.refinedmods.refinedstorage2.api.core.CoreValidations;
import com.refinedmods.refinedstorage2.api.resource.ResourceKey;

import java.util.Optional;
import javax.annotation.Nullable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.apiguardian.api.API;

@API(status = API.Status.STABLE, since = "2.0.0-milestone.1.0")
public record FluidResource(Fluid fluid, @Nullable CompoundTag tag) implements ResourceKey, FuzzyModeNormalizer {
    private static final String TAG_TAG = "tag";
    private static final String TAG_ID = "id";

    public FluidResource(final Fluid fluid, @Nullable final CompoundTag tag) {
        this.fluid = CoreValidations.validateNotNull(fluid, "Fluid must not be null");
        this.tag = tag;
    }

    @Override
    public ResourceKey normalize() {
        return new FluidResource(fluid, null);
    }

    public static CompoundTag toTag(final FluidResource fluidResource) {
        final CompoundTag tag = new CompoundTag();
        if (fluidResource.tag() != null) {
            tag.put(TAG_TAG, fluidResource.tag());
        }
        tag.putString(TAG_ID, BuiltInRegistries.FLUID.getKey(fluidResource.fluid()).toString());
        return tag;
    }

    public static Optional<ResourceKey> fromTag(final CompoundTag tag) {
        final ResourceLocation id = new ResourceLocation(tag.getString(TAG_ID));
        final Fluid fluid = BuiltInRegistries.FLUID.get(id);
        if (fluid == Fluids.EMPTY) {
            return Optional.empty();
        }
        final CompoundTag itemTag = tag.contains(TAG_TAG) ? tag.getCompound(TAG_TAG) : null;
        return Optional.of(new FluidResource(fluid, itemTag));
    }
}
