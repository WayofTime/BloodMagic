package WayofTime.alchemicalWizardry.api.harvest;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public interface IHarvestHandler 
{	
	/**
	 * A handler that is used to harvest and replant the block at the specified location
	 * 
	 * @param world
	 * @param xCoord
	 * @param yCoord
	 * @param zCoord
	 * @param block block at this given location
	 * @param meta meta at this given location
	 * @return true if successfully harvested, false if not
	 */
	public boolean harvestAndPlant(World world, int xCoord, int yCoord, int zCoord, Block block, int meta);
}
