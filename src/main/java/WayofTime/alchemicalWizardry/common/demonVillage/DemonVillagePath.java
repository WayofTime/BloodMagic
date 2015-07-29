package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.IRoadWard;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class DemonVillagePath
{
    public int xPos;
    public int yPos;
    public int zPos;
    public EnumFacing dir;
    public int length;
    
    public static boolean canGoDown = true;
    public static boolean tunnelIfObstructed = false;
    public static boolean createBridgeInAirIfObstructed = false;

    public DemonVillagePath(int xi, int yi, int zi, EnumFacing dir2, int length)
    {
        this.xPos = xi;
        this.yPos = yi;
        this.zPos = zi;
        this.dir = dir2;
        this.length = length;
    }

    public Int3AndBool constructFullPath(TEDemonPortal portal, World world, int clearance)
    {
        int xi = this.xPos;
        int yi = this.yPos;
        int zi = this.zPos;
        int rad = this.getRoadRadius();
        int value = 0;

        int finalYPos = this.constructPartialPath(portal, world, clearance, xi - rad * dir.getFrontOffsetX(), yi, zi - rad * dir.getFrontOffsetZ(), dir, length + rad, false);

        for (int i = -rad; i <= rad; i++)
        {
            value = Math.max(this.constructPartialPath(portal, world, clearance, xi - rad * dir.getFrontOffsetX() + i * dir.getFrontOffsetZ(), yi, zi - rad * dir.getFrontOffsetZ() + i * dir.getFrontOffsetX(), dir, length + 2 * rad, true), value);
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
    public int constructPartialPath(TEDemonPortal portal, World world, int clearance, int xi, int yi, int zi, EnumFacing dir, int length, boolean doConstruct)
    {
        for (int i = 0; i < length; i++)
        {
            int xOffset = i * dir.getFrontOffsetX();
            int zOffset = i * dir.getFrontOffsetZ();

            boolean completed = false;

            for (int yOffset = 0; yOffset <= clearance; yOffset++)
            {
                int sign = 1;

                BlockPos pos1 = new BlockPos(xi + xOffset, yi + sign * yOffset, zi + zOffset);
                BlockPos highPos1 = pos1.offsetUp();
                
                IBlockState state1 = world.getBlockState(pos1);
                IBlockState highState1 = world.getBlockState(highPos1);
                Block block1 = state1.getBlock();
                Block highBlock1 = highState1.getBlock();

                if ((this.forceReplaceBlock(block1))||(!block1.isReplaceable(world, pos1) && this.isBlockReplaceable(block1) ) && (this.forceCanTunnelUnder(highBlock1) || highBlock1.isReplaceable(world, highPos1)))
                {
                	if(doConstruct)
                    {
                		world.setBlockState(pos1, portal.getRoadState(), 3);
                    }
                    yi += sign * yOffset;
                    completed = true;
                    break;
                } else if(canGoDown)
                {
                    sign = -1;
                    pos1 = new BlockPos(xi + xOffset, yi + sign * yOffset, zi + zOffset);
                    highPos1 = pos1.offsetUp();
                    
                    state1 = world.getBlockState(pos1);
                    highState1 = world.getBlockState(highPos1);
                    block1 = state1.getBlock();
                    highBlock1 = highState1.getBlock();

                    if ((this.forceReplaceBlock(block1))||(!block1.isReplaceable(world, pos1) && this.isBlockReplaceable(block1) ) && (this.forceCanTunnelUnder(highBlock1) || highBlock1.isReplaceable(world, highPos1)))
                    {
                    	if(doConstruct)
                        {
                    		world.setBlockState(pos1, portal.getRoadState(), 3);
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
            		BlockPos pos1 = new BlockPos(xi + xOffset, yi, zi + zOffset);
            		IBlockState state1 = world.getBlockState(pos1);
            		Block block1 = state1.getBlock();

                    if (block1.isReplaceable(world, pos1) || !this.isBlockReplaceable(block1) || !this.forceReplaceBlock(block1))
                    {
                		returnAmount = false;

                    	if(doConstruct)
                        {
                			world.setBlockState(pos1, portal.getRoadState(), 3);
                        }
                    }else
                    {
                    	returnAmount = true;
                    }

            	}else if(tunnelIfObstructed)
            	{
            		BlockPos pos1 = new BlockPos(xi + xOffset, yi, zi + zOffset);
            		IBlockState state1 = world.getBlockState(pos1);
            		Block block1 = state1.getBlock();

            		if (!block1.isReplaceable(world, pos1) || this.isBlockReplaceable(block1) || !this.forceReplaceBlock(block1))
                    {
                		returnAmount = false;

                    	if(doConstruct)
                        {
                			world.setBlockState(pos1, portal.getRoadState(), 3);
                			world.setBlockToAir(pos1.offsetUp(1));
                			world.setBlockToAir(pos1.offsetUp(2));
                			world.setBlockToAir(pos1.offsetUp(3));
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
            int xOffset = i * dir.getFrontOffsetX();
            int zOffset = i * dir.getFrontOffsetZ();

            for (int yOffset = 0; yOffset <= clearance; yOffset++)
            {
                int sign = 1;

                BlockPos pos1 = new BlockPos(xi + xOffset, yi + sign * yOffset, zi + zOffset);
                BlockPos highPos1 = pos1.offsetUp();
                
                IBlockState state1 = world.getBlockState(pos1);
                IBlockState highState1 = world.getBlockState(highPos1);
                Block block1 = state1.getBlock();
                Block highBlock1 = highState1.getBlock();

                if ((this.forceReplaceBlock(block1))||(!block1.isReplaceable(world, pos1) && this.isBlockReplaceable(block1) ) && (this.forceCanTunnelUnder(highBlock1) || highBlock1.isReplaceable(world, highPos1)))
                {
                    yi += sign * yOffset;
                    break;
                } else
                {
                    sign = -1;
                    BlockPos pos2 = new BlockPos(xi + xOffset, yi + sign * yOffset, zi + zOffset);
                    BlockPos highPos2 = pos2.offsetUp();
                    
                    IBlockState state2 = world.getBlockState(pos2);
                    IBlockState highState2 = world.getBlockState(highPos2);
                    Block block2 = state2.getBlock();
                    Block highBlock2 = highState2.getBlock();

                    if ((this.forceReplaceBlock(block2))||(!block2.isReplaceable(world, pos2) && this.isBlockReplaceable(block2) ) && (this.forceCanTunnelUnder(highBlock2) || highBlock2.isReplaceable(world, highPos2)))
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
