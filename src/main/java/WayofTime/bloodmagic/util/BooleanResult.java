package WayofTime.bloodmagic.util;

public class BooleanResult<T> {

    private final boolean result;
    private final T value;

    private BooleanResult(boolean result, T value) {
        this.result = result;
        this.value = value;
    }

    public boolean isSuccess() {
        return result;
    }

    public T getValue() {
        return value;
    }

    public static <T> BooleanResult<T> newResult(boolean success, T value) {
        return new BooleanResult<>(success, value);
    }
}
