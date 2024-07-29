package com.refinedmods.refinedstorage.common.grid;

import com.refinedmods.refinedstorage.api.grid.operations.GridExtractMode;
import com.refinedmods.refinedstorage.api.grid.operations.GridInsertMode;
import com.refinedmods.refinedstorage.api.grid.query.GridQueryParserException;
import com.refinedmods.refinedstorage.api.grid.query.GridQueryParserImpl;
import com.refinedmods.refinedstorage.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.api.grid.view.GridSortingDirection;
import com.refinedmods.refinedstorage.api.grid.view.GridView;
import com.refinedmods.refinedstorage.api.grid.view.GridViewBuilder;
import com.refinedmods.refinedstorage.api.grid.view.GridViewBuilderImpl;
import com.refinedmods.refinedstorage.api.grid.watcher.GridWatcher;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.tracked.TrackedResource;
import com.refinedmods.refinedstorage.common.Config;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.grid.Grid;
import com.refinedmods.refinedstorage.common.api.grid.GridResourceAttributeKeys;
import com.refinedmods.refinedstorage.common.api.grid.GridScrollMode;
import com.refinedmods.refinedstorage.common.api.grid.GridSynchronizer;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridExtractionStrategy;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridInsertionStrategy;
import com.refinedmods.refinedstorage.common.api.grid.strategy.GridScrollingStrategy;
import com.refinedmods.refinedstorage.common.api.storage.PlayerActor;
import com.refinedmods.refinedstorage.common.api.support.registry.PlatformRegistry;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;
import com.refinedmods.refinedstorage.common.grid.strategy.ClientGridExtractionStrategy;
import com.refinedmods.refinedstorage.common.grid.strategy.ClientGridInsertionStrategy;
import com.refinedmods.refinedstorage.common.grid.strategy.ClientGridScrollingStrategy;
import com.refinedmods.refinedstorage.common.grid.view.CompositeGridResourceFactory;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.packet.s2c.S2CPackets;
import com.refinedmods.refinedstorage.common.support.resource.ResourceTypes;
import com.refinedmods.refinedstorage.common.support.stretching.ScreenSizeListener;
import com.refinedmods.refinedstorage.query.lexer.LexerTokenMappings;
import com.refinedmods.refinedstorage.query.parser.ParserOperatorMappings;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public abstract class AbstractGridContainerMenu extends AbstractBaseContainerMenu
    implements GridWatcher, GridInsertionStrategy, GridExtractionStrategy, GridScrollingStrategy, ScreenSizeListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGridContainerMenu.class);
    private static final GridQueryParserImpl QUERY_PARSER = new GridQueryParserImpl(
        LexerTokenMappings.DEFAULT_MAPPINGS,
        ParserOperatorMappings.DEFAULT_MAPPINGS,
        Map.of(
            "@", Set.of(GridResourceAttributeKeys.MOD_ID, GridResourceAttributeKeys.MOD_NAME),
            "$", Set.of(GridResourceAttributeKeys.TAGS),
            "#", Set.of(GridResourceAttributeKeys.TOOLTIP)
        )
    );

    private static String lastSearchQuery = "";

    protected final Inventory playerInventory;

    private final GridView view;
    @Nullable
    private Grid grid;
    @Nullable
    private GridInsertionStrategy insertionStrategy;
    @Nullable
    private GridExtractionStrategy extractionStrategy;
    @Nullable
    private GridScrollingStrategy scrollingStrategy;
    private GridSynchronizer synchronizer;
    @Nullable
    private ResourceType resourceTypeFilter;
    private boolean autoSelected;
    private boolean active;
    @Nullable
    private GridSearchBox searchBox;

    protected AbstractGridContainerMenu(
        final MenuType<? extends AbstractGridContainerMenu> menuType,
        final int syncId,
        final Inventory playerInventory,
        final GridData gridData
    ) {
        super(menuType, syncId);

        this.playerInventory = playerInventory;

        this.active = gridData.active();

        final GridViewBuilder viewBuilder = createViewBuilder();
        gridData.resources().forEach(gridResource -> viewBuilder.withResource(
            gridResource.resourceAmount().getResource(),
            gridResource.resourceAmount().getAmount(),
            gridResource.trackedResource().orElse(null)
        ));

        this.view = viewBuilder.build();
        this.view.setSortingDirection(Platform.INSTANCE.getConfig().getGrid().getSortingDirection());
        this.view.setSortingType(Platform.INSTANCE.getConfig().getGrid().getSortingType());
        this.view.setFilterAndSort(filterResourceType());

        this.synchronizer = loadSynchronizer();
        this.resourceTypeFilter = loadResourceType();
        this.insertionStrategy = new ClientGridInsertionStrategy();
        this.extractionStrategy = new ClientGridExtractionStrategy();
        this.scrollingStrategy = new ClientGridScrollingStrategy();
        this.autoSelected = loadAutoSelected();
    }

    protected AbstractGridContainerMenu(
        final MenuType<? extends AbstractGridContainerMenu> menuType,
        final int syncId,
        final Inventory playerInventory,
        final Grid grid
    ) {
        super(menuType, syncId);

        this.view = createViewBuilder().build();

        this.playerInventory = playerInventory;
        this.grid = grid;
        this.grid.addWatcher(this, PlayerActor.class);

        this.synchronizer = NoopGridSynchronizer.INSTANCE;
        initStrategies((ServerPlayer) playerInventory.player);
    }

    private Predicate<GridResource> filterResourceType() {
        return gridResource -> Platform.INSTANCE.getConfig().getGrid().getResourceType().flatMap(resourceTypeId ->
            RefinedStorageApi.INSTANCE
                .getResourceTypeRegistry()
                .get(resourceTypeId)
                .map(type -> type.isGridResourceBelonging(gridResource))
        ).orElse(true);
    }

    private static GridViewBuilder createViewBuilder() {
        return new GridViewBuilderImpl(
            new CompositeGridResourceFactory(RefinedStorageApi.INSTANCE.getResourceTypeRegistry()),
            GridSortingTypes.NAME,
            GridSortingTypes.QUANTITY
        );
    }

    public void onResourceUpdate(final ResourceKey resource,
                                 final long amount,
                                 @Nullable final TrackedResource trackedResource) {
        LOGGER.debug("{} got updated with {}", resource, amount);
        view.onChange(resource, amount, trackedResource);
    }

    public GridSortingDirection getSortingDirection() {
        return Platform.INSTANCE.getConfig().getGrid().getSortingDirection();
    }

    public void setSortingDirection(final GridSortingDirection sortingDirection) {
        Platform.INSTANCE.getConfig().getGrid().setSortingDirection(sortingDirection);
        view.setSortingDirection(sortingDirection);
        view.sort();
    }

    public GridSortingTypes getSortingType() {
        return Platform.INSTANCE.getConfig().getGrid().getSortingType();
    }

    public void setSortingType(final GridSortingTypes sortingType) {
        Platform.INSTANCE.getConfig().getGrid().setSortingType(sortingType);
        view.setSortingType(sortingType);
        view.sort();
    }

    public void setSearchBox(final GridSearchBox searchBox) {
        this.searchBox = searchBox;
        registerViewUpdatingListener(searchBox);
        configureSearchBox(searchBox);
    }

    private void registerViewUpdatingListener(final GridSearchBox theSearchBox) {
        theSearchBox.addListener(text -> {
            final boolean valid = onSearchTextChanged(text);
            theSearchBox.setValid(valid);
        });
    }

    private boolean onSearchTextChanged(final String text) {
        try {
            view.setFilterAndSort(QUERY_PARSER.parse(text).and(filterResourceType()));
            return true;
        } catch (GridQueryParserException e) {
            view.setFilterAndSort(resource -> false);
            return false;
        }
    }

    private void configureSearchBox(final GridSearchBox theSearchBox) {
        theSearchBox.setAutoSelected(isAutoSelected());
        if (Platform.INSTANCE.getConfig().getGrid().isRememberSearchQuery()) {
            theSearchBox.setValue(lastSearchQuery);
            theSearchBox.addListener(AbstractGridContainerMenu::updateLastSearchQuery);
        }
    }

    private static void updateLastSearchQuery(final String text) {
        lastSearchQuery = text;
    }

    @Override
    public void removed(final Player playerEntity) {
        super.removed(playerEntity);
        if (grid != null) {
            grid.removeWatcher(this);
        }
    }

    @Override
    public void onScreenReady(final int playerInventoryY) {
        resetSlots();
        addPlayerInventory(playerInventory, 8, playerInventoryY);
    }

    public GridView getView() {
        return view;
    }

    @Override
    public void onActiveChanged(final boolean newActive) {
        this.active = newActive;
        if (this.playerInventory.player instanceof ServerPlayer serverPlayerEntity) {
            S2CPackets.sendGridActiveness(serverPlayerEntity, newActive);
        }
    }

    @Override
    public void onChanged(
        final ResourceKey resource,
        final long change,
        @Nullable final TrackedResource trackedResource
    ) {
        if (!(resource instanceof PlatformResourceKey platformResource)) {
            return;
        }
        LOGGER.debug("{} received a change of {} for {}", this, change, resource);
        S2CPackets.sendGridUpdate(
            (ServerPlayer) playerInventory.player,
            platformResource,
            change,
            trackedResource
        );
    }

    @Override
    public void invalidate() {
        if (playerInventory.player instanceof ServerPlayer serverPlayer) {
            initStrategies(serverPlayer);
            S2CPackets.sendGridClear(serverPlayer);
        }
    }

    private void initStrategies(final ServerPlayer player) {
        this.insertionStrategy = RefinedStorageApi.INSTANCE.createGridInsertionStrategy(
            this,
            player,
            requireNonNull(grid)
        );
        this.extractionStrategy = RefinedStorageApi.INSTANCE.createGridExtractionStrategy(
            this,
            player,
            requireNonNull(grid)
        );
        this.scrollingStrategy = RefinedStorageApi.INSTANCE.createGridScrollingStrategy(
            this,
            player,
            requireNonNull(grid)
        );
    }

    public boolean isActive() {
        return active;
    }

    public void setAutoSelected(final boolean autoSelected) {
        this.autoSelected = autoSelected;
        Platform.INSTANCE.getConfig().getGrid().setAutoSelected(autoSelected);
        if (searchBox != null) {
            searchBox.setAutoSelected(autoSelected);
        }
    }

    private boolean loadAutoSelected() {
        return Platform.INSTANCE.getConfig().getGrid().isAutoSelected();
    }

    public boolean isAutoSelected() {
        return autoSelected;
    }

    private GridSynchronizer loadSynchronizer() {
        return Platform.INSTANCE
            .getConfig()
            .getGrid()
            .getSynchronizer()
            .flatMap(id -> RefinedStorageApi.INSTANCE.getGridSynchronizerRegistry().get(id))
            .orElse(NoopGridSynchronizer.INSTANCE);
    }

    @Nullable
    private ResourceType loadResourceType() {
        return Platform.INSTANCE
            .getConfig()
            .getGrid()
            .getResourceType()
            .flatMap(id -> RefinedStorageApi.INSTANCE.getResourceTypeRegistry().get(id))
            .orElse(null);
    }

    public GridSynchronizer getSynchronizer() {
        return synchronizer;
    }

    @Nullable
    public ResourceType getResourceType() {
        return resourceTypeFilter;
    }

    public void toggleSynchronizer() {
        final PlatformRegistry<GridSynchronizer> registry = RefinedStorageApi.INSTANCE.getGridSynchronizerRegistry();
        final Config.GridEntry config = Platform.INSTANCE.getConfig().getGrid();
        final GridSynchronizer newSynchronizer = registry.nextOrNullIfLast(getSynchronizer());
        if (newSynchronizer == null) {
            config.clearSynchronizer();
        } else {
            registry.getId(newSynchronizer).ifPresent(config::setSynchronizer);
        }
        this.synchronizer = newSynchronizer == null ? NoopGridSynchronizer.INSTANCE : newSynchronizer;
    }

    public void toggleResourceType() {
        final PlatformRegistry<ResourceType> registry = RefinedStorageApi.INSTANCE.getResourceTypeRegistry();
        final Config.GridEntry config = Platform.INSTANCE.getConfig().getGrid();
        final ResourceType newResourceType = resourceTypeFilter == null
            ? ResourceTypes.ITEM
            : registry.nextOrNullIfLast(resourceTypeFilter);
        if (newResourceType == null) {
            config.clearResourceType();
        } else {
            registry.getId(newResourceType).ifPresent(config::setResourceType);
        }
        this.resourceTypeFilter = newResourceType;
        this.view.sort();
    }

    @Override
    public boolean onInsert(final GridInsertMode insertMode, final boolean tryAlternatives) {
        if (grid != null && !grid.isGridActive()) {
            return false;
        }
        if (insertionStrategy == null) {
            return false;
        }
        return insertionStrategy.onInsert(insertMode, tryAlternatives);
    }

    @Override
    public boolean onExtract(final PlatformResourceKey resource,
                             final GridExtractMode extractMode,
                             final boolean cursor) {
        if (grid != null && !grid.isGridActive()) {
            return false;
        }
        if (extractionStrategy == null) {
            return false;
        }
        return extractionStrategy.onExtract(resource, extractMode, cursor);
    }

    @Override
    public boolean onScroll(final PlatformResourceKey resource, final GridScrollMode scrollMode, final int slotIndex) {
        if (grid != null && !grid.isGridActive()) {
            return false;
        }
        if (scrollingStrategy == null) {
            return false;
        }
        return scrollingStrategy.onScroll(resource, scrollMode, slotIndex);
    }

    @Override
    public boolean onTransfer(final int slotIndex) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("resource")
    @Override
    public ItemStack quickMoveStack(final Player playerEntity, final int slotIndex) {
        if (transferManager.transfer(slotIndex)) {
            return ItemStack.EMPTY;
        } else if (!playerEntity.level().isClientSide() && grid != null && grid.isGridActive()) {
            final Slot slot = getSlot(slotIndex);
            if (slot.hasItem() && insertionStrategy != null && canTransferSlot(slot)) {
                insertionStrategy.onTransfer(slot.index);
            }
        }
        return ItemStack.EMPTY;
    }

    protected boolean canTransferSlot(final Slot slot) {
        return true;
    }

    public void onClear() {
        view.clear();
    }
}
