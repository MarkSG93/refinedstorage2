package com.refinedmods.refinedstorage.common.support.resource.list;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.resource.list.ResourceListImpl;
import com.refinedmods.refinedstorage.common.SetupMinecraft;
import com.refinedmods.refinedstorage.common.api.support.resource.list.FuzzyResourceList;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.Collection;
import java.util.Optional;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SetupMinecraft.class)
class FuzzyResourceListImplTest {
    private static final ItemResource DUMMY_A = new ItemResource(Items.DIRT, DataComponentPatch.EMPTY);
    private static final ItemResource DUMMY_B = new ItemResource(Items.DIRT, DataComponentPatch.builder()
        .set(DataComponents.BASE_COLOR, DyeColor.RED).build());
    private static final ItemResource DUMMY_C = new ItemResource(Items.DIRT, DataComponentPatch.builder()
        .set(DataComponents.BASE_COLOR, DyeColor.GREEN).build());
    private static final ItemResource DUMMY_D = new ItemResource(Items.GLASS, DataComponentPatch.EMPTY);
    private static final ItemResource DUMMY_E = new ItemResource(Items.DARK_OAK_DOOR, DataComponentPatch.EMPTY);

    FuzzyResourceList sut;

    @BeforeEach
    void setUp() {
        sut = new FuzzyResourceListImpl(ResourceListImpl.create());
    }

