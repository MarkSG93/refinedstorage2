package com.refinedmods.refinedstorage.common.grid;

import com.refinedmods.refinedstorage.api.grid.view.GridResourceFactory;
import com.refinedmods.refinedstorage.api.grid.view.GridSortingDirection;
import com.refinedmods.refinedstorage.api.grid.view.GridView;
import com.refinedmods.refinedstorage.api.grid.view.GridViewBuilder;
import com.refinedmods.refinedstorage.api.grid.view.GridViewBuilderImpl;
import com.refinedmods.refinedstorage.api.storage.tracked.TrackedResource;
import com.refinedmods.refinedstorage.common.SetupMinecraft;
import com.refinedmods.refinedstorage.common.api.grid.view.PlatformGridResource;
import com.refinedmods.refinedstorage.common.grid.view.AbstractItemGridResourceFactory;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.Comparator;
import java.util.Optional;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SetupMinecraft.class)
class GridSortingTypesTest {
    private static final GridResourceFactory<PlatformGridResource> FACTORY = new AbstractItemGridResourceFactory() {
        @Override
        public String getModId(final ItemStack itemStack) {
            return "";
        }

        @Override
        public Optional<String> getModName(final String modId) {
            return Optional.empty();
        }
    };

    private GridViewBuilder<PlatformGridResource> viewBuilder;
    private ItemResource dirt;
    private ItemResource stone;
    private ItemResource gold;

    @BeforeEach
    void setUp() {
        viewBuilder = new GridViewBuilderImpl<>(
            FACTORY,
            view -> Comparator.comparing(PlatformGridResource::getName),
            view -> Comparator.comparingLong(resource -> resource.getAmount(view))
        );
        dirt = new ItemResource(Items.DIRT, DataComponentPatch.EMPTY);
        stone = new ItemResource(Items.STONE, DataComponentPatch.EMPTY);
        gold = new ItemResource(Items.GOLD_INGOT, DataComponentPatch.EMPTY);
    }

    @ParameterizedTest
    @EnumSource(GridSortingTypes.class)
    void testSortingAscending(final GridSortingTypes sortingType) {
        // Arrange
        final GridView<PlatformGridResource> view = viewBuilder
            .withResource(dirt, 10, null)
            .withResource(dirt, 5, new TrackedResource("Raoul", 3))
            .withResource(stone, 1, new TrackedResource("VdB", 2))
            .withResource(gold, 2, null)
            .build();

        view.setSortingType(sortingType);
        view.setSortingDirection(GridSortingDirection.ASCENDING);

        // Act
        view.sort();

        // Assert
        switch (sortingType) {
            case QUANTITY -> assertThat(view.getViewList())
                .extracting(PlatformGridResource::getName)
                .containsExactly(
                    "Stone",
                    "Gold Ingot",
                    "Dirt"
                );
            case NAME -> assertThat(view.getViewList())
                .extracting(PlatformGridResource::getName)
                .containsExactly(
                    "Dirt",
                    "Gold Ingot",
                    "Stone"
                );
            case ID -> assertThat(view.getViewList())
                .extracting(PlatformGridResource::getName)
                .containsExactly(
                    "Stone",
                    "Dirt",
                    "Gold Ingot"
                );
            case LAST_MODIFIED -> assertThat(view.getViewList())
                .extracting(PlatformGridResource::getName)
                .containsExactly(
                    "Gold Ingot",
                    "Stone",
                    "Dirt"
                );
            default -> fail();
        }
    }

    @ParameterizedTest
    @EnumSource(GridSortingTypes.class)
    void testSortingDescending(final GridSortingTypes sortingType) {
        // Arrange
        final GridView<PlatformGridResource> view = viewBuilder
            .withResource(dirt, 10, null)
            .withResource(dirt, 5, new TrackedResource("Raoul", 3))
            .withResource(stone, 1, new TrackedResource("VDB", 2))
            .withResource(gold, 2, null)
            .build();

        view.setSortingType(sortingType);
        view.setSortingDirection(GridSortingDirection.DESCENDING);

        // Act
        view.sort();

        // Assert
        switch (sortingType) {
            case QUANTITY -> assertThat(view.getViewList())
                .extracting(PlatformGridResource::getName)
                .containsExactly(
                    "Dirt",
                    "Gold Ingot",
                    "Stone"
                );
            case NAME -> assertThat(view.getViewList())
                .extracting(PlatformGridResource::getName)
                .containsExactly(
                    "Stone",
                    "Gold Ingot",
                    "Dirt"
                );
            case ID -> assertThat(view.getViewList())
                .extracting(PlatformGridResource::getName)
                .containsExactly(
                    "Gold Ingot",
                    "Dirt",
                    "Stone"
                );
            case LAST_MODIFIED -> assertThat(view.getViewList())
                .extracting(PlatformGridResource::getName)
                .containsExactly(
                    "Dirt",
                    "Stone",
                    "Gold Ingot"
                );
            default -> fail();
        }
    }
}
