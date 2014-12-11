package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.IRoadWard;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class DemonVillagePath
{
    public int xi;
    public int yi;
    public int zi;
    public ForgeDirection dir;
    public int length;
    
    public static boolean canGoDown = true;
    public static boolean tunnelIfObstructed = false;
    public static boolean createBridgeInAirIfObstructed = false;

    public DemonVillagePath(int xi, int yi, int zi, ForgeDirection dir, int length)
    {
        this.xi = xi;
        this.yi = yi;
        this.zi = zi;
        this.dir = dir;
        this.length = length;
    }

    public Int3AndBool constructFullPath(TEDemonPortal portal, World world, int clearance)
    {
        int xPos = this.xi;
        int yPos = this.yi;
        int zPos = this.zi;
        int rad = this.getRoadRadius();
        int value = 0;
        
        int finalYPos = this.constructPartialPath(portal, world, clearance, xPos - rad * dir.offsetX, yPos, zPos - rad * dir.offsetZ, dir, length + rad, false);

        for (int i = -rad; i <= rad; i++)
        {
            value = Math.max(this.constructPartialPath(portal, world, clearance, xPos - rad * dir.offsetX + i * dir.offsetZ, yPos, zPos - rad * dir.offsetZ + i * dir.offsetX, dir, length + 2 * rad, true), value);
            if(TEDemonPortal.printDebug)
            System.out.println("" + (length + 2 * rad) + ", " + value + "");
        }
        
        Int3 position = new Int3(xPos, finalYPos, zPos);

        boolean bool = value >= length + 2 * rad;

        return new Int3AndBool(position, bool);
    }
    
    public class Int3AndBool
    {
    	public Int3 coords;
    	public boolean bool;
    	public Int3AndBool(Int3 int3, boolean bool)
    	{
    		this.coords = int3;
    		this.bool = bool;
    	}
    }

    /**
     * 
     * @param portal
     * @param world
     * @param clearance
     * @param xi
     * @param yi
     * @param zi
     * @param dir
     * @param length
     * @param doConstruct
     * @return				length if doConstruct, yPos if !doConstruct
     */
    public int constructPartialPath(TEDemonPortal portal, World world, int clearance, int xi, int yi, int zi, ForgeDirection dir, int length, boolean doConstruct)
    {
        int xPos = xi;
        int yPos = yi;
        int zPos = zi;

        for (int i = 0; i < length; i++)
        {
            int xOffset = i * dir.offsetX;
            int zOffset = i * dir.offsetZ;

            boolean completed = false;
            
            for (int yOffset = 0; yOffset <= clearance; yOffset++)
            {
                int sign = 1;

                Block block1 = world.getBlock(xPos + xOffset, yPos + sign * yOffset, zPos + zOffset);
                Block highBlock1 = world.getBlock(xPos + xOffset, yPos + sign * yOffset + 1, zPos + zOffset);

                if ((this.forceReplaceBlock(block1))||(!block1.isReplaceable(world, xPos + xOffset, yPos + sign * yOffset, zPos + zOffset) && this.isBlockReplaceable(block1) ) && (this.forceCanTunnelUnder(highBlock1) || highBlock1.isReplaceable(world, xPos + xOffset, yPos + sign * yOffset + 1, zPos + zOffset)))
                {
                	if(doConstruct)
                    {
                		world.setBlock(xPos + xOffset, yPos + sign * yOffset, zPos + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                    }
                    yPos += sign * yOffset;
                    completed = true;
                    break;
                } else if(canGoDown)
                {
                    sign = -1;
                    Block block2 = world.getBlock(xPos + xOffset, yPos + sign * yOffset, zPos + zOffset);
                    Block highBlock2 = world.getBlock(xPos + xOffset, yPos + sign * yOffset + 1, zPos + zOffset);

                    if ((this.forceReplaceBlock(block2))||(!block2.isReplaceable(world, xPos + xOffset, yPos + sign * yOffset, zPos + zOffset) && this.isBlockReplaceable(block2)) && (this.forceCanTunnelUnder(highBlock2) || highBlock2.isReplaceable(world, xPos + xOffset, yPos + sign * yOffset + 1, zPos + zOffset)))
                    {
                        if(doConstruct)
                        {
                        	world.setBlock(xPos + xOffset, yPos + sign * yOffset, zPos + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                        }
                        yPos += sign * yOffset;
                        completed = true;
                        break;
                    }
                }
            }
            
            if(!completed)
            {
            	boolean returnAmount = true;
            	if(createBridgeInAirIfObstructed)
            	{
            		Block block1 = world.getBlock(xPos + xOffset, yPos, zPos + zOffset);
                    
                    if (block1.isReplaceable(world, xPos + xOffset, yPos, zPos + zOffset) || !this.isBlockReplaceable(block1) || !this.forceReplaceBlock(block1))
                    {
                		returnAmount = false;

                    	if(doConstruct)
                        {
                			world.setBlock(xPos + xOffset, yPos, zPos + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                        }
                    }else
                    {
                    	returnAmount = true;
                    }
            		
            	}else if(this.tunnelIfObstructed)
            	{
            		Block block1 = world.getBlock(xPos + xOffset, yPos, zPos + zOffset);

            		if (!block1.isReplaceable(world, xPos + xOffset, yPos, zPos + zOffset) || this.isBlockReplaceable(block1) || !this.forceReplaceBlock(block1))
                    {
                		returnAmount = false;

                    	if(doConstruct)
                        {
                			world.setBlock(xPos + xOffset, yPos, zPos + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                			world.setBlockToAir(xPos + xOffset, yPos + 1, zPos + zOffset);
                			world.setBlockToAir(xPos + xOffset, yPos + 2, zPos + zOffset);
                			world.setBlockToAir(xPos + xOffset, yPos + 3, zPos + zOffset);
                        }
                    }else
                    {
                    	returnAmount = true;
                    }
            	}
            	
            	if(returnAmount)
        		{
                	return doConstruct ? i : yPos;
        		}
            }
        }
        
        return doConstruct ? length : yPos;
    }

    public Int3 getFinalLocation(World world, int clearance)
    {
        int xPos = xi;
        int yPos = yi;
        int zPos = zi;

        for (int i = 0; i < length; i++)
        {
            int xOffset = i * dir.offsetX;
            int zOffset = i * dir.offsetZ;

            for (int yOffset = 0; yOffset <= clearance; yOffset++)
            {
                int sign = 1;

                Block block1 = world.getBlock(xPos + xOffset, yPos + sign * yOffset, zPos + zOffset);
                Block highBlock1 = world.getBlock(xPos + xOffset, yPos + sign * yOffset + 1, zPos + zOffset);

                if ((this.forceReplaceBlock(block1))||(!block1.isReplaceable(world, xPos + xOffset, yPos + sign * yOffset, zPos + zOffset) && this.isBlockReplaceable(block1) ) && (this.forceCanTunnelUnder(highBlock1) || highBlock1.isReplaceable(world, xPos + xOffset, yPos + sign * yOffset + 1, zPos + zOffset)))
                {
                    yPos += sign * yOffset;
                    break;
                } else
                {
                    sign = -1;
                    Block block2 = world.getBlock(xPos + xOffset, yPos + sign * yOffset, zPos + zOffset);
                    Block highBlock2 = world.getBlock(xPos + xOffset, yPos + sign * yOffset + 1, zPos + zOffset);

                    if ((this.forceReplaceBlock(block2))||(!block2.isReplaceable(world, xPos + xOffset, yPos + sign * yOffset, zPos + zOffset) && this.isBlockReplaceable(block2) ) && (this.forceCanTunnelUnder(highBlock2) || highBlock2.isReplaceable(world, xPos + xOffset, yPos + sign * yOffset + 1, zPos + zOffset)))
                    {
                        yPos += sign * yOffset;
                        break;
                    }
                }
            }
        }

        return new Int3(xPos, yPos, zPos);
    }

    public int getRoadRadius()
    {
        return 1;
    }

    public boolean isBlockReplaceable(Block block)
    {
        if (block.getMaterial() == Material.leaves || block.getMaterial() == Material.vine || block instanceof IRoadWard)
        {
            return false;
        }
        return !block.equals(ModBlocks.blockDemonPortal);
    }
    
    public boolean forceReplaceBlock(Block block)
    {
    	if(block.getMaterial().isLiquid())
    	{
    		return true;
    	}else
    	{
    		return false;
    	}
    }
    
    public boolean forceCanTunnelUnder(Block block)
    {
    	if(block instanceof BlockFlower || block == Blocks.double_plant)
    	{
    		return true;
    	}
    	
    	return false;
    }
}
