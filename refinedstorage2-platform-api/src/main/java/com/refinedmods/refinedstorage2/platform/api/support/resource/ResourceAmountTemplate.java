package com.refinedmods.refinedstorage2.platform.api.support.resource;

import com.refinedmods.refinedstorage2.api.resource.ResourceKey;
import com.refinedmods.refinedstorage2.api.storage.ResourceTemplate;
import com.refinedmods.refinedstorage2.platform.api.storage.channel.PlatformStorageChannelType;

import java.util.Objects;

import net.minecraft.world.item.ItemStack;
import org.apiguardian.api.API;

/**
 * A ResourceAmountTemplate is the combination of a {@link com.refinedmods.refinedstorage2.api.resource.ResourceAmount}
 * and a {@link ResourceTemplate}. It identifies a resource, its storage channel type and an amount.
 * Additionally, for performance reasons, it provides an {@link ItemStack} representation.
 */
@API(status = API.Status.STABLE, since = "2.0.0-milestone.2.13")
public class ResourceAmountTemplate {
    private final ResourceTemplate resourceTemplate;
    private final long amount;
    private final ItemStack stackRepresentation;

    public ResourceAmountTemplate(final ResourceKey resource,
                                  final long amount,
                                  final PlatformStorageChannelType storageChannelType) {
        this.resourceTemplate = new ResourceTemplate(resource, storageChannelType);
        this.amount = amount;
        this.stackRepresentation = resource instanceof ItemResource itemResource
            ? itemResource.toItemStack(amount)
            : ItemStack.EMPTY;
    }

    public ResourceKey getResource() {
        return resourceTemplate.resource();
    }

    public PlatformStorageChannelType getStorageChannelType() {
        return (PlatformStorageChannelType) resourceTemplate.storageChannelType();
    }

    public long getAmount() {
        return amount;
    }

    public ResourceTemplate getResourceTemplate() {
        return resourceTemplate;
    }

    public ItemStack getStackRepresentation() {
        return stackRepresentation;
    }

    public ResourceAmountTemplate withAmount(final long newAmount) {
        return new ResourceAmountTemplate(
            resourceTemplate.resource(),
            newAmount,
            (PlatformStorageChannelType) resourceTemplate.storageChannelType()
        );
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ResourceAmountTemplate that = (ResourceAmountTemplate) o;
        return Objects.equals(amount, that.amount)
            && Objects.equals(resourceTemplate.resource(), that.resourceTemplate.resource());
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, resourceTemplate.resource());
    }
}
