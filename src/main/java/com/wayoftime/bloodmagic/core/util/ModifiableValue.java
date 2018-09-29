package com.wayoftime.bloodmagic.core.util;

import java.util.Objects;

public class ModifiableValue<T> {

    private T value;

    public ModifiableValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModifiableValue)) return false;

        ModifiableValue<?> that = (ModifiableValue<?>) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }
}
