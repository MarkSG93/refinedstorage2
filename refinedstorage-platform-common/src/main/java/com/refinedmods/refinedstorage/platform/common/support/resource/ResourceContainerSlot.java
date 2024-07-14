package com.refinedmods.refinedstorage.platform.common.support.resource;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.platform.api.support.resource.PlatformResourceKey;

import java.util.Objects;

import net.minecraft.world.item.ItemStack;

class ResourceContainerSlot {
    private final ResourceAmount resourceAmount;
    private final ItemStack stackRepresentation;

    ResourceContainerSlot(final ResourceAmount resourceAmount) {
        this.resourceAmount = resourceAmount;
        this.stackRepresentation = resourceAmount.getResource() instanceof ItemResource itemResource
            ? itemResource.toItemStack(resourceAmount.getAmount())
            : ItemStack.EMPTY;
    }

    ResourceAmount getResourceAmount() {
        return resourceAmount;
    }

    PlatformResourceKey getPlatformResource() {
        return (PlatformResourceKey) resourceAmount.getResource();
    }

    ItemStack getStackRepresentation() {
        return stackRepresentation;
    }

    ResourceContainerSlot withAmount(final long newAmount) {
        return new ResourceContainerSlot(new ResourceAmount(resourceAmount.getResource(), newAmount));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ResourceContainerSlot that = (ResourceContainerSlot) o;
        return Objects.equals(resourceAmount, that.resourceAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceAmount);
    }
}
