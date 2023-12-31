package com.refinedmods.refinedstorage2.platform.common;

import com.refinedmods.refinedstorage2.api.core.component.ComponentMapFactory;
import com.refinedmods.refinedstorage2.api.network.Network;
import com.refinedmods.refinedstorage2.api.network.NetworkBuilder;
import com.refinedmods.refinedstorage2.api.network.component.NetworkComponent;
import com.refinedmods.refinedstorage2.api.network.energy.EnergyStorage;
import com.refinedmods.refinedstorage2.api.network.impl.NetworkBuilderImpl;
import com.refinedmods.refinedstorage2.api.network.impl.NetworkFactory;
import com.refinedmods.refinedstorage2.api.network.node.container.NetworkNodeContainer;
import com.refinedmods.refinedstorage2.platform.api.PlatformApi;
import com.refinedmods.refinedstorage2.platform.api.constructordestructor.ConstructorStrategyFactory;
import com.refinedmods.refinedstorage2.platform.api.constructordestructor.DestructorStrategyFactory;
import com.refinedmods.refinedstorage2.platform.api.exporter.ExporterTransferStrategyFactory;
import com.refinedmods.refinedstorage2.platform.api.grid.Grid;
import com.refinedmods.refinedstorage2.platform.api.grid.GridInsertionHint;
import com.refinedmods.refinedstorage2.platform.api.grid.GridInsertionHints;
import com.refinedmods.refinedstorage2.platform.api.grid.GridSynchronizer;
import com.refinedmods.refinedstorage2.platform.api.grid.strategy.GridExtractionStrategy;
import com.refinedmods.refinedstorage2.platform.api.grid.strategy.GridExtractionStrategyFactory;
import com.refinedmods.refinedstorage2.platform.api.grid.strategy.GridInsertionStrategy;
import com.refinedmods.refinedstorage2.platform.api.grid.strategy.GridInsertionStrategyFactory;
import com.refinedmods.refinedstorage2.platform.api.grid.strategy.GridScrollingStrategy;
import com.refinedmods.refinedstorage2.platform.api.grid.strategy.GridScrollingStrategyFactory;
import com.refinedmods.refinedstorage2.platform.api.importer.ImporterTransferStrategyFactory;
import com.refinedmods.refinedstorage2.platform.api.recipemod.IngredientConverter;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageContainerItemHelper;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageRepository;
import com.refinedmods.refinedstorage2.platform.api.storage.StorageType;
import com.refinedmods.refinedstorage2.platform.api.storage.channel.PlatformStorageChannelType;
import com.refinedmods.refinedstorage2.platform.api.storage.externalstorage.PlatformExternalStorageProviderFactory;
import com.refinedmods.refinedstorage2.platform.api.storagemonitor.StorageMonitorExtractionStrategy;
import com.refinedmods.refinedstorage2.platform.api.storagemonitor.StorageMonitorInsertionStrategy;
import com.refinedmods.refinedstorage2.platform.api.support.energy.EnergyItemHelper;
import com.refinedmods.refinedstorage2.platform.api.support.network.bounditem.NetworkBoundItemHelper;
import com.refinedmods.refinedstorage2.platform.api.support.network.bounditem.SlotReference;
import com.refinedmods.refinedstorage2.platform.api.support.network.bounditem.SlotReferenceFactory;
import com.refinedmods.refinedstorage2.platform.api.support.network.bounditem.SlotReferenceProvider;
import com.refinedmods.refinedstorage2.platform.api.support.registry.PlatformRegistry;
import com.refinedmods.refinedstorage2.platform.api.support.resource.FluidResource;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ItemResource;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ResourceFactory;
import com.refinedmods.refinedstorage2.platform.api.support.resource.ResourceRendering;
import com.refinedmods.refinedstorage2.platform.api.upgrade.BuiltinUpgradeDestinations;
import com.refinedmods.refinedstorage2.platform.api.upgrade.UpgradeRegistry;
import com.refinedmods.refinedstorage2.platform.api.wirelesstransmitter.WirelessTransmitterRangeModifier;
import com.refinedmods.refinedstorage2.platform.common.grid.NoOpGridSynchronizer;
import com.refinedmods.refinedstorage2.platform.common.grid.screen.AbstractGridScreen;
import com.refinedmods.refinedstorage2.platform.common.grid.screen.hint.GridInsertionHintsImpl;
import com.refinedmods.refinedstorage2.platform.common.grid.screen.hint.ItemGridInsertionHint;
import com.refinedmods.refinedstorage2.platform.common.grid.screen.hint.SingleItemGridInsertionHint;
import com.refinedmods.refinedstorage2.platform.common.grid.strategy.CompositeGridExtractionStrategy;
import com.refinedmods.refinedstorage2.platform.common.grid.strategy.CompositeGridInsertionStrategy;
import com.refinedmods.refinedstorage2.platform.common.grid.strategy.CompositeGridScrollingStrategy;
import com.refinedmods.refinedstorage2.platform.common.recipemod.CompositeIngredientConverter;
import com.refinedmods.refinedstorage2.platform.common.storage.ClientStorageRepository;
import com.refinedmods.refinedstorage2.platform.common.storage.StorageContainerItemHelperImpl;
import com.refinedmods.refinedstorage2.platform.common.storage.StorageRepositoryImpl;
import com.refinedmods.refinedstorage2.platform.common.storage.StorageTypes;
import com.refinedmods.refinedstorage2.platform.common.storage.channel.StorageChannelTypes;
import com.refinedmods.refinedstorage2.platform.common.storagemonitor.CompositeStorageMonitorExtractionStrategy;
import com.refinedmods.refinedstorage2.platform.common.storagemonitor.CompositeStorageMonitorInsertionStrategy;
import com.refinedmods.refinedstorage2.platform.common.support.energy.EnergyItemHelperImpl;
import com.refinedmods.refinedstorage2.platform.common.support.energy.ItemEnergyStorage;
import com.refinedmods.refinedstorage2.platform.common.support.network.ConnectionProviderImpl;
import com.refinedmods.refinedstorage2.platform.common.support.network.bounditem.CompositeSlotReferenceProvider;
import com.refinedmods.refinedstorage2.platform.common.support.network.bounditem.InventorySlotReference;
import com.refinedmods.refinedstorage2.platform.common.support.network.bounditem.InventorySlotReferenceFactory;
import com.refinedmods.refinedstorage2.platform.common.support.network.bounditem.NetworkBoundItemHelperImpl;
import com.refinedmods.refinedstorage2.platform.common.support.registry.PlatformRegistryImpl;
import com.refinedmods.refinedstorage2.platform.common.support.resource.FluidResourceFactory;
import com.refinedmods.refinedstorage2.platform.common.support.resource.ItemResourceFactory;
import com.refinedmods.refinedstorage2.platform.common.upgrade.BuiltinUpgradeDestinationsImpl;
import com.refinedmods.refinedstorage2.platform.common.upgrade.UpgradeRegistryImpl;
import com.refinedmods.refinedstorage2.platform.common.util.IdentifierUtil;
import com.refinedmods.refinedstorage2.platform.common.util.ServerEventQueue;
import com.refinedmods.refinedstorage2.platform.common.wirelesstransmitter.CompositeWirelessTransmitterRangeModifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.refinedmods.refinedstorage2.platform.common.util.IdentifierUtil.createIdentifier;

