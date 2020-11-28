package wayoftime.bloodmagic.core.util;

public final class Value<T>
{
	private T value;

	private Value(T t)
	{
		this.value = t;
	}

	public T get()
	{
		return value;
	}

	public Value<T> set(T t)
	{
		this.value = t;
		return this;
	}

	public static <T> Value<T> of(T t)
	{
		return new Value<>(t);
	}
}