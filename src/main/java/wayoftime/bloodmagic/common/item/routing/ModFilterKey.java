package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFilterKey implements IFilterKey
{
	private String namespace;
	private int count;

	public ModFilterKey(String namespace, int count)
	{
		this.namespace = namespace;
		this.count = count;
	}

	@Override
	public boolean doesStackMatch(ItemStack testStack)
	{
		return !testStack.isEmpty() && ForgeRegistries.ITEMS.getKey(testStack.getItem()).getNamespace().equals(namespace);
	}

	@Override
	public int getCount()
	{
		return count;
	}

	@Override
	public void setCount(int count)
	{
		this.count = count;
	}

	@Override
	public void shrink(int changeAmount)
	{
		this.count -= changeAmount;
	}

	@Override
	public void grow(int changeAmount)
	{
		this.count += changeAmount;
	}

	@Override
	public boolean isEmpty()
	{
		return count == 0;
	}
}
