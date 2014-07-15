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

public class GourdHarvestHandler implements IHarvestHandler
{
	public boolean canHandleBlock(Block block) 
	{
		return block == Blocks.melon_block || block == Blocks.pumpkin;
	}

	@Override
	public boolean harvestAndPlant(World world, int xCoord, int yCoord, int zCoord, Block block, int meta) 
	{
		if(!this.canHandleBlock(block))
		{
			return false;
		}
		
		int fortune = 0;
		
		List<ItemStack> list = block.getDrops(world, xCoord, yCoord, zCoord, meta, fortune);
		
		world.setBlockToAir(xCoord, yCoord, zCoord);
		
		for(ItemStack stack : list)
		{
			EntityItem itemEnt = new EntityItem(world, xCoord, yCoord, zCoord, stack); 
			
			world.spawnEntityInWorld(itemEnt);
		}

		return true;
	}
}