package com.refinedmods.refinedstorage.common.constructordestructor;

import com.refinedmods.refinedstorage.common.api.constructordestructor.ConstructorStrategy;
import com.refinedmods.refinedstorage.common.api.constructordestructor.ConstructorStrategyFactory;
import com.refinedmods.refinedstorage.common.api.upgrade.UpgradeState;
import com.refinedmods.refinedstorage.common.content.Items;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;

public class ItemDropConstructorStrategyFactory implements ConstructorStrategyFactory {
    @Override
    public Optional<ConstructorStrategy> create(final ServerLevel level,
                                                final BlockPos pos,
                                                final Direction direction,
                                                final UpgradeState upgradeState,
                                                final boolean dropItems) {
        if (!dropItems) {
            return Optional.empty();
        }
        return Optional.of(new ItemDropConstructorStrategy(
            level,
            pos,
            direction,
            upgradeState.has(Items.INSTANCE.getStackUpgrade())
        ));
    }

    @Override
    public int getPriority() {
        return -1;
    }
}
