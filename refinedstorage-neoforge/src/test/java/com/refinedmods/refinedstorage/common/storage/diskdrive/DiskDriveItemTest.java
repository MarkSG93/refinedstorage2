package com.refinedmods.refinedstorage.common.storage.diskdrive;

import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.resource.filter.FilterMode;
import com.refinedmods.refinedstorage.api.storage.AccessMode;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;

import java.util.Set;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import static com.refinedmods.refinedstorage.common.GameTestUtil.asResource;
import static com.refinedmods.refinedstorage.common.GameTestUtil.extract;
import static com.refinedmods.refinedstorage.common.GameTestUtil.getItemAsDamaged;
import static com.refinedmods.refinedstorage.common.GameTestUtil.insert;
import static com.refinedmods.refinedstorage.common.GameTestUtil.networkIsAvailable;
import static com.refinedmods.refinedstorage.common.GameTestUtil.storageContainsExactly;
import static com.refinedmods.refinedstorage.common.storage.diskdrive.DiskDriveTestPlots.preparePlot;
import static net.minecraft.world.item.Items.DIAMOND_CHESTPLATE;
import static net.minecraft.world.item.Items.DIRT;
import static net.minecraft.world.item.Items.STONE;

@GameTestHolder(IdentifierUtil.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DiskDriveItemTest {
    private DiskDriveItemTest() {
    }

    @GameTest(template = "empty_15x15")
    public static void shouldInsertItemsDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> insert(helper, network, STONE, 2)));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 2)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, STONE, 62);
                    insert(helper, network, DIRT, 12);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 64),
                    new ResourceAmount(asResource(DIRT), 12)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldInsertItemAllowlistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> insert(helper, network, STONE, 64)));

            // Act
            diskDrive.setFilterMode(FilterMode.ALLOW);
            diskDrive.setFilters(Set.of(asResource(STONE)));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 64)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, STONE, 64);
                    insert(helper, network, DIRT, 12, false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 128)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldInsertItemFuzzyAllowlistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> {
            }));

            // Act
            final ItemStack damagedDiamondChestplate = getItemAsDamaged(DIAMOND_CHESTPLATE.getDefaultInstance(), 500);

            diskDrive.setFuzzyMode(true);
            diskDrive.setFilterMode(FilterMode.ALLOW);
            diskDrive.setFilters(Set.of(asResource(DIAMOND_CHESTPLATE.getDefaultInstance())));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(helper, pos))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, STONE, 12, false);
                    insert(helper, network, DIAMOND_CHESTPLATE, 1);
                    insert(helper, network, asResource(damagedDiamondChestplate), 1);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(DIAMOND_CHESTPLATE), 1),
                    new ResourceAmount(asResource(damagedDiamondChestplate), 1)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldInsertItemBlocklistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> insert(helper, network, STONE, 64)));

            // Act
            diskDrive.setFilterMode(FilterMode.BLOCK);
            diskDrive.setFilters(Set.of(asResource(DIRT)));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 64)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, STONE, 64);
                    insert(helper, network, DIRT, 12, false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 128)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldInsertItemFuzzyBlocklistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> {
            }));

            // Act
            final ItemStack damagedDiamondChestplate = getItemAsDamaged(DIAMOND_CHESTPLATE.getDefaultInstance(), 500);

            diskDrive.setFuzzyMode(true);
            diskDrive.setFilterMode(FilterMode.BLOCK);
            diskDrive.setFilters(Set.of(asResource(DIAMOND_CHESTPLATE.getDefaultInstance())));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(helper, pos))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, STONE, 12);
                    insert(helper, network, DIAMOND_CHESTPLATE, 1, false);
                    insert(helper, network, asResource(damagedDiamondChestplate), 1, false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 12)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldExtractItemsDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> {
                insert(helper, network, STONE, 64);
                insert(helper, network, DIRT, 12);
            }));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 64),
                    new ResourceAmount(asResource(DIRT), 12)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    extract(helper, network, STONE, 62);
                    extract(helper, network, DIRT, 12);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 2)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldExtractItemAllowlistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> insert(helper, network, STONE, 64)));

            // Act
            diskDrive.setFilterMode(FilterMode.ALLOW);
            diskDrive.setFilters(Set.of(asResource(STONE)));

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 64)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    extract(helper, network, STONE, 32);
                    extract(helper, network, DIRT, 12, false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 32)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldExtractItemBlocklistDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> insert(helper, network, STONE, 64)));

            // Act
            diskDrive.setFilterMode(FilterMode.BLOCK);

            // Assert
            sequence
                .thenExecute(() -> diskDrive.setFilters(Set.of(asResource(DIRT))))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 64)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    extract(helper, network, STONE, 32);
                    extract(helper, network, DIRT, 12, false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 32)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldVoidItemsDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> insert(helper, network, STONE, 1024)));

            // Act
            diskDrive.setFilterMode(FilterMode.ALLOW);
            diskDrive.setFilters(Set.of(asResource(STONE)));
            diskDrive.setVoidExcess(true);

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 1024)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, STONE, 2);
                    insert(helper, network, DIRT, 2, false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 1024)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldRespectItemInsertAccessModeDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> insert(helper, network, STONE, 64)));

            // Act
            diskDrive.setAccessMode(AccessMode.INSERT);

            // Assert
            sequence
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 64)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, STONE, 64);
                    insert(helper, network, DIRT, 12);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 128),
                    new ResourceAmount(asResource(DIRT), 12)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> extract(helper, network, STONE, 32, false)))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 128),
                    new ResourceAmount(asResource(DIRT), 12)
                ))
                .thenSucceed();
        });
    }

    @GameTest(template = "empty_15x15")
    public static void shouldRespectItemExtractAccessModeDiskDrive(final GameTestHelper helper) {
        preparePlot(helper, true, (diskDrive, pos, sequence) -> {
            // Arrange
            sequence.thenWaitUntil(networkIsAvailable(helper, pos, network -> insert(helper, network, STONE, 64)));

            // Assert
            sequence
                .thenExecute(() -> diskDrive.setAccessMode(AccessMode.EXTRACT))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 64)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> {
                    insert(helper, network, STONE, 64, false);
                    insert(helper, network, DIRT, 12, false);
                }))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 64)
                ))
                .thenExecute(networkIsAvailable(helper, pos, network -> extract(helper, network, STONE, 32)))
                .thenWaitUntil(storageContainsExactly(
                    helper,
                    pos,
                    new ResourceAmount(asResource(STONE), 32)
                ))
                .thenSucceed();
        });
    }
}
