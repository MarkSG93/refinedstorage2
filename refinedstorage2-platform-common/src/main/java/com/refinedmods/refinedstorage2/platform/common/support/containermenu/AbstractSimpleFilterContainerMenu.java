package com.refinedmods.refinedstorage2.platform.common.support.containermenu;

import com.refinedmods.refinedstorage2.platform.api.PlatformApi;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage2.platform.common.support.resource.ResourceContainerImpl;
import com.refinedmods.refinedstorage2.platform.common.upgrade.UpgradeContainer;
import com.refinedmods.refinedstorage2.platform.common.upgrade.UpgradeDestinations;
import com.refinedmods.refinedstorage2.platform.common.upgrade.UpgradeSlot;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractSimpleFilterContainerMenu<T extends BlockEntity>
    extends AbstractResourceContainerMenu {
    private static final int FILTER_SLOT_X = 8;
    private static final int FILTER_SLOT_Y = 20;

    private final Component filterHelp;

    protected AbstractSimpleFilterContainerMenu(final MenuType<?> type,
                                                final int syncId,
                                                final Player player,
                                                final ResourceContainer resourceContainer,
                                                final UpgradeContainer upgradeContainer,
                                                final T blockEntity,
                                                final Component filterHelp) {
        super(type, syncId, player);
        this.filterHelp = filterHelp;
        registerServerProperties(blockEntity);
        addSlots(player, resourceContainer, upgradeContainer);
    }

    protected AbstractSimpleFilterContainerMenu(final MenuType<?> type,
                                                final int syncId,
                                                final Player player,
                                                final FriendlyByteBuf buf,
                                                final UpgradeDestinations upgradeDestination,
                                                final Component filterHelp) {
        super(type, syncId);
        this.filterHelp = filterHelp;
        registerClientProperties();
        addSlots(
            player,
            ResourceContainerImpl.createForFilter(),
            new UpgradeContainer(upgradeDestination, PlatformApi.INSTANCE.getUpgradeRegistry())
        );
        initializeResourceSlots(buf);
    }

    protected abstract void registerClientProperties();

    protected abstract void registerServerProperties(T blockEntity);

    private void addSlots(final Player player,
                          final ResourceContainer resourceContainer,
                          final UpgradeContainer upgradeContainer) {
        for (int i = 0; i < resourceContainer.size(); ++i) {
            addSlot(createFilterSlot(resourceContainer, i));
        }
        for (int i = 0; i < upgradeContainer.getContainerSize(); ++i) {
            addSlot(new UpgradeSlot(upgradeContainer, i, 187, 6 + (i * 18)));
        }
        addPlayerInventory(player.getInventory(), 8, 55);

        transferManager.addBiTransfer(player.getInventory(), upgradeContainer);
        transferManager.addFilterTransfer(player.getInventory());
    }

    private Slot createFilterSlot(final ResourceContainer resourceContainer, final int i) {
        final int x = FILTER_SLOT_X + (18 * i);
        return new ResourceSlot(resourceContainer, i, filterHelp, x, FILTER_SLOT_Y, ResourceSlotType.FILTER);
    }
}
