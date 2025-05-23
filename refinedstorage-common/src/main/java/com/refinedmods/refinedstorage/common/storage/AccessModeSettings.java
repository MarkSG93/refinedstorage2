package com.refinedmods.refinedstorage.common.storage;

import com.refinedmods.refinedstorage.api.storage.AccessMode;

public class AccessModeSettings {
    private static final int INSERT_EXTRACT = 0;
    private static final int INSERT = 1;
    private static final int EXTRACT = 2;

    private AccessModeSettings() {
    }

    public static AccessMode getAccessMode(final int accessMode) {
        return switch (accessMode) {
            case INSERT -> AccessMode.INSERT;
            case EXTRACT -> AccessMode.EXTRACT;
            default -> AccessMode.INSERT_EXTRACT;
        };
    }

    public static int getAccessMode(final AccessMode accessMode) {
        return switch (accessMode) {
            case INSERT_EXTRACT -> INSERT_EXTRACT;
            case INSERT -> INSERT;
            case EXTRACT -> EXTRACT;
        };
    }
}
