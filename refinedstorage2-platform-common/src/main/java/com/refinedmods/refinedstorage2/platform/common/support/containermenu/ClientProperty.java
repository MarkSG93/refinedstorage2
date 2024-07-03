package com.refinedmods.refinedstorage2.platform.common.support.containermenu;

import com.refinedmods.refinedstorage2.platform.common.support.packet.c2s.C2SPackets;

import net.minecraft.world.inventory.DataSlot;

public class ClientProperty<T> extends DataSlot implements Property<T> {
    private final PropertyType<T> type;
    private T value;
    private int rawValue;

    public ClientProperty(final PropertyType<T> type, final T value) {
        this.type = type;
        this.rawValue = type.serializer().apply(value);
        this.value = value;
    }

    @Override
    public PropertyType<T> getType() {
        return type;
    }

    @Override
    public T getValue() {
        return value;
    }

    public void setValue(final T newValue) {
        C2SPackets.sendPropertyChange(type, newValue);
    }

    @Override
    public DataSlot getDataSlot() {
        return this;
    }

    @Override
    public int get() {
        return rawValue;
    }

    @Override
    public void set(final int newValue) {
        this.value = type.deserializer().apply(newValue);
        this.rawValue = newValue;
        onChangedOnClient(value);
    }

    protected void onChangedOnClient(final T newValue) {
        // do nothing by default
    }
}
