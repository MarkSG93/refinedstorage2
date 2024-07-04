package com.refinedmods.refinedstorage.platform.common.grid.strategy;

import com.refinedmods.refinedstorage.api.grid.operations.GridInsertMode;
import com.refinedmods.refinedstorage.platform.api.grid.strategy.GridInsertionStrategy;

import java.util.Collections;
import java.util.List;

public class CompositeGridInsertionStrategy implements GridInsertionStrategy {
    private final GridInsertionStrategy defaultStrategy;
    private final List<GridInsertionStrategy> alternativeStrategies;

    public CompositeGridInsertionStrategy(final GridInsertionStrategy defaultStrategy,
                                          final List<GridInsertionStrategy> alternativeStrategies) {
        this.defaultStrategy = defaultStrategy;
        this.alternativeStrategies = Collections.unmodifiableList(alternativeStrategies);
    }

    @Override
    public boolean onInsert(final GridInsertMode insertMode, final boolean tryAlternatives) {
        if (tryAlternatives) {
            for (final GridInsertionStrategy alternativeHandler : alternativeStrategies) {
                if (alternativeHandler.onInsert(insertMode, true)) {
                    return true;
                }
            }
        }
        return defaultStrategy.onInsert(insertMode, tryAlternatives);
    }

    @Override
    public boolean onTransfer(final int slotIndex) {
        return defaultStrategy.onTransfer(slotIndex);
    }
}
