package WayofTime.alchemicalWizardry.common.compress;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.compress.CompressionHandler;
import WayofTime.alchemicalWizardry.api.compress.CompressionRegistry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class AdvancedCompressionHandler extends CompressionHandler
{
	@Override
	public ItemStack compressInventory(ItemStack[] inv, World world) 
	{		
		return test(inv, true, world);
	}
	
	public ItemStack test(ItemStack[] inv, boolean doDrain, World world)
	{
		for(ItemStack invStack : inv)
		{
			if(invStack == null)
			{
				continue;
			}
			
			int threshold = CompressionRegistry.getItemThreshold(invStack);
			
			for(int i=2; i<=3; i++)
			{
				ItemStack stacky = getRecipe(invStack, world, i);
				if(isResultStackReversible(stacky, i, world))
				{
					int needed = i*i;
					int neededLeft = iterateThroughInventory(invStack, threshold, inv, needed, false);
					if(neededLeft <= 0)
					{
						iterateThroughInventory(invStack, 0, inv, needed, true);
						return stacky;
					}
				}
			}	
		}
		
		return null;
	}
	
	public int iterateThroughInventory(ItemStack required, int kept, ItemStack[] inv, int needed, boolean doDrain)
	{
		int i = -1;
		
		for(ItemStack invStack : inv)
		{
			i++;
			
			if(invStack == null)
			{
				continue;
			}
			
			if(invStack.isItemEqual(required) && (invStack.getTagCompound() == null ? required.getTagCompound() == null : invStack.getTagCompound().equals(required.getTagCompound())))
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
					if(doDrain)
					{
						invStack.stackSize = remainingFromStack + used;
						if(invStack.stackSize <= 0)
						{
							inv[i] = null;
						}
					}
					
					needed -= (stackSize - used - remainingFromStack);
				}
				
				if(needed <= 0)
				{
					return 0;
				}
			}
		}
		
		return needed;
	}
	
	public boolean isResultStackReversible(ItemStack stack, int gridSize, World world)
	{
		if(stack == null)
		{
			return false;
		}
		InventoryCrafting inventory = new InventoryCrafting(new Container()
	    {
	        public boolean canInteractWith(EntityPlayer player)
	        {
	            return false;
	        }
	    }, 2, 2);
		
		inventory.setInventorySlotContents(0, stack);

		ItemStack returnStack = CraftingManager.getInstance().findMatchingRecipe(inventory, world);
		if(returnStack == null)
		{
			return false;
		}
				
		ItemStack compressedStack = null;
		switch(gridSize)
		{
		case 2:
			compressedStack = get22Recipe(returnStack, world);
			break;
		case 3:
			compressedStack = get33Recipe(returnStack, world);
			break;
		}
		
		if(compressedStack == null)
		{
			return false;
		}else
		{
			return SpellHelper.areItemStacksEqual(stack, compressedStack);
		}
	}
	
	public ItemStack getRecipe(ItemStack stack, World world, int gridSize)
	{
		InventoryCrafting inventory = new InventoryCrafting(new Container()
	    {
	        public boolean canInteractWith(EntityPlayer player)
	        {
	            return false;
	        }
	    }, gridSize, gridSize);
		for(int i=0; i<inventory.getSizeInventory(); i++)
		{
			inventory.setInventorySlotContents(i, stack);
		}
		
		return CraftingManager.getInstance().findMatchingRecipe(inventory, world);
	}
	
	public boolean has22Recipe(ItemStack stack, World world)
	{
		return get22Recipe(stack, world) != null;
	}
	
	public ItemStack get22Recipe(ItemStack stack, World world)
	{
		return getRecipe(stack, world, 2);
	}
	
	public boolean has33Recipe(ItemStack stack, World world)
	{
		return get22Recipe(stack, world) != null;
	}
	
	public ItemStack get33Recipe(ItemStack stack, World world)
	{
		return getRecipe(stack, world, 3);
	}
}
