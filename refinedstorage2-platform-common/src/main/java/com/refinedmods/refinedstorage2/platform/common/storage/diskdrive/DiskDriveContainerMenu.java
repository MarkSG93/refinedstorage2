package com.refinedmods.refinedstorage2.platform.common.storage.diskdrive;

import com.refinedmods.refinedstorage2.platform.api.PlatformApi;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageContainerItem;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageInfo;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage2.platform.common.content.Menus;
import com.refinedmods.refinedstorage2.platform.common.storage.AbstractStorageContainerMenu;
import com.refinedmods.refinedstorage2.platform.common.storage.StorageAccessor;
import com.refinedmods.refinedstorage2.platform.common.storage.StorageConfigurationContainer;
import com.refinedmods.refinedstorage2.platform.common.support.containermenu.ResourceSlot;
import com.refinedmods.refinedstorage2.platform.common.support.containermenu.ValidatedSlot;
import com.refinedmods.refinedstorage2.platform.common.support.resource.ResourceContainerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static com.refinedmods.refinedstorage2.platform.common.util.IdentifierUtil.createTranslation;

public class DiskDriveContainerMenu extends AbstractStorageContainerMenu implements StorageAccessor {
    private static final int DISK_SLOT_X = 61;
    private static final int DISK_SLOT_Y = 54;

    private static final int FILTER_SLOT_X = 8;
    private static final int FILTER_SLOT_Y = 20;

    private final StorageDiskInfoAccessor storageInfoAccessor;

    private final List<Slot> diskSlots = new ArrayList<>();

    public DiskDriveContainerMenu(final int syncId, final Inventory playerInventory, final FriendlyByteBuf buf) {
        super(Menus.INSTANCE.getDiskDrive(), syncId);
        this.storageInfoAccessor = new StorageDiskInfoAccessorImpl(PlatformApi.INSTANCE.getStorageRepository(
            playerInventory.player.level()
        ));
        addSlots(
            playerInventory.player,
            new SimpleContainer(AbstractDiskDriveBlockEntity.AMOUNT_OF_DISKS),
            ResourceContainerImpl.createForFilter()
        );
        initializeResourceSlots(buf);
    }

    DiskDriveContainerMenu(final int syncId,
                           final Player player,
                           final SimpleContainer diskInventory,
                           final ResourceContainer resourceContainer,
                           final StorageConfigurationContainer configContainer,
                           final StorageDiskInfoAccessor storageInfoAccessor) {
        super(Menus.INSTANCE.getDiskDrive(), syncId, player, configContainer);
        this.storageInfoAccessor = storageInfoAccessor;
        addSlots(player, diskInventory, resourceContainer);
    }

    private void addSlots(final Player player,
                          final SimpleContainer diskInventory,
                          final ResourceContainer resourceContainer) {
        for (int i = 0; i < diskInventory.getContainerSize(); ++i) {
            diskSlots.add(addSlot(createDiskSlot(diskInventory, i)));
        }
        for (int i = 0; i < resourceContainer.size(); ++i) {
            addSlot(createFilterSlot(resourceContainer, i));
        }
        addPlayerInventory(player.getInventory(), 8, 141);

        transferManager.addBiTransfer(player.getInventory(), diskInventory);
        transferManager.addFilterTransfer(player.getInventory());
    }

    private Slot createFilterSlot(final ResourceContainer resourceContainer, final int i) {
        final int x = FILTER_SLOT_X + (18 * i);
        return new ResourceSlot(
            resourceContainer,
            i,
            createTranslation("gui", "storage.filter_help"),
            x,
            FILTER_SLOT_Y
        );
    }

    private Slot createDiskSlot(final SimpleContainer diskInventory, final int i) {
        final int x = DISK_SLOT_X + ((i % 2) * 18);
        final int y = DISK_SLOT_Y + Math.floorDiv(i, 2) * 18;
        return new ValidatedSlot(diskInventory, i, x, y, stack -> stack.getItem() instanceof StorageContainerItem);
    }

    @Override
    public boolean hasCapacity() {
        return getStorageDiskInfo().allMatch(info -> info.capacity() > 0);
    }

    @Override
    public double getProgress() {
        if (!hasCapacity()) {
            return 0;
        }
        return (double) getStored() / (double) getCapacity();
    }

    @Override
    public long getCapacity() {
        return getStorageDiskInfo().mapToLong(StorageInfo::capacity).sum();
    }

    @Override
    public long getStored() {
        return getStorageDiskInfo().mapToLong(StorageInfo::stored).sum();
    }

    private Stream<ItemStack> getDiskStacks() {
        return diskSlots
            .stream()
            .map(Slot::getItem)
            .filter(stack -> !stack.isEmpty());
    }

    private Stream<StorageInfo> getStorageDiskInfo() {
        return getDiskStacks()
            .map(storageInfoAccessor::getInfo)
            .flatMap(Optional::stream);
    }
}