public class PlatformApiImpl implements PlatformApi {
    private static final String ITEM_REGISTRY_KEY = "item";

    private final StorageRepository clientStorageRepository =
        new ClientStorageRepository(Platform.INSTANCE.getClientToServerCommunications()::sendStorageInfoRequest);
    private final ComponentMapFactory<NetworkComponent, Network> networkComponentMapFactory =
        new ComponentMapFactory<>();
    private final NetworkBuilder networkBuilder =
        new NetworkBuilderImpl(new NetworkFactory(networkComponentMapFactory));
    private final PlatformRegistry<StorageType<?>> storageTypeRegistry =
        new PlatformRegistryImpl<>(createIdentifier(ITEM_REGISTRY_KEY), StorageTypes.ITEM);
    private final PlatformRegistry<PlatformStorageChannelType<?>> storageChannelTypeRegistry =
        new PlatformRegistryImpl<>(createIdentifier(ITEM_REGISTRY_KEY), StorageChannelTypes.ITEM);
    private final PlatformRegistry<GridSynchronizer> gridSynchronizerRegistry =
        new PlatformRegistryImpl<>(createIdentifier("off"), new NoOpGridSynchronizer());
    private final PlatformRegistry<ImporterTransferStrategyFactory> importerTransferStrategyRegistry =
        new PlatformRegistryImpl<>(createIdentifier("noop"),
            (level, pos, direction, upgradeState, amountOverride) -> (filter, actor, network) -> false);
    private final PlatformRegistry<ExporterTransferStrategyFactory> exporterTransferStrategyRegistry =
        new PlatformRegistryImpl<>(createIdentifier("noop"),
            (level, pos, direction, upgradeState, amountOverride, fuzzyMode) -> (resource, actor, network) -> false);
    private final UpgradeRegistry upgradeRegistry = new UpgradeRegistryImpl();
    private final BuiltinUpgradeDestinations builtinUpgradeDestinations = new BuiltinUpgradeDestinationsImpl();
    private final Queue<PlatformExternalStorageProviderFactory> externalStorageProviderFactories = new PriorityQueue<>(
        Comparator.comparingInt(PlatformExternalStorageProviderFactory::getPriority)
    );
    private final Queue<DestructorStrategyFactory> destructorStrategyFactories = new PriorityQueue<>(
        Comparator.comparingInt(DestructorStrategyFactory::getPriority)
    );
    private final Queue<ConstructorStrategyFactory> constructorStrategyFactories = new PriorityQueue<>(
        Comparator.comparingInt(ConstructorStrategyFactory::getPriority)
    );
    private final CompositeStorageMonitorInsertionStrategy storageMonitorInsertionStrategy =
        new CompositeStorageMonitorInsertionStrategy();
    private final CompositeStorageMonitorExtractionStrategy storageMonitorExtractionStrategy =
        new CompositeStorageMonitorExtractionStrategy();
    private final CompositeIngredientConverter compositeConverter = new CompositeIngredientConverter();
    private final StorageContainerItemHelper storageContainerItemHelper = new StorageContainerItemHelperImpl();
    private final List<GridInsertionStrategyFactory> gridInsertionStrategyFactories = new ArrayList<>();
    private final GridInsertionHintsImpl gridInsertionHints = new GridInsertionHintsImpl(
        new ItemGridInsertionHint(),
        new SingleItemGridInsertionHint()
    );
    private final List<GridExtractionStrategyFactory> gridExtractionStrategyFactories = new ArrayList<>();
    private final List<GridScrollingStrategyFactory> gridScrollingStrategyFactories = new ArrayList<>();
    private final ResourceFactory<ItemResource> itemResourceFactory = new ItemResourceFactory();
    private final ResourceFactory<FluidResource> fluidResourceFactory = new FluidResourceFactory();
    private final Set<ResourceFactory<?>> resourceFactories = new HashSet<>();
    private final Map<Class<?>, ResourceRendering<?>> resourceRenderingMap = new HashMap<>();
    private final CompositeWirelessTransmitterRangeModifier wirelessTransmitterRangeModifier =
        new CompositeWirelessTransmitterRangeModifier();
    private final EnergyItemHelper energyItemHelper = new EnergyItemHelperImpl();
    private final NetworkBoundItemHelper networkBoundItemHelper = new NetworkBoundItemHelperImpl();
    private final PlatformRegistry<SlotReferenceFactory> slotReferenceFactoryRegistry = new PlatformRegistryImpl<>(
        createIdentifier("inventory"),
        InventorySlotReferenceFactory.INSTANCE
    );
    private final CompositeSlotReferenceProvider slotReferenceProvider = new CompositeSlotReferenceProvider();

