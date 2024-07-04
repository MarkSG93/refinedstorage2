package com.refinedmods.refinedstorage.api.resource;

import com.refinedmods.refinedstorage.api.core.CoreValidations;

import org.apiguardian.api.API;

/**
 * A class representing a resource and a corresponding amount.
 * The resource cannot be mutated but the amount can be modified.
 */
@API(status = API.Status.STABLE, since = "2.0.0-milestone.1.2")
public final class ResourceAmount {
    private final ResourceKey resource;
    private long amount;

    /**
     * @param resource the resource, must be non-null
     * @param amount   the amount, must be larger than 0
     */
    public ResourceAmount(final ResourceKey resource, final long amount) {
        validate(resource, amount);
        this.resource = resource;
        this.amount = amount;
    }

    public ResourceKey getResource() {
        return resource;
    }

    public long getAmount() {
        return amount;
    }

    /**
     * Increments with the given amount.
     *
     * @param amountToIncrement the amount to increment, must be larger than 0
     */
    public void increment(final long amountToIncrement) {
        CoreValidations.validateLargerThanZero(amountToIncrement, "Amount to increment must be larger than 0");
        this.amount += amountToIncrement;
    }

    /**
     * Decrements with the given amount.
     * The amount, after performing this decrement, may not be 0 or less than 0.
     *
     * @param amountToDecrement the amount to decrement, a positive number
     */
    public void decrement(final long amountToDecrement) {
        CoreValidations.validateLargerThanZero(amountToDecrement, "Amount to decrement must be larger than 0");
        CoreValidations.validateLargerThanZero(
            amount - amountToDecrement,
            "Cannot decrement, amount will be zero or negative"
        );
        this.amount -= amountToDecrement;
    }

    @Override
    public String toString() {
        return "ResourceAmount{"
            + "resource=" + resource
            + ", amount=" + amount
            + '}';
    }

    public static void validate(final ResourceKey resource, final long amount) {
        CoreValidations.validateLargerThanZero(amount, "Amount must be larger than 0");
        CoreValidations.validateNotNull(resource, "Resource must not be null");
    }
}
