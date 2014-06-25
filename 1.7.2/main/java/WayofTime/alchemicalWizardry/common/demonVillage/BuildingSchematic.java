package WayofTime.alchemicalWizardry.common.demonVillage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.Int3;

public class BuildingSchematic 
{
	public String name;
	public int doorX;
	public int doorZ;
	public List<BlockSet> blockList;
	
	public BuildingSchematic()
	{
		this("");
	}
	
	public BuildingSchematic(String name)
	{
		this.name = name;
		blockList = new ArrayList();
		this.doorX = 0;
		this.doorZ = 0;
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
	
	public void buildAll(World world, int xCoord, int yCoord, int zCoord, ForgeDirection dir)
	{
		for(BlockSet set : blockList)
		{
			set.buildAll(world, xCoord, yCoord, zCoord, dir);
		}
	}
	
	public GridSpaceHolder createGSH()
	{
		GridSpaceHolder holder = new GridSpaceHolder();
		
		for(BlockSet set : blockList)
		{
			for(Int3 coords : set.getPositions())
			{
				int gridX = (int)((coords.xCoord+2*Math.signum(coords.xCoord))/5);
				int gridZ = (int)((coords.zCoord+2*Math.signum(coords.zCoord))/5);
								
				holder.setGridSpace(gridX, gridZ, new GridSpace(GridSpace.HOUSE,0));
			}
		}
		
		return holder;
	}
	
	public Int3 getGridSpotOfDoor()
	{
		int gridX = (int)((doorX+2*Math.signum(doorX))/5);
		int gridZ = (int)((doorZ+2*Math.signum(doorZ))/5);
		
		return new Int3(gridX, 0, gridZ);
	}
}