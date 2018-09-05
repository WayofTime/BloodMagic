package com.wayoftime.bloodmagic.core.util;

public class BooleanResult<T> {

    private final T value;
    private final boolean success;

    public BooleanResult(T value, boolean success) {
        this.value = value;
        this.success = success;
    }

    public T getValue() {
        return value;
    }

    public boolean isSuccess() {
        return success;
    }
}
