package com.refinedmods.refinedstorage.common.controller;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ControllerItemPropertyFunction implements ClampedItemPropertyFunction {
    @Override
    public float unclampedCall(final ItemStack stack,
                               @Nullable final ClientLevel level,
                               @Nullable final LivingEntity entity,
                               final int seed) {
        return RefinedStorageApi.INSTANCE.getEnergyStorage(stack).map(energyStorage -> {
            if (energyStorage.getStored() == 0) {
                return 1F;
            }
            return (float) energyStorage.getStored() / (float) energyStorage.getCapacity();
        }).orElse(1F);
    }
}