    @Override
    public PlatformRegistry<StorageType<?>> getStorageTypeRegistry() {
        return storageTypeRegistry;
    }

    @Override
    public StorageRepository getStorageRepository(final Level level) {
        if (level.getServer() == null) {
            return clientStorageRepository;
        }
        final ServerLevel serverLevel = Objects.requireNonNull(level.getServer().getLevel(Level.OVERWORLD));
        return serverLevel
            .getDataStorage()
            .computeIfAbsent(
                this::createStorageRepository,
                this::createStorageRepository,
                StorageRepositoryImpl.NAME
            );
    }

    @Override
    public StorageContainerItemHelper getStorageContainerItemHelper() {
        return storageContainerItemHelper;
    }

    private StorageRepositoryImpl createStorageRepository(final CompoundTag tag) {
        final StorageRepositoryImpl repository = createStorageRepository();
        repository.read(tag);
        return repository;
    }

    private StorageRepositoryImpl createStorageRepository() {
        return new StorageRepositoryImpl(storageTypeRegistry);
    }

    @Override
    public PlatformRegistry<PlatformStorageChannelType<?>> getStorageChannelTypeRegistry() {
        return storageChannelTypeRegistry;
    }

    @Override
    public PlatformRegistry<ImporterTransferStrategyFactory> getImporterTransferStrategyRegistry() {
        return importerTransferStrategyRegistry;
    }

