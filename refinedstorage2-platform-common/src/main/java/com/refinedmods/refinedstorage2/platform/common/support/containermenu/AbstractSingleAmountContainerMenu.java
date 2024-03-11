package com.refinedmods.refinedstorage2.platform.common.support.containermenu;

import com.refinedmods.refinedstorage2.platform.api.PlatformApi;
import com.refinedmods.refinedstorage2.platform.api.support.network.bounditem.SlotReference;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage2.platform.common.Platform;
import com.refinedmods.refinedstorage2.platform.common.support.resource.ResourceContainerImpl;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public abstract class AbstractSingleAmountContainerMenu extends AbstractResourceContainerMenu {
    private double clientAmount;

    private final Component filterHelpText;

    protected AbstractSingleAmountContainerMenu(final MenuType<?> type,
                                                final int syncId,
                                                final Inventory playerInventory,
                                                final FriendlyByteBuf buf,
                                                final Component filterHelpText) {
        super(type, syncId);
        if (buf.readBoolean()) {
            disabledSlot = PlatformApi.INSTANCE.getSlotReference(buf).orElse(null);
        }
        this.clientAmount = buf.readDouble();
        this.filterHelpText = filterHelpText;
        addSlots(playerInventory.player, ResourceContainerImpl.createForFilter(1));
        initializeResourceSlots(buf);
    }

    protected AbstractSingleAmountContainerMenu(final MenuType<?> type,
                                                final int syncId,
                                                final Player player,
                                                final ResourceContainer resourceContainer,
                                                final Component filterHelpText,
                                                @Nullable final SlotReference disabledSlotReference) {
        super(type, syncId, player);
        this.disabledSlot = disabledSlotReference;
        this.filterHelpText = filterHelpText;
        addSlots(player, resourceContainer);
    }

    private void addSlots(final Player player, final ResourceContainer resourceContainer) {
        addSlot(new ResourceSlot(resourceContainer, 0, filterHelpText, 116, 47, ResourceSlotType.FILTER));
        addPlayerInventory(player.getInventory(), 8, 106);
        transferManager.addFilterTransfer(player.getInventory());
    }

    public double getAmount() {
        return clientAmount;
    }

    public void changeAmountOnClient(final double newAmount) {
        Platform.INSTANCE.getClientToServerCommunications().sendSingleAmountChange(newAmount);
        this.clientAmount = newAmount;
    }

    public abstract void changeAmountOnServer(double newAmount);

    public static void writeToBuf(final FriendlyByteBuf buf,
                                  final double amount,
                                  final ResourceContainer container,
                                  @Nullable final SlotReference disabledSlotReference) {
        if (disabledSlotReference != null) {
            buf.writeBoolean(true);
            PlatformApi.INSTANCE.writeSlotReference(disabledSlotReference, buf);
        } else {
            buf.writeBoolean(false);
        }
        buf.writeDouble(amount);
        container.writeToUpdatePacket(buf);
    }
}
