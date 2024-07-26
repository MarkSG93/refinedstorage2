package com.refinedmods.refinedstorage.common.configurationcard;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;

public class ConfigurationCardItemPropertyFunction implements ClampedItemPropertyFunction {
    public static final ResourceLocation NAME = createIdentifier("configuration_card_active");

    @Override
    public float unclampedCall(final ItemStack itemStack,
                               @Nullable final ClientLevel clientLevel,
                               @Nullable final LivingEntity livingEntity,
                               final int i) {
        if (itemStack.getItem() instanceof ConfigurationCardItem cardItem) {
            return cardItem.isActive(itemStack) ? 1 : 0;
        }
        return 0;
    }
}