    @Override
    public PlatformRegistry<ExporterTransferStrategyFactory> getExporterTransferStrategyRegistry() {
        return exporterTransferStrategyRegistry;
    }

    @Override
    public void addExternalStorageProviderFactory(final PlatformExternalStorageProviderFactory factory) {
        externalStorageProviderFactories.add(factory);
    }

    @Override
    public Collection<PlatformExternalStorageProviderFactory> getExternalStorageProviderFactories() {
        return externalStorageProviderFactories;
    }

    @Override
    public Collection<DestructorStrategyFactory> getDestructorStrategyFactories() {
        return destructorStrategyFactories;
    }

    @Override
    public void addDestructorStrategyFactory(final DestructorStrategyFactory factory) {
        destructorStrategyFactories.add(factory);
    }

    @Override
    public Collection<ConstructorStrategyFactory> getConstructorStrategyFactories() {
        return constructorStrategyFactories;
    }

    @Override
    public void addConstructorStrategyFactory(final ConstructorStrategyFactory factory) {
        constructorStrategyFactories.add(factory);
    }

    @Override
    public void addStorageMonitorExtractionStrategy(final StorageMonitorExtractionStrategy strategy) {
        storageMonitorExtractionStrategy.addStrategy(strategy);
    }

    @Override
    public StorageMonitorExtractionStrategy getStorageMonitorExtractionStrategy() {
        return storageMonitorExtractionStrategy;
    }

    @Override
    public void addStorageMonitorInsertionStrategy(final StorageMonitorInsertionStrategy strategy) {
        storageMonitorInsertionStrategy.addStrategy(strategy);
    }

    @Override
    public StorageMonitorInsertionStrategy getStorageMonitorInsertionStrategy() {
        return storageMonitorInsertionStrategy;
    }

    @Override
    public MutableComponent createTranslation(final String category, final String value, final Object... args) {
        return IdentifierUtil.createTranslation(category, value, args);
    }

    @Override
    public ComponentMapFactory<NetworkComponent, Network> getNetworkComponentMapFactory() {
        return networkComponentMapFactory;
    }

    @Override
    public PlatformRegistry<GridSynchronizer> getGridSynchronizerRegistry() {
        return gridSynchronizerRegistry;
    }

