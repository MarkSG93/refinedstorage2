package com.refinedmods.refinedstorage.common.content;

import com.refinedmods.refinedstorage.common.autocrafting.PatternGridBlock;
import com.refinedmods.refinedstorage.common.constructordestructor.ConstructorBlock;
import com.refinedmods.refinedstorage.common.constructordestructor.DestructorBlock;
import com.refinedmods.refinedstorage.common.controller.AbstractControllerBlock;
import com.refinedmods.refinedstorage.common.controller.ControllerBlock;
import com.refinedmods.refinedstorage.common.controller.ControllerBlockEntityTicker;
import com.refinedmods.refinedstorage.common.controller.ControllerBlockItem;
import com.refinedmods.refinedstorage.common.controller.CreativeControllerBlock;
import com.refinedmods.refinedstorage.common.controller.CreativeControllerBlockItem;
import com.refinedmods.refinedstorage.common.detector.DetectorBlock;
import com.refinedmods.refinedstorage.common.exporter.ExporterBlock;
import com.refinedmods.refinedstorage.common.grid.CraftingGridBlock;
import com.refinedmods.refinedstorage.common.grid.GridBlock;
import com.refinedmods.refinedstorage.common.iface.InterfaceBlock;
import com.refinedmods.refinedstorage.common.importer.ImporterBlock;
import com.refinedmods.refinedstorage.common.networking.CableBlock;
import com.refinedmods.refinedstorage.common.networking.NetworkReceiverBlock;
import com.refinedmods.refinedstorage.common.networking.NetworkTransmitterBlock;
import com.refinedmods.refinedstorage.common.networking.RelayBlock;
import com.refinedmods.refinedstorage.common.security.SecurityManagerBlock;
import com.refinedmods.refinedstorage.common.storage.FluidStorageVariant;
import com.refinedmods.refinedstorage.common.storage.ItemStorageVariant;
import com.refinedmods.refinedstorage.common.storage.diskdrive.DiskDriveBlock;
import com.refinedmods.refinedstorage.common.storage.diskinterface.AbstractDiskInterfaceBlockEntity;
import com.refinedmods.refinedstorage.common.storage.diskinterface.DiskInterfaceBlock;
import com.refinedmods.refinedstorage.common.storage.externalstorage.ExternalStorageBlock;
import com.refinedmods.refinedstorage.common.storage.portablegrid.PortableGridBlock;
import com.refinedmods.refinedstorage.common.storage.storageblock.FluidStorageBlock;
import com.refinedmods.refinedstorage.common.storage.storageblock.ItemStorageBlock;
import com.refinedmods.refinedstorage.common.storagemonitor.StorageMonitorBlock;
import com.refinedmods.refinedstorage.common.support.BaseBlockItem;
import com.refinedmods.refinedstorage.common.support.SimpleBlock;
import com.refinedmods.refinedstorage.common.wirelesstransmitter.WirelessTransmitterBlock;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

import static java.util.Objects.requireNonNull;

public final class Blocks {
    public static final DyeColor COLOR = DyeColor.LIGHT_BLUE;
    public static final DyeColor CABLE_LIKE_COLOR = DyeColor.GRAY;
    public static final Blocks INSTANCE = new Blocks();

