package wayoftime.bloodmagic.common.item.routing;

public interface IFilterKey<T>
{
	public T getType();

	public boolean doesStackMatch(T testStack);

	public int getCount();

	public void setCount(int count);

	public void grow(int changeAmount);

	public boolean isEmpty();

	public void shrink(int changeAmount);
}
