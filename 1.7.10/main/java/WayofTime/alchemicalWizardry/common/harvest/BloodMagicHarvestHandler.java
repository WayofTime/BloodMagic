package WayofTime.alchemicalWizardry.common.harvest;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;

public class BloodMagicHarvestHandler implements IHarvestHandler
{
	public boolean canHandleBlock(Block block) 
	{
		return block == Blocks.wheat || block == Blocks.carrots || block == Blocks.potatoes || block == Blocks.nether_wart;
	}

	public int getHarvestMeta(Block block) 
	{
		if(block == Blocks.wheat)
		{
			return 7;
		}
		if(block == Blocks.carrots)
		{
			return 7;
		}
		if(block == Blocks.potatoes)
		{
			return 7;
		}
		if(block == Blocks.nether_wart)
		{
			return 3;
		}
		return 7;
	}

	@Override
	public boolean harvestAndPlant(World world, int xCoord, int yCoord, int zCoord, Block block, int meta) 
	{
		if(!this.canHandleBlock(block) || meta != this.getHarvestMeta(block))
		{
			return false;
		}
		
		IPlantable seed = this.getSeedItem(block);
		
		if(seed == null)
		{
			return false;
		}
		
		int fortune = 0;
		
		List<ItemStack> list = block.getDrops(world, xCoord, yCoord, zCoord, meta, fortune);
		boolean foundAndRemovedSeed = false;
		
		for(ItemStack stack : list)
		{
			if(stack == null)
			{
				continue;
			}
			
			Item item = stack.getItem();
			if(item == seed)
			{
				int itemSize = stack.stackSize;
				if(itemSize > 1)
				{
					stack.stackSize--;
					foundAndRemovedSeed = true;
					break;
				}else if(itemSize == 1)
				{
					list.remove(stack);
					foundAndRemovedSeed = true;
					break;
				}
			}
		}
		
		if(foundAndRemovedSeed)
		{
			int plantMeta = seed.getPlantMetadata(world, xCoord, yCoord, zCoord);
			Block plantBlock = seed.getPlant(world, xCoord, yCoord, zCoord);
			
			world.setBlock(xCoord, yCoord, zCoord, plantBlock, plantMeta, 3);
			
			for(ItemStack stack : list)
			{
				EntityItem itemEnt = new EntityItem(world, xCoord, yCoord, zCoord, stack); 
				
				world.spawnEntityInWorld(itemEnt);
			}
		}
		
		return false;
	}

	public IPlantable getSeedItem(Block block) 
	{
		if(block == Blocks.wheat)
		{
			return (IPlantable) Items.wheat_seeds;
		}
		if(block == Blocks.carrots)
		{
			return (IPlantable) Items.carrot;
		}
		if(block == Blocks.potatoes)
		{
			return (IPlantable) Items.potato;
		}
		if(block == Blocks.nether_wart)
		{
			return (IPlantable) Items.nether_wart;
		}
		
		return null;
	}
}