    private final BlockColorMap<CableBlock, BaseBlockItem> cable = new BlockColorMap<>(
        CableBlock::new,
        ContentIds.CABLE,
        ContentNames.CABLE,
        CABLE_LIKE_COLOR
    );
    private final BlockColorMap<GridBlock, BaseBlockItem> grid = new BlockColorMap<>(
        GridBlock::new,
        ContentIds.GRID,
        ContentNames.GRID,
        COLOR
    );
    private final BlockColorMap<CraftingGridBlock, BaseBlockItem> craftingGrid = new BlockColorMap<>(
        CraftingGridBlock::new,
        ContentIds.CRAFTING_GRID,
        ContentNames.CRAFTING_GRID,
        COLOR
    );
    private final BlockColorMap<PatternGridBlock, BaseBlockItem> patternGrid = new BlockColorMap<>(
        PatternGridBlock::new,
        ContentIds.PATTERN_GRID,
        ContentNames.PATTERN_GRID,
        COLOR
    );
    private final BlockColorMap<DetectorBlock, BaseBlockItem> detector = new BlockColorMap<>(
        DetectorBlock::new,
        ContentIds.DETECTOR,
        ContentNames.DETECTOR,
        COLOR
    );
    private final BlockColorMap<AbstractControllerBlock<ControllerBlockItem>, ControllerBlockItem> controller =
        new BlockColorMap<>(
            (color, name) -> new ControllerBlock(
                name,
                new ControllerBlockEntityTicker(BlockEntities.INSTANCE::getController),
                color
            ),
            ContentIds.CONTROLLER,
            ContentNames.CONTROLLER,
            COLOR
        );
    private final BlockColorMap
        <AbstractControllerBlock<CreativeControllerBlockItem>, CreativeControllerBlockItem> creativeController =
        new BlockColorMap<>((color, name) -> new CreativeControllerBlock(
            name,
            new ControllerBlockEntityTicker(BlockEntities.INSTANCE::getCreativeController),
            color
        ),
            ContentIds.CREATIVE_CONTROLLER,
            ContentNames.CREATIVE_CONTROLLER,
            COLOR
        );
    private final BlockColorMap<ExporterBlock, BaseBlockItem> exporter = new BlockColorMap<>(
        ExporterBlock::new,
        ContentIds.EXPORTER,
        ContentNames.EXPORTER,
        CABLE_LIKE_COLOR
    );
    private final BlockColorMap<ImporterBlock, BaseBlockItem> importer = new BlockColorMap<>(
        ImporterBlock::new,
        ContentIds.IMPORTER,
        ContentNames.IMPORTER,
        CABLE_LIKE_COLOR
    );
    private final BlockColorMap<ExternalStorageBlock, BaseBlockItem> externalStorage = new BlockColorMap<>(
        ExternalStorageBlock::new,
        ContentIds.EXTERNAL_STORAGE,
        ContentNames.EXTERNAL_STORAGE,
        CABLE_LIKE_COLOR
    );
    private final BlockColorMap<DestructorBlock, BaseBlockItem> destructor = new BlockColorMap<>(
        DestructorBlock::new,
        ContentIds.DESTRUCTOR,
        ContentNames.DESTRUCTOR,
        CABLE_LIKE_COLOR
    );
    private final BlockColorMap<ConstructorBlock, BaseBlockItem> constructor = new BlockColorMap<>(
        ConstructorBlock::new,
        ContentIds.CONSTRUCTOR,
        ContentNames.CONSTRUCTOR,
        CABLE_LIKE_COLOR
    );
    private final BlockColorMap<WirelessTransmitterBlock, BaseBlockItem> wirelessTransmitter = new BlockColorMap<>(
        WirelessTransmitterBlock::new,
        ContentIds.WIRELESS_TRANSMITTER,
        ContentNames.WIRELESS_TRANSMITTER,
        COLOR
    );
    private final BlockColorMap<NetworkReceiverBlock, BaseBlockItem> networkReceiver = new BlockColorMap<>(
        NetworkReceiverBlock::new,
        ContentIds.NETWORK_RECEIVER,
        ContentNames.NETWORK_RECEIVER,
        COLOR
    );
    private final BlockColorMap<NetworkTransmitterBlock, BaseBlockItem> networkTransmitter = new BlockColorMap<>(
        NetworkTransmitterBlock::new,
        ContentIds.NETWORK_TRANSMITTER,
        ContentNames.NETWORK_TRANSMITTER,
        COLOR
    );
    private final BlockColorMap<SecurityManagerBlock, BaseBlockItem> securityManager = new BlockColorMap<>(
        SecurityManagerBlock::new,
        ContentIds.SECURITY_MANAGER,
        ContentNames.SECURITY_MANAGER,
        COLOR
    );
    private final BlockColorMap<RelayBlock, BaseBlockItem> relay = new BlockColorMap<>(
        RelayBlock::new,
        ContentIds.RELAY,
        ContentNames.RELAY,
        COLOR
    );
    @Nullable
    private BlockColorMap<DiskInterfaceBlock, BaseBlockItem> diskInterface;

    @Nullable
    private Supplier<SimpleBlock> quartzEnrichedIronBlock;
    @Nullable
    private Supplier<SimpleBlock> quartzEnrichedCopperBlock;
    @Nullable
    private Supplier<DiskDriveBlock> diskDrive;
    @Nullable
    private Supplier<SimpleBlock> machineCasing;
    private final Map<ItemStorageVariant, Supplier<ItemStorageBlock>> itemStorageBlocks =
        new EnumMap<>(ItemStorageVariant.class);
    private final Map<FluidStorageVariant, Supplier<FluidStorageBlock>> fluidStorageBlocks =
        new EnumMap<>(FluidStorageVariant.class);
    @Nullable
    private Supplier<InterfaceBlock> iface;
    @Nullable
    private Supplier<StorageMonitorBlock> storageMonitor;
    @Nullable
    private Supplier<PortableGridBlock> portableGrid;
    @Nullable
    private Supplier<PortableGridBlock> creativePortableGrid;

    private Blocks() {
    }

    public BlockColorMap<CableBlock, BaseBlockItem> getCable() {
        return cable;
    }

    public SimpleBlock getQuartzEnrichedIronBlock() {
        return requireNonNull(quartzEnrichedIronBlock).get();
    }

    public SimpleBlock getQuartzEnrichedCopperBlock() {
        return requireNonNull(quartzEnrichedCopperBlock).get();
    }

    public DiskDriveBlock getDiskDrive() {
        return requireNonNull(diskDrive).get();
    }

    public SimpleBlock getMachineCasing() {
        return requireNonNull(machineCasing).get();
    }

    public BlockColorMap<GridBlock, BaseBlockItem> getGrid() {
        return grid;
    }

