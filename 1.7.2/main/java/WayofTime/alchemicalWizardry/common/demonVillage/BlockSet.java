package WayofTime.alchemicalWizardry.common.demonVillage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.Int3;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class BlockSet 
{
	private String blockid;
	private int[] metadata;
	private List<Int3> positions;
	
	public BlockSet()
	{
		this(Blocks.stone);
	}
	
	public BlockSet(String blockid)
	{
		this.blockid = blockid;
		this.metadata = new int[4];
		positions = new ArrayList();
	}
	
	public BlockSet(Block block)
	{
		this(BlockSet.getPairedIdForBlock(block));
	}
	
	public BlockSet(Block block, int meta)
	{
		this(block);
		for(int i=0; i<metadata.length; i++)
		{
			metadata[i] = meta;
		}
		if(block instanceof BlockStairs)
		{
			int[] northSet = new int[]{2,3,0,1};
			int[] eastSet = new int[]{1,0,2,3};
			int[] southSet = new int[]{3,2,1,0};
			int[] westSet = new int[]{0,1,3,2};
			int[] northUpSet = new int[]{6,7,4,5};
			int[] eastUpSet = new int[]{5,4,6,7};
			int[] southUpSet = new int[]{7,6,5,4};
			int[] westUpSet = new int[]{4,5,7,6};
			
			switch(meta)
			{
			case 0:
				metadata = westSet;
				break;
			case 1:
				metadata = eastSet;
				break;
			case 2:
				metadata = northSet;
				break;
			case 3:
				metadata = southSet;
				break;
			case 4:
				metadata = westUpSet;
				break;
			case 5:
				metadata = eastUpSet;
				break;
			case 6:
				metadata = northUpSet;
				break;
			case 7:
				metadata = southUpSet;
				break;
			}
		}
	}
	
	public List<Int3> getPositions()
	{
		return positions;
	}
	
	public void addPositionToBlock(int xOffset, int yOffset, int zOffset)
	{
		positions.add(new Int3(xOffset, yOffset, zOffset));
	}
	
	public Block getBlock()
	{
		return this.getBlockForString(blockid);
	}
	
	public static String getPairedIdForBlock(Block block)
	{
		UniqueIdentifier un = GameRegistry.findUniqueIdentifierFor(block);
		String name = "";
		
		if(un != null)
		{
			name = un.modId + ":" + un.name;
		}
		
		return name;
	}
	
	public static Block getBlockForString(String str)
	{
		String[] parts = str.split(":");
		String modId = parts[0];
		String name = parts[1];
		return GameRegistry.findBlock(modId, name);
	}
	
	public int getMetaForDirection(ForgeDirection dir)
	{
		if(metadata.length < 4)
		{
			return 0;
		}
			
		switch(dir)
		{
		case NORTH:
			return metadata[0];
		case SOUTH:
			return metadata[1];
		case WEST:
			return metadata[2];
		case EAST:
			return metadata[3];
		default:
			return 0;
		}
	}
	
	public void buildAtIndex(World world, int xCoord, int yCoord, int zCoord, ForgeDirection dir, int index)
	{
		Block block = this.getBlock();
		if(index >= positions.size() || block == null)
		{
			return;
		}
		
		Int3 position = positions.get(index);
		int xOff = position.xCoord;
		int yOff = position.yCoord;
		int zOff = position.zCoord;
		int meta = this.getMetaForDirection(dir);
		
		switch(dir)
		{
		case NORTH:
			break;
		case SOUTH:
			xOff *= -1;
			zOff *= -1;
			break;
		case WEST:
			int temp = zOff;
			zOff = xOff * -1;
			xOff = temp;
			break;
		case EAST:
			int temp2 = zOff * -1;
			zOff = xOff;
			xOff = temp2;
			break;
		default:
		}
		
		world.setBlock(xCoord + xOff, yCoord + yOff, zCoord + zOff, block, meta, 3);
	}
	
	public void buildAll(World world, int xCoord, int yCoord, int zCoord, ForgeDirection dir)
	{
		for(int i=0; i<positions.size(); i++)
		{
			this.buildAtIndex(world, xCoord, yCoord, zCoord, dir, i);
		}
	}
	
	public boolean isContained(Block block, int defaultMeta)
	{
		Block thisBlock = this.getBlock();
		return thisBlock == null ? false : thisBlock.equals(block) && this.metadata[0] == defaultMeta;
	}
}