    @Override
    public void writeGridScreenOpeningData(final Grid grid, final FriendlyByteBuf buf) {
        AbstractGridScreen.writeScreenOpeningData(storageChannelTypeRegistry, grid, buf);
    }

    @Override
    public UpgradeRegistry getUpgradeRegistry() {
        return upgradeRegistry;
    }

    @Override
    public BuiltinUpgradeDestinations getBuiltinUpgradeDestinations() {
        return builtinUpgradeDestinations;
    }

    @Override
    public void requestNetworkNodeInitialization(final NetworkNodeContainer container,
                                                 final Level level,
                                                 final Runnable callback) {
        final ConnectionProviderImpl connectionProvider = new ConnectionProviderImpl(level);
        ServerEventQueue.queue(() -> {
            networkBuilder.initialize(container, connectionProvider);
            callback.run();
        });
    }

    @Override
    public void requestNetworkNodeRemoval(final NetworkNodeContainer container, final Level level) {
        final ConnectionProviderImpl connectionProvider = new ConnectionProviderImpl(level);
        networkBuilder.remove(container, connectionProvider);
    }

    @Override
    public void requestNetworkNodeUpdate(final NetworkNodeContainer container, final Level level) {
        final ConnectionProviderImpl connectionProvider = new ConnectionProviderImpl(level);
        networkBuilder.update(container, connectionProvider);
    }

    @Override
    public GridInsertionStrategy createGridInsertionStrategy(final AbstractContainerMenu containerMenu,
                                                             final Player player,
                                                             final Grid grid) {
        return new CompositeGridInsertionStrategy(
            Platform.INSTANCE.getDefaultGridInsertionStrategyFactory().create(
                containerMenu,
                player,
                grid
            ),
            gridInsertionStrategyFactories.stream().map(f -> f.create(
                containerMenu,
                player,
                grid
            )).toList()
        );
    }

    @Override
    public void addGridInsertionStrategyFactory(final GridInsertionStrategyFactory insertionStrategyFactory) {
        gridInsertionStrategyFactories.add(insertionStrategyFactory);
    }

    @Override
    public void addAlternativeGridInsertionHint(final GridInsertionHint hint) {
        gridInsertionHints.addAlternativeHint(hint);
    }

    @Override
    public GridInsertionHints getGridInsertionHints() {
        return gridInsertionHints;
    }

    @Override
    public GridExtractionStrategy createGridExtractionStrategy(final AbstractContainerMenu containerMenu,
                                                               final Player player,
                                                               final Grid grid) {
        final List<GridExtractionStrategy> strategies = gridExtractionStrategyFactories
            .stream()
            .map(f -> f.create(containerMenu, player, grid))
            .toList();
        return new CompositeGridExtractionStrategy(strategies);
    }

    @Override
    public void addGridExtractionStrategyFactory(final GridExtractionStrategyFactory extractionStrategyFactory) {
        gridExtractionStrategyFactories.add(extractionStrategyFactory);
    }

    @Override
    public GridScrollingStrategy createGridScrollingStrategy(final AbstractContainerMenu containerMenu,
                                                             final Player player,
                                                             final Grid grid) {
        final List<GridScrollingStrategy> strategies = gridScrollingStrategyFactories
            .stream()
            .map(f -> f.create(containerMenu, player, grid))
            .toList();
        return new CompositeGridScrollingStrategy(strategies);
    }

    @Override
    public void addGridScrollingStrategyFactory(final GridScrollingStrategyFactory scrollingStrategyFactory) {
        gridScrollingStrategyFactories.add(scrollingStrategyFactory);
    }

    @Override
    public <T> void addResourceFactory(final ResourceFactory<T> factory) {
        resourceFactories.add(factory);
    }

    @Override
    public ResourceFactory<ItemResource> getItemResourceFactory() {
        return itemResourceFactory;
    }

