package com.refinedmods.refinedstorage.common.support.widget;

import java.util.List;

public class History {
    private final List<String> items;
    private int index = -1;

    public History(final List<String> items) {
        this.items = items;
    }

    public String older() {
        if (items.isEmpty()) {
            return "";
        }
        if (index == -1) {
            index = items.size() - 1;
        } else {
            index--;
            if (index < 0) {
                index = 0;
            }
        }
        return items.get(index);
    }

    public String newer() {
        if (index == -1) {
            return "";
        }
        index++;
        if (index == items.size()) {
            index = -1;
            return "";
        }
        return items.get(index);
    }

    public boolean save(final String value) {
        if (value.trim().isEmpty()) {
            return false;
        }
        if (!items.isEmpty() && items.getLast().equals(value)) {
            return false;
        }
        items.add(value);
        return true;
    }
}
