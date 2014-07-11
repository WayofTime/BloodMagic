package WayofTime.alchemicalWizardry.api.harvest;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public interface IHarvestHandler 
{
	public boolean canHandleBlock(Block block);
	
	public int getHarvestMeta(Block block);
	
	public boolean harvestAndPlant(World world, int xCoord, int yCoord, int zCoord, Block block, int meta);
	
	public IPlantable getSeedItem(Block block);
}