    public BlockColorMap<CraftingGridBlock, BaseBlockItem> getCraftingGrid() {
        return craftingGrid;
    }

    public BlockColorMap<PatternGridBlock, BaseBlockItem> getPatternGrid() {
        return patternGrid;
    }

    public BlockColorMap<AbstractControllerBlock<ControllerBlockItem>, ControllerBlockItem> getController() {
        return controller;
    }

    public BlockColorMap<
        AbstractControllerBlock<CreativeControllerBlockItem>,
        CreativeControllerBlockItem> getCreativeController() {
        return creativeController;
    }

    public void setQuartzEnrichedIronBlock(final Supplier<SimpleBlock> quartzEnrichedIronBlockSupplier) {
        this.quartzEnrichedIronBlock = quartzEnrichedIronBlockSupplier;
    }

    public void setQuartzEnrichedCopperBlock(final Supplier<SimpleBlock> quartzEnrichedCopperBlockSupplier) {
        this.quartzEnrichedCopperBlock = quartzEnrichedCopperBlockSupplier;
    }

    public void setDiskDrive(final Supplier<DiskDriveBlock> diskDriveSupplier) {
        this.diskDrive = diskDriveSupplier;
    }

    public void setMachineCasing(final Supplier<SimpleBlock> machineCasingSupplier) {
        this.machineCasing = machineCasingSupplier;
    }

    public void setItemStorageBlock(final ItemStorageVariant variant, final Supplier<ItemStorageBlock> supplier) {
        itemStorageBlocks.put(variant, supplier);
    }

    public ItemStorageBlock getItemStorageBlock(final ItemStorageVariant variant) {
        return itemStorageBlocks.get(variant).get();
    }

    public void setFluidStorageBlock(final FluidStorageVariant variant,
                                     final Supplier<FluidStorageBlock> supplier) {
        fluidStorageBlocks.put(variant, supplier);
    }

    public FluidStorageBlock getFluidStorageBlock(final FluidStorageVariant variant) {
        return fluidStorageBlocks.get(variant).get();
    }

    public BlockColorMap<ImporterBlock, BaseBlockItem> getImporter() {
        return importer;
    }

    public BlockColorMap<ExporterBlock, BaseBlockItem> getExporter() {
        return exporter;
    }

    public void setInterface(final Supplier<InterfaceBlock> interfaceSupplier) {
        this.iface = interfaceSupplier;
    }

    public InterfaceBlock getInterface() {
        return requireNonNull(iface).get();
    }

    public BlockColorMap<ExternalStorageBlock, BaseBlockItem> getExternalStorage() {
        return externalStorage;
    }

    public BlockColorMap<DetectorBlock, BaseBlockItem> getDetector() {
        return detector;
    }

    public BlockColorMap<DestructorBlock, BaseBlockItem> getDestructor() {
        return destructor;
    }

    public BlockColorMap<ConstructorBlock, BaseBlockItem> getConstructor() {
        return constructor;
    }

    public BlockColorMap<WirelessTransmitterBlock, BaseBlockItem> getWirelessTransmitter() {
        return wirelessTransmitter;
    }

    public void setStorageMonitor(final Supplier<StorageMonitorBlock> supplier) {
        this.storageMonitor = supplier;
    }

    public StorageMonitorBlock getStorageMonitor() {
        return requireNonNull(storageMonitor).get();
    }

    public BlockColorMap<NetworkReceiverBlock, BaseBlockItem> getNetworkReceiver() {
        return networkReceiver;
    }

    public BlockColorMap<NetworkTransmitterBlock, BaseBlockItem> getNetworkTransmitter() {
        return networkTransmitter;
    }

    public PortableGridBlock getPortableGrid() {
        return requireNonNull(portableGrid).get();
    }

    public void setPortableGrid(final Supplier<PortableGridBlock> supplier) {
        this.portableGrid = supplier;
    }

    public PortableGridBlock getCreativePortableGrid() {
        return requireNonNull(creativePortableGrid).get();
    }

    public void setCreativePortableGrid(final Supplier<PortableGridBlock> supplier) {
        this.creativePortableGrid = supplier;
    }

    public BlockColorMap<SecurityManagerBlock, BaseBlockItem> getSecurityManager() {
        return securityManager;
    }

    public BlockColorMap<RelayBlock, BaseBlockItem> getRelay() {
        return relay;
    }

    public BlockColorMap<DiskInterfaceBlock, BaseBlockItem> setDiskInterface(
        final BiFunction<BlockPos, BlockState, AbstractDiskInterfaceBlockEntity> blockEntityFactory
    ) {
        this.diskInterface = new BlockColorMap<>(
            (color, name) -> new DiskInterfaceBlock(
                color,
                name,
                blockEntityFactory
            ),
            ContentIds.DISK_INTERFACE,
            ContentNames.DISK_INTERFACE,
            COLOR
        );
        return diskInterface;
    }

    public BlockColorMap<DiskInterfaceBlock, BaseBlockItem> getDiskInterface() {
        return requireNonNull(diskInterface);
    }
}
