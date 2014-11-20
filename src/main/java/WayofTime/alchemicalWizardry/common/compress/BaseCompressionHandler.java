package WayofTime.alchemicalWizardry.common.compress;

import net.minecraft.item.ItemStack;
import WayofTime.alchemicalWizardry.api.compress.CompressionHandler;

public class BaseCompressionHandler extends CompressionHandler
{
	private final ItemStack required;
	private final ItemStack result;
	private final int leftover;
	
	public BaseCompressionHandler(ItemStack requested, ItemStack result, int leftover)
	{
		super();
		this.required = requested;
		this.result = result;
		this.leftover = leftover;
	}
	
	@Override
	public ItemStack getResultStack() 
	{
		return this.result.copy();
	}

	@Override
	public ItemStack getRequiredStack() 
	{
		return this.required.copy();
	}

	@Override
	public ItemStack compressInventory(ItemStack[] inv) 
	{
		int needed = this.required.stackSize;
		int kept = this.getLeftover();
		
		for(ItemStack invStack : inv)
		{
			if(invStack == null)
			{
				continue;
			}
			
			if(invStack.equals(invStack))
			{
				
			}
		}
		
		return null;
	}
	
	public int getLeftover()
	{
		return this.leftover;
	}
}
