package com.refinedmods.refinedstorage.common.storage.diskdrive;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.filter.FilterMode;
import com.refinedmods.refinedstorage.api.storage.AccessMode;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;

import java.util.Set;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import static com.refinedmods.refinedstorage.common.GameTestUtil.asResource;
import static com.refinedmods.refinedstorage.common.GameTestUtil.extract;
import static com.refinedmods.refinedstorage.common.GameTestUtil.insert;
import static com.refinedmods.refinedstorage.common.GameTestUtil.networkIsAvailable;
import static com.refinedmods.refinedstorage.common.GameTestUtil.storageContainsExactly;
import static com.refinedmods.refinedstorage.common.storage.diskdrive.DiskDriveTestPlots.preparePlot;
import static net.minecraft.world.level.material.Fluids.LAVA;
import static net.minecraft.world.level.material.Fluids.WATER;

@GameTestHolder(IdentifierUtil.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DiskDriveFluidTest {
    private DiskDriveFluidTest() {
    }

    @GameTest(template = "empty_15x15")
    public static void shouldInsertFluidsDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, false, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network ->
                insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 2)));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 2)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 14);
                    insert(helper, network, LAVA, Platform.INSTANCE.getBucketAmount() * 16);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 16),
                    new ResourceAmount(asResource(LAVA), Platform.INSTANCE.getBucketAmount() * 16)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldInsertFluidAllowlistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, false, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network ->
                insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16)));

            // Act
            diskDrive.setFilterMode(FilterMode.ALLOW);
            diskDrive.setFilters(Set.of(asResource(WATER)));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 16)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16);
                    insert(helper, network, LAVA, Platform.INSTANCE.getBucketAmount(), false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 32)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldInsertFluidBlocklistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, false, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network ->
                insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16)));

            // Act
            diskDrive.setFilterMode(FilterMode.BLOCK);
            diskDrive.setFilters(Set.of(asResource(LAVA)));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 16)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16);
                    insert(helper, network, LAVA, Platform.INSTANCE.getBucketAmount(), false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 32)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldExtractFluidsDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, false, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> {
                insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16);
                insert(helper, network, LAVA, Platform.INSTANCE.getBucketAmount());
            }));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 16),
                    new ResourceAmount(asResource(LAVA), Platform.INSTANCE.getBucketAmount())
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    extract(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 10);
                    extract(helper, network, LAVA, Platform.INSTANCE.getBucketAmount());
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 6)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldExtractFluidAllowlistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, false, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network ->
                insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16)));

            // Act
            diskDrive.setFilterMode(FilterMode.ALLOW);
            diskDrive.setFilters(Set.of(asResource(WATER)));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 16)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    extract(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 8);
                    extract(helper, network, LAVA, Platform.INSTANCE.getBucketAmount(), false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 8)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldExtractFluidBlocklistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, false, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network ->
                insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16)));

            // Act
            diskDrive.setFilterMode(FilterMode.BLOCK);
            diskDrive.setFilters(Set.of(asResource(LAVA)));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 16)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    extract(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 8);
                    extract(helper, network, LAVA, Platform.INSTANCE.getBucketAmount(), false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 8)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldVoidFluidsDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, false, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network ->
                insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 64)));

            // Act
            diskDrive.setFilterMode(FilterMode.ALLOW);
            diskDrive.setFilters(Set.of(asResource(WATER)));
            diskDrive.setVoidExcess(true);

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 64)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 2);
                    insert(helper, network, LAVA, Platform.INSTANCE.getBucketAmount(), false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 64)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldRespectFluidInsertAccessModeDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, false, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network ->
                insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16)));

            // Act
            diskDrive.setAccessMode(AccessMode.INSERT);

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 16)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16);
                    insert(helper, network, LAVA, Platform.INSTANCE.getBucketAmount());
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 32),
                    new ResourceAmount(asResource(LAVA), Platform.INSTANCE.getBucketAmount())
                ))
                .thenExecute(networkIsAvailable(helper, pos, network ->
                    extract(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16, false)))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 32),
                    new ResourceAmount(asResource(LAVA), Platform.INSTANCE.getBucketAmount())
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldRespectFluidExtractAccessModeDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, false, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network ->
                insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16)));

            // Assert
            sequence
                .thenExecute(() -> diskDrive.setAccessMode(AccessMode.EXTRACT))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 16)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 16, false);
                    insert(helper, network, LAVA, Platform.INSTANCE.getBucketAmount(), false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 16)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network ->
                    extract(helper, network, WATER, Platform.INSTANCE.getBucketAmount() * 8)))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(WATER), Platform.INSTANCE.getBucketAmount() * 8)
                ))
                .thenSucceed();
        });
    }
}
