package com.refinedmods.refinedstorage.common.support;

public class RedstoneModeSettings {
    private static final int IGNORE = 0;
    private static final int HIGH = 1;
    private static final int LOW = 2;

    private RedstoneModeSettings() {
    }

    public static RedstoneMode getRedstoneMode(final int redstoneMode) {
        return switch (redstoneMode) {
            case HIGH -> RedstoneMode.HIGH;
            case LOW -> RedstoneMode.LOW;
            default -> RedstoneMode.IGNORE;
        };
    }

    public static int getRedstoneMode(final RedstoneMode redstoneMode) {
        return switch (redstoneMode) {
            case IGNORE -> IGNORE;
            case HIGH -> HIGH;
            case LOW -> LOW;
        };
    }
}