    @Override
    public PlatformStorageChannelType<ItemResource> getItemStorageChannelType() {
        return StorageChannelTypes.ITEM;
    }

    @Override
    public StorageType<ItemResource> getItemStorageType() {
        return StorageTypes.ITEM;
    }

    @Override
    public ResourceFactory<FluidResource> getFluidResourceFactory() {
        return fluidResourceFactory;
    }

    @Override
    public PlatformStorageChannelType<FluidResource> getFluidStorageChannelType() {
        return StorageChannelTypes.FLUID;
    }

    @Override
    public StorageType<FluidResource> getFluidStorageType() {
        return StorageTypes.FLUID;
    }

    @Override
    public Set<ResourceFactory<?>> getAlternativeResourceFactories() {
        return resourceFactories;
    }

    @Override
    public <T> void registerResourceRendering(final Class<T> resourceClass, final ResourceRendering<T> rendering) {
        resourceRenderingMap.put(resourceClass, rendering);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ResourceRendering<T> getResourceRendering(final T resource) {
        return (ResourceRendering<T>) resourceRenderingMap.get(resource.getClass());
    }

    @Override
    public void registerIngredientConverter(final IngredientConverter converter) {
        this.compositeConverter.addConverter(converter);
    }

    @Override
    public IngredientConverter getIngredientConverter() {
        return compositeConverter;
    }

    @Override
    public void addWirelessTransmitterRangeModifier(final WirelessTransmitterRangeModifier rangeModifier) {
        wirelessTransmitterRangeModifier.addModifier(rangeModifier);
    }

    @Override
    public WirelessTransmitterRangeModifier getWirelessTransmitterRangeModifier() {
        return wirelessTransmitterRangeModifier;
    }

    @Override
    public Optional<EnergyStorage> getEnergyStorage(final ItemStack stack) {
        return Platform.INSTANCE.getEnergyStorage(stack);
    }

    @Override
    public EnergyItemHelper getEnergyItemHelper() {
        return energyItemHelper;
    }

    @Override
    public EnergyStorage asItemEnergyStorage(final EnergyStorage energyStorage,
                                             final ItemStack stack) {
        return new ItemEnergyStorage(stack, energyStorage);
    }

    @Override
    public NetworkBoundItemHelper getNetworkBoundItemHelper() {
        return networkBoundItemHelper;
    }

    @Override
    public PlatformRegistry<SlotReferenceFactory> getSlotReferenceFactoryRegistry() {
        return slotReferenceFactoryRegistry;
    }

    @Override
    public void writeSlotReference(final SlotReference slotReference, final FriendlyByteBuf buf) {
        this.slotReferenceFactoryRegistry.getId(slotReference.getFactory()).ifPresentOrElse(id -> {
            buf.writeBoolean(true);
            buf.writeResourceLocation(id);
            slotReference.writeToBuffer(buf);
        }, () -> buf.writeBoolean(false));
    }

    @Override
    public Optional<SlotReference> getSlotReference(final FriendlyByteBuf buf) {
        if (!buf.readBoolean()) {
            return Optional.empty();
        }
        final ResourceLocation id = buf.readResourceLocation();
        return slotReferenceFactoryRegistry.get(id).map(factory -> factory.create(buf));
    }

    @Override
    public void addSlotReferenceProvider(final SlotReferenceProvider provider) {
        slotReferenceProvider.addProvider(provider);
    }

    @Override
    public SlotReference createInventorySlotReference(final Player player, final InteractionHand hand) {
        return InventorySlotReference.of(player, hand);
    }

    @Override
    public void useNetworkBoundItem(final Player player, final Item... items) {
        final Set<Item> validItems = new HashSet<>(Arrays.asList(items));
        slotReferenceProvider.findForUse(player, items[0], validItems).ifPresent(
            slotReference -> Platform.INSTANCE.getClientToServerCommunications().sendUseNetworkBoundItem(slotReference)
        );
    }
}
