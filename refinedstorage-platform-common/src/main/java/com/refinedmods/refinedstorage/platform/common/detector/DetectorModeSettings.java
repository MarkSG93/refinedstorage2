package com.refinedmods.refinedstorage.platform.common.detector;

import com.refinedmods.refinedstorage.api.network.impl.node.detector.DetectorMode;

final class DetectorModeSettings {
    private static final int UNDER = 0;
    private static final int EQUAL = 1;
    private static final int ABOVE = 2;

    private DetectorModeSettings() {
    }

    static DetectorMode getDetectorMode(final int detectorMode) {
        return switch (detectorMode) {
            case UNDER -> DetectorMode.UNDER;
            case ABOVE -> DetectorMode.ABOVE;
            default -> DetectorMode.EQUAL;
        };
    }

    static int getDetectorMode(final DetectorMode detectorMode) {
        return switch (detectorMode) {
            case UNDER -> UNDER;
            case EQUAL -> EQUAL;
            case ABOVE -> ABOVE;
        };
    }
}
