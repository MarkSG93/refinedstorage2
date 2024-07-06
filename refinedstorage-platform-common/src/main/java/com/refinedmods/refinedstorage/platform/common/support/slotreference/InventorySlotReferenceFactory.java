package com.refinedmods.refinedstorage.platform.common.support.slotreference;

import com.refinedmods.refinedstorage.platform.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.platform.api.support.slotreference.SlotReferenceFactory;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class InventorySlotReferenceFactory implements SlotReferenceFactory {
    public static final SlotReferenceFactory INSTANCE = new InventorySlotReferenceFactory();

    private static final StreamCodec<RegistryFriendlyByteBuf, InventorySlotReference> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.INT, slotReference -> slotReference.slotIndex,
            InventorySlotReference::new
        );

    private InventorySlotReferenceFactory() {
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public StreamCodec<RegistryFriendlyByteBuf, SlotReference> getStreamCodec() {
        return (StreamCodec) STREAM_CODEC;
    }
}
