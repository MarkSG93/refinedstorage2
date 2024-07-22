package com.refinedmods.refinedstorage.common.support.energy;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.common.api.PlatformApi;
import com.refinedmods.refinedstorage.common.api.support.energy.EnergyItemHelper;
import com.refinedmods.refinedstorage.common.api.support.energy.TransferableBlockEntityEnergy;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createStoredWithCapacityTranslation;

public class EnergyItemHelperImpl implements EnergyItemHelper {
    @Override
    public void addTooltip(final ItemStack stack, final List<Component> lines) {
        PlatformApi.INSTANCE.getEnergyStorage(stack).ifPresent(energyStorage -> {
            final long stored = energyStorage.getStored();
            final long capacity = energyStorage.getCapacity();
            final double pct = stored / (double) capacity;
            lines.add(createStoredWithCapacityTranslation(stored, capacity, pct).withStyle(ChatFormatting.GRAY));
        });
    }

    @Override
    public boolean isBarVisible(final ItemStack stack) {
        return PlatformApi.INSTANCE.getEnergyStorage(stack).isPresent();
    }

    @Override
    public int getBarWidth(final ItemStack stack) {
        return PlatformApi.INSTANCE.getEnergyStorage(stack).map(energyStorage -> (int) Math.round(
            (energyStorage.getStored() / (double) energyStorage.getCapacity()) * 13D
        )).orElse(0);
    }

    @Override
    public int getBarColor(final ItemStack stack) {
        return PlatformApi.INSTANCE.getEnergyStorage(stack).map(energyStorage -> Mth.hsvToRgb(
            Math.max(0.0F, (float) energyStorage.getStored() / (float) energyStorage.getCapacity()) / 3.0F,
            1.0F,
            1.0F
        )).orElse(0);
    }

    @Override
    public ItemStack createAtEnergyCapacity(final Item item) {
        final ItemStack stack = item.getDefaultInstance();
        PlatformApi.INSTANCE.getEnergyStorage(stack).ifPresent(energyStorage -> energyStorage.receive(
            energyStorage.getCapacity(),
            Action.EXECUTE
        ));
        return stack;
    }

    @Override
    public void passEnergyToBlockEntity(final BlockPos pos, final Level level, final ItemStack stack) {
        if (level.isClientSide()
            || !(level.getBlockEntity(pos) instanceof TransferableBlockEntityEnergy transferableBlockEntityEnergy)) {
            return;
        }
        PlatformApi.INSTANCE.getEnergyStorage(stack).ifPresent(
            energyStorage -> transferableBlockEntityEnergy.getEnergyStorage()
                .receive(energyStorage.getStored(), Action.EXECUTE)
        );
    }
}