    @Test
    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    void testRetrievingFuzzy() {
        // Arrange
        sut.add(DUMMY_A, 5);
        sut.add(DUMMY_A, 5);
        sut.remove(DUMMY_A, 9);

        sut.add(DUMMY_B, 15);

        sut.add(DUMMY_C, 20);

        sut.add(DUMMY_D, 10);
        sut.add(DUMMY_D, 15);

        // Act
        final Optional<ResourceAmount> strictA = sut.get(DUMMY_A);
        final Optional<ResourceAmount> strictB = sut.get(DUMMY_B);
        final Optional<ResourceAmount> strictC = sut.get(DUMMY_C);
        final Optional<ResourceAmount> strictD = sut.get(DUMMY_D);
        final Optional<ResourceAmount> strictE = sut.get(DUMMY_E);

        final Collection<ResourceKey> fuzzyA = sut.getFuzzy(DUMMY_A);
        final Collection<ResourceKey> fuzzyB = sut.getFuzzy(DUMMY_B);
        final Collection<ResourceKey> fuzzyC = sut.getFuzzy(DUMMY_C);
        final Collection<ResourceKey> fuzzyD = sut.getFuzzy(DUMMY_D);
        final Collection<ResourceKey> fuzzyE = sut.getFuzzy(DUMMY_E);

        // Assert
        assertThat(strictA).get().usingRecursiveComparison().isEqualTo(new ResourceAmount(DUMMY_A, 1));
        assertThat(strictB).get().usingRecursiveComparison().isEqualTo(new ResourceAmount(DUMMY_B, 15));
        assertThat(strictC).get().usingRecursiveComparison().isEqualTo(new ResourceAmount(DUMMY_C, 20));
        assertThat(strictD).get().usingRecursiveComparison().isEqualTo(new ResourceAmount(DUMMY_D, 25));
        assertThat(strictE).isNotPresent();

        assertThat(fuzzyA).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            DUMMY_A,
            DUMMY_B,
            DUMMY_C
        );
        assertThat(fuzzyB).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            DUMMY_A,
            DUMMY_B,
            DUMMY_C
        );
        assertThat(fuzzyC).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            DUMMY_A,
            DUMMY_B,
            DUMMY_C
        );
        assertThat(fuzzyD).usingRecursiveFieldByFieldElementComparator().containsExactly(DUMMY_D);
        assertThat(fuzzyE).isEmpty();
    }

    @Test
    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    void shouldRemoveEntireResourceFromFuzzyIndexAfterRemoval() {
        // Arrange
        sut.add(DUMMY_A, 5);
        sut.add(DUMMY_A, 5);
        sut.remove(DUMMY_A, 10);

        sut.add(DUMMY_B, 15);
        sut.remove(DUMMY_B, 15);

        sut.add(DUMMY_C, 20);
        sut.remove(DUMMY_C, 20);

        sut.add(DUMMY_D, 10);
        sut.add(DUMMY_D, 15);

        // Act
        final Optional<ResourceAmount> strictA = sut.get(DUMMY_A);
        final Optional<ResourceAmount> strictB = sut.get(DUMMY_B);
        final Optional<ResourceAmount> strictC = sut.get(DUMMY_C);
        final Optional<ResourceAmount> strictD = sut.get(DUMMY_D);
        final Optional<ResourceAmount> strictE = sut.get(DUMMY_E);

        final Collection<ResourceKey> fuzzyA = sut.getFuzzy(DUMMY_A);
        final Collection<ResourceKey> fuzzyB = sut.getFuzzy(DUMMY_B);
        final Collection<ResourceKey> fuzzyC = sut.getFuzzy(DUMMY_C);
        final Collection<ResourceKey> fuzzyD = sut.getFuzzy(DUMMY_D);
        final Collection<ResourceKey> fuzzyE = sut.getFuzzy(DUMMY_E);

        // Assert
        assertThat(strictA).isNotPresent();
        assertThat(strictB).isNotPresent();
        assertThat(strictC).isNotPresent();
        assertThat(strictD).get().usingRecursiveComparison().isEqualTo(new ResourceAmount(DUMMY_D, 25));
        assertThat(strictE).isNotPresent();

        assertThat(fuzzyA).isEmpty();
        assertThat(fuzzyB).isEmpty();
        assertThat(fuzzyC).isEmpty();
        assertThat(fuzzyD).usingRecursiveFieldByFieldElementComparator().containsExactly(DUMMY_D);
        assertThat(fuzzyE).isEmpty();
    }

    @Test
    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    void shouldRemoveSingleResourceFromFuzzyIndexAfterRemoval() {
        // Arrange
        sut.add(DUMMY_A, 5);
        sut.add(DUMMY_A, 5);
        sut.remove(DUMMY_A, 10);

        sut.add(DUMMY_B, 15);

        sut.add(DUMMY_C, 20);

        sut.add(DUMMY_D, 10);
        sut.add(DUMMY_D, 15);

        // Act
        final Optional<ResourceAmount> strictA = sut.get(DUMMY_A);
        final Optional<ResourceAmount> strictB = sut.get(DUMMY_B);
        final Optional<ResourceAmount> strictC = sut.get(DUMMY_C);
        final Optional<ResourceAmount> strictD = sut.get(DUMMY_D);
        final Optional<ResourceAmount> strictE = sut.get(DUMMY_E);

        final Collection<ResourceKey> fuzzyA = sut.getFuzzy(DUMMY_A);
        final Collection<ResourceKey> fuzzyB = sut.getFuzzy(DUMMY_B);
        final Collection<ResourceKey> fuzzyC = sut.getFuzzy(DUMMY_C);
        final Collection<ResourceKey> fuzzyD = sut.getFuzzy(DUMMY_D);
        final Collection<ResourceKey> fuzzyE = sut.getFuzzy(DUMMY_E);

        // Assert
        assertThat(strictA).isNotPresent();
        assertThat(strictB).get().usingRecursiveComparison().isEqualTo(new ResourceAmount(DUMMY_B, 15));
        assertThat(strictC).get().usingRecursiveComparison().isEqualTo(new ResourceAmount(DUMMY_C, 20));
        assertThat(strictD).get().usingRecursiveComparison().isEqualTo(new ResourceAmount(DUMMY_D, 25));
        assertThat(strictE).isNotPresent();

        assertThat(fuzzyA).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            DUMMY_B,
            DUMMY_C
        );
        assertThat(fuzzyB).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            DUMMY_B,
            DUMMY_C
        );
        assertThat(fuzzyC).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(
            DUMMY_B,
            DUMMY_C
        );
        assertThat(fuzzyD).usingRecursiveFieldByFieldElementComparator().containsExactly(DUMMY_D);
        assertThat(fuzzyE).isEmpty();
    }
}
