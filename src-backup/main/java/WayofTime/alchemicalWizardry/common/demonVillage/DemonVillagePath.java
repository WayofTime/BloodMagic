package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.Int3;

public class DemonVillagePath 
{
	public int xi;
	public int yi;
	public int zi;
	public ForgeDirection dir;
	public int length;
	
	public DemonVillagePath(int xi, int yi, int zi, ForgeDirection dir, int length)
	{
		this.xi = xi;
		this.yi = yi;
		this.zi = zi;
		this.dir = dir;
		this.length = length;
	}
	
	public Int3 constructFullPath(World world, int clearance, Block block, int meta)
	{
		int xPos = this.xi;
		int yPos = this.yi;
		int zPos = this.zi;
		int rad = this.getRoadRadius();
		
		for(int i=-rad; i<=rad; i++)
		{
			this.constructPartialPath(world, clearance, block, meta, xPos-rad*dir.offsetX+i*dir.offsetZ, yPos, zPos-rad*dir.offsetZ+i*dir.offsetX, dir, length+2*rad);
		}
		
		return this.getFinalLocation(world, clearance);
	}
	
	public void constructPartialPath(World world, int clearance, Block roadBlock, int meta, int xi, int yi, int zi, ForgeDirection dir, int length)
	{
		int xPos = xi;
		int yPos = yi;
		int zPos = zi;
		
		for(int i=0; i<length; i++)
		{
			int xOffset = i*dir.offsetX;
			int zOffset = i*dir.offsetZ;
			
			for(int yOffset=0; yOffset<=clearance; yOffset++)
			{
				int sign = 1;
				
				Block block1 = world.getBlock(xPos + xOffset, yPos + sign*yOffset, zPos + zOffset);
				Block highBlock1 = world.getBlock(xPos + xOffset, yPos + sign*yOffset + 1, zPos + zOffset);
				
				if(!block1.isReplaceable(world, xPos + xOffset, yPos + sign*yOffset, zPos + zOffset) && this.isBlockReplaceable(block1) && highBlock1.isReplaceable(world, xPos + xOffset, yPos + sign*yOffset + 1, zPos + zOffset))
				{
					world.setBlock(xPos + xOffset, yPos + sign*yOffset, zPos + zOffset, roadBlock, meta, 3);
					yPos += sign*yOffset;
					break;
				}else
				{
					sign = -1;
					Block block2 = world.getBlock(xPos + xOffset, yPos + sign*yOffset, zPos + zOffset);
					Block highBlock2 = world.getBlock(xPos + xOffset, yPos + sign*yOffset + 1, zPos + zOffset);
					
					if(!block2.isReplaceable(world, xPos + xOffset, yPos + sign*yOffset, zPos + zOffset) && this.isBlockReplaceable(block1) && highBlock2.isReplaceable(world, xPos + xOffset, yPos + sign*yOffset + 1, zPos + zOffset))
					{
						world.setBlock(xPos + xOffset, yPos + sign*yOffset, zPos + zOffset, roadBlock, meta, 3);
						yPos += sign*yOffset;
						break;
					}
				}
			}
		}
	}
	
	public Int3 getFinalLocation(World world, int clearance)
	{
		int xPos = xi;
		int yPos = yi;
		int zPos = zi;
		
		for(int i=0; i<length; i++)
		{
			int xOffset = i*dir.offsetX;
			int zOffset = i*dir.offsetZ;
			
			for(int yOffset=0; yOffset<=clearance; yOffset++)
			{
				int sign = 1;
				
				Block block1 = world.getBlock(xPos + xOffset, yPos + sign*yOffset, zPos + zOffset);
				Block highBlock1 = world.getBlock(xPos + xOffset, yPos + sign*yOffset + 1, zPos + zOffset);
				
				if(!block1.isReplaceable(world, xPos + xOffset, yPos + sign*yOffset, zPos + zOffset) && this.isBlockReplaceable(block1) && highBlock1.isReplaceable(world, xPos + xOffset, yPos + sign*yOffset + 1, zPos + zOffset))
				{
					yPos += sign*yOffset;
					break;
				}else
				{
					sign = -1;
					Block block2 = world.getBlock(xPos + xOffset, yPos + sign*yOffset, zPos + zOffset);
					Block highBlock2 = world.getBlock(xPos + xOffset, yPos + sign*yOffset + 1, zPos + zOffset);
					
					if(!block2.isReplaceable(world, xPos + xOffset, yPos + sign*yOffset, zPos + zOffset) && this.isBlockReplaceable(block1) && highBlock2.isReplaceable(world, xPos + xOffset, yPos + sign*yOffset + 1, zPos + zOffset))
					{
						yPos += sign*yOffset;
						break;
					}
				}
			}
		}
		
		return new Int3(xPos,yPos,zPos);
	}
	
	public int getRoadRadius()
	{
		return 1;
	}
	
	public boolean isBlockReplaceable(Block block)
	{
		if(block.getMaterial() == Material.leaves || block.getMaterial() == Material.vine)
		{
			return false;
		}
		return !block.equals(ModBlocks.blockDemonPortal);
	}
}
