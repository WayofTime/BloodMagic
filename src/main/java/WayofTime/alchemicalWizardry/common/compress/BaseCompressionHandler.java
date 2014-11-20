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
		int remaining = this.getRemainingNeeded(inv);
		if(remaining <= 0)
		{
			this.drainInventory(inv);
			return this.getResultStack();
		}
		
		return null;
	}
	
	public int getRemainingNeeded(ItemStack[] inv)
	{
		return iterateThroughInventory(inv, false);
	}
	
	public int drainInventory(ItemStack[] inv)
	{
		return iterateThroughInventory(inv, true);
	}
	
	public int iterateThroughInventory(ItemStack[] inv, boolean doDrain)
	{
		int needed = this.required.stackSize;
		int kept = this.getLeftover();
		int i = -1;
		
		for(ItemStack invStack : inv)
		{
			i++;
			
			if(invStack == null)
			{
				continue;
			}
			
			if(invStack.isItemEqual(this.required) && (invStack.getTagCompound() == null ? this.required.getTagCompound() == null : invStack.getTagCompound().equals(this.required.getTagCompound())))
			{
				int stackSize = invStack.stackSize;
				int used = 0;
				if(kept > 0)
				{
					int remainingFromStack = Math.max(stackSize - kept, 0);
					used += stackSize - remainingFromStack;
				}
				
				kept -= used;
				
				if(kept <= 0 && needed > 0)
				{
					int remainingFromStack = Math.max(stackSize - used - needed, 0);
					needed -= (stackSize - used - remainingFromStack);
					if(doDrain)
					{
						invStack.stackSize = remainingFromStack;
						if(invStack.stackSize <= 0)
						{
							inv[i] = null;
						}
					}
				}
				
				if(needed <= 0)
				{
					return 0;
				}
			}
		}
		
		return needed;
	}
	
	public int getLeftover()
	{
		return this.leftover;
	}
}
