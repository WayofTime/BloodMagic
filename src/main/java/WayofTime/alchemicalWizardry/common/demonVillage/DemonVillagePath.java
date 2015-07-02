package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.IRoadWard;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class DemonVillagePath
{
    public int xPos;
    public int yPos;
    public int zPos;
    public ForgeDirection dir;
    public int length;
    
    public static boolean canGoDown = true;
    public static boolean tunnelIfObstructed = false;
    public static boolean createBridgeInAirIfObstructed = false;

    public DemonVillagePath(int xi, int yi, int zi, ForgeDirection dir, int length)
    {
        this.xPos = xi;
        this.yPos = yi;
        this.zPos = zi;
        this.dir = dir;
        this.length = length;
    }

    public Int3AndBool constructFullPath(TEDemonPortal portal, World world, int clearance)
    {
        int xi = this.xPos;
        int yi = this.yPos;
        int zi = this.zPos;
        int rad = this.getRoadRadius();
        int value = 0;

        int finalYPos = this.constructPartialPath(portal, world, clearance, xi - rad * dir.offsetX, yi, zi - rad * dir.offsetZ, dir, length + rad, false);

        for (int i = -rad; i <= rad; i++)
        {
            value = Math.max(this.constructPartialPath(portal, world, clearance, xi - rad * dir.offsetX + i * dir.offsetZ, yi, zi - rad * dir.offsetZ + i * dir.offsetX, dir, length + 2 * rad, true), value);
            if(TEDemonPortal.printDebug)
            System.out.println("" + (length + 2 * rad) + ", " + value + "");
        }

        Int3 position = new Int3(xi, finalYPos, zi);

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
     * @return				length if doConstruct, yi if !doConstruct
     */
    public int constructPartialPath(TEDemonPortal portal, World world, int clearance, int xi, int yi, int zi, ForgeDirection dir, int length, boolean doConstruct)
    {
        for (int i = 0; i < length; i++)
        {
            int xOffset = i * dir.offsetX;
            int zOffset = i * dir.offsetZ;

            boolean completed = false;

            for (int yOffset = 0; yOffset <= clearance; yOffset++)
            {
                int sign = 1;

                Block block1 = world.getBlock(xi + xOffset, yi + sign * yOffset, zi + zOffset);
                Block highBlock1 = world.getBlock(xi + xOffset, yi + sign * yOffset + 1, zi + zOffset);

                if ((this.forceReplaceBlock(block1))||(!block1.isReplaceable(world, xi + xOffset, yi + sign * yOffset, zi + zOffset) && this.isBlockReplaceable(block1) ) && (this.forceCanTunnelUnder(highBlock1) || highBlock1.isReplaceable(world, xi + xOffset, yi + sign * yOffset + 1, zi + zOffset)))
                {
                	if(doConstruct)
                    {
                		world.setBlock(xi + xOffset, yi + sign * yOffset, zi + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                    }
                    yi += sign * yOffset;
                    completed = true;
                    break;
                } else if(canGoDown)
                {
                    sign = -1;
                    Block block2 = world.getBlock(xi + xOffset, yi + sign * yOffset, zi + zOffset);
                    Block highBlock2 = world.getBlock(xi + xOffset, yi + sign * yOffset + 1, zi + zOffset);

                    if ((this.forceReplaceBlock(block2))||(!block2.isReplaceable(world, xi + xOffset, yi + sign * yOffset, zi + zOffset) && this.isBlockReplaceable(block2)) && (this.forceCanTunnelUnder(highBlock2) || highBlock2.isReplaceable(world, xi + xOffset, yi + sign * yOffset + 1, zi + zOffset)))
                    {
                        if(doConstruct)
                        {
                        	world.setBlock(xi + xOffset, yi + sign * yOffset, zi + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                        }
                        yi += sign * yOffset;
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
            		Block block1 = world.getBlock(xi + xOffset, yi, zi + zOffset);

                    if (block1.isReplaceable(world, xi + xOffset, yi, zi + zOffset) || !this.isBlockReplaceable(block1) || !this.forceReplaceBlock(block1))
                    {
                		returnAmount = false;

                    	if(doConstruct)
                        {
                			world.setBlock(xi + xOffset, yi, zi + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                        }
                    }else
                    {
                    	returnAmount = true;
                    }

            	}else if(tunnelIfObstructed)
            	{
            		Block block1 = world.getBlock(xi + xOffset, yi, zi + zOffset);

            		if (!block1.isReplaceable(world, xi + xOffset, yi, zi + zOffset) || this.isBlockReplaceable(block1) || !this.forceReplaceBlock(block1))
                    {
                		returnAmount = false;

                    	if(doConstruct)
                        {
                			world.setBlock(xi + xOffset, yi, zi + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                			world.setBlockToAir(xi + xOffset, yi + 1, zi + zOffset);
                			world.setBlockToAir(xi + xOffset, yi + 2, zi + zOffset);
                			world.setBlockToAir(xi + xOffset, yi + 3, zi + zOffset);
                        }
                    }else
                    {
                    	returnAmount = true;
                    }
            	}

            	if(returnAmount)
        		{
                	return doConstruct ? i : yi;
        		}
            }
        }

        return doConstruct ? length : yi;
    }

    public Int3 getFinalLocation(World world, int clearance)
    {
        int xi = xPos;
        int yi = yPos;
        int zi = zPos;

        for (int i = 0; i < length; i++)
        {
            int xOffset = i * dir.offsetX;
            int zOffset = i * dir.offsetZ;

            for (int yOffset = 0; yOffset <= clearance; yOffset++)
            {
                int sign = 1;

                Block block1 = world.getBlock(xi + xOffset, yi + sign * yOffset, zi + zOffset);
                Block highBlock1 = world.getBlock(xi + xOffset, yi + sign * yOffset + 1, zi + zOffset);

                if ((this.forceReplaceBlock(block1))||(!block1.isReplaceable(world, xi + xOffset, yi + sign * yOffset, zi + zOffset) && this.isBlockReplaceable(block1) ) && (this.forceCanTunnelUnder(highBlock1) || highBlock1.isReplaceable(world, xi + xOffset, yi + sign * yOffset + 1, zi + zOffset)))
                {
                    yi += sign * yOffset;
                    break;
                } else
                {
                    sign = -1;
                    Block block2 = world.getBlock(xi + xOffset, yi + sign * yOffset, zi + zOffset);
                    Block highBlock2 = world.getBlock(xi + xOffset, yi + sign * yOffset + 1, zi + zOffset);

                    if ((this.forceReplaceBlock(block2))||(!block2.isReplaceable(world, xi + xOffset, yi + sign * yOffset, zi + zOffset) && this.isBlockReplaceable(block2) ) && (this.forceCanTunnelUnder(highBlock2) || highBlock2.isReplaceable(world, xi + xOffset, yi + sign * yOffset + 1, zi + zOffset)))
                    {
                        yi += sign * yOffset;
                        break;
                    }
                }
            }
        }

        return new Int3(xi, yi, zi);
    }

    public int getRoadRadius()
    {
        return 1;
    }

    public boolean isBlockReplaceable(Block block)
    {
        return !(block.getMaterial() == Material.leaves || block.getMaterial() == Material.vine || block instanceof IRoadWard) &&!block.equals(ModBlocks.blockDemonPortal);
    }
    
    public boolean forceReplaceBlock(Block block)
    {
    	return block.getMaterial().isLiquid();
    }
    
    public boolean forceCanTunnelUnder(Block block)
    {
    	return block instanceof BlockFlower || block == Blocks.double_plant;
    }
}
