package WayofTime.alchemicalWizardry.common.demonVillage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BuildingSchematic 
{
	public String name;
	public List<BlockSet> blockList;
	
	public BuildingSchematic()
	{
		this("");
	}
	
	public BuildingSchematic(String name)
	{
		this.name = name;
		blockList = new ArrayList();
	}
	
	public void addBlockWithMeta(Block block, int meta, int xOffset, int yOffset, int zOffset)
	{
		for(BlockSet set : blockList)
		{
			if(set.isContained(block, meta))
			{
				set.addPositionToBlock(xOffset, yOffset, zOffset);
				return;
			}
		}
		
		BlockSet set = new BlockSet(block, meta);
		set.addPositionToBlock(xOffset, yOffset, zOffset);
		blockList.add(set);
	}
	
	public void buildAll(World world, int xCoord, int yCoord, int zCoord)
	{
		
	}
}