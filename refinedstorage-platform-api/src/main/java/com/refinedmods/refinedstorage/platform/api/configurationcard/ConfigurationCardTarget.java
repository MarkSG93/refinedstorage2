package com.refinedmods.refinedstorage.platform.api.configurationcard;

import java.util.Collections;
import java.util.List;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import org.apiguardian.api.API;

/**
 * Implement this on a block entity that supports the configuration card.
 */
@API(status = API.Status.STABLE, since = "2.0.0-milestone.3.2")
public interface ConfigurationCardTarget {
    void writeConfiguration(CompoundTag tag, HolderLookup.Provider provider);

    void readConfiguration(CompoundTag tag, HolderLookup.Provider provider);

    default List<Item> getUpgradeItems() {
        return Collections.emptyList();
    }

    default boolean addUpgradeItem(Item upgradeItem) {
        return false;
    }
}
