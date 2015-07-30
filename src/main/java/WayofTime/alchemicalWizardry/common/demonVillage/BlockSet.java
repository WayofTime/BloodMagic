package WayofTime.alchemicalWizardry.common.demonVillage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.UniqueIdentifier;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.loot.DemonVillageLootRegistry;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.IBlockPortalNode;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.ITilePortalNode;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class BlockSet
{
    protected String blockid;
    protected int[] metadata;
    protected List<Int3> positions;

    public BlockSet()
    {
        this(Blocks.stone);
    }

    public BlockSet(String blockid)
    {
        this.blockid = blockid;
        this.metadata = new int[4];
        positions = new ArrayList<Int3>();
    }

    public BlockSet(Block block)
    {
        this(BlockSet.getPairedIdForBlock(block));
    }

    public BlockSet(Block block, int meta)
    {
        this(block);
        for (int i = 0; i < metadata.length; i++)
        {
            metadata[i] = meta;
        }
        if (block instanceof BlockStairs)
        {
            int[] northSet = new int[]{2, 3, 0, 1};
            int[] eastSet = new int[]{1, 0, 2, 3};
            int[] southSet = new int[]{3, 2, 1, 0};
            int[] westSet = new int[]{0, 1, 3, 2};
            int[] northUpSet = new int[]{6, 7, 4, 5};
            int[] eastUpSet = new int[]{5, 4, 6, 7};
            int[] southUpSet = new int[]{7, 6, 5, 4};
            int[] westUpSet = new int[]{4, 5, 7, 6};

            switch (meta)
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
        else if(block instanceof BlockLadder)
        {
        	int[] northSet = new int[]{3, 2, 5, 4};
            int[] eastSet = new int[]{4, 5, 3, 2};
            int[] southSet = new int[]{2, 3, 4, 5};
            int[] westSet = new int[]{5, 4, 2, 3};
            
            switch (meta)
            {
                case 3:
                    metadata = northSet;
                    break;
                case 4:
                    metadata = eastSet;
                    break;
                case 2:
                    metadata = southSet;
                    break;
                case 5:
                    metadata = westSet;
                    break;
            }
        }else if(block instanceof BlockTrapDoor)
        {
        	int div = meta / 4;
        	int mod = meta % 4;
        	int[] northSet = new int[]{1 + div*4, div*4, 3 + div*4, 2 + div*4}; //Second one: 0 +
            int[] eastSet = new int[]{2 + div*4, 3 + div*4, 1 + div*4, div*4}; //Last one: 0 +
            int[] southSet = new int[]{div*4, 1 + div*4, 2 + div*4, 3 + div*4}; //First one: 0 +
            int[] westSet = new int[]{3 + div*4, 2 + div*4, div*4, 1 + div*4}; //Third one: 0 +

            switch (mod)
            {
                case 0:
                    metadata = southSet;
                    break;
                case 1:
                    metadata = northSet;
                    break;
                case 2:
                    metadata = eastSet;
                    break;
                case 3:
                    metadata = westSet;
                    break;
            }
        }else if(block instanceof BlockTorch)
        {
        	int[] northSet = new int[]{3, 4, 1, 2};
            int[] eastSet = new int[]{2, 1, 3, 4};
            int[] southSet = new int[]{4, 3, 2, 1};
            int[] westSet = new int[]{1, 2, 4, 3};


            switch (meta)
            {
                case 1:
                    metadata = westSet;
                    break;
                case 2:
                    metadata = eastSet;
                    break;
                case 3:
                    metadata = northSet;
                    break;
                case 4:
                    metadata = southSet;
                    break;
            }
        }else if(block instanceof BlockDoor)
        {
        	int[] northSet = new int[]{3, 1, 2, 0};
            int[] eastSet = new int[]{0, 2, 3, 1};
            int[] southSet = new int[]{1, 3, 0, 2};
            int[] westSet = new int[]{2, 0, 1, 3};


            switch (meta)
            {
                case 0:
                    metadata = eastSet;
                    break;
                case 1:
                    metadata = southSet;
                    break;
                case 2:
                    metadata = westSet;
                    break;
                case 3:
                    metadata = northSet;
                    break;
            }
        }else if(block instanceof BlockRedstoneComparator)
        {
        	int div = meta / 4;
        	int mod = meta % 4;
            int[] northSet = new int[]{div * 4, 2 + div * 4, 3 + div * 4, 1 + div * 4}; //First one: 0 +
            int[] eastSet = new int[]{1 + div*4, 3 + div*4, div*4, 2 + div*4}; //Third one: 0 +
            int[] southSet = new int[]{2 + div*4, div*4, 1 + div*4, 3 + div*4}; //Second one: 0 +
            int[] westSet = new int[]{3 + div*4, 1 + div*4, 2 + div*4, div*4}; //Last one: 0 +

            switch (mod)
            {
                case 0:
                    metadata = northSet;
                    break;
                case 1:
                    metadata = eastSet;
                    break;
                case 2:
                    metadata = southSet;
                    break;
                case 3:
                    metadata = westSet;
                    break;
            }
        }else if(block instanceof BlockRedstoneRepeater)
        {
        	int div = meta / 4;
        	int mod = meta % 4;
        	int[] northSet = new int[]{div*4, 2 + div*4, 3 + div*4, 1 + div*4}; //First one: 0 +
            int[] eastSet = new int[]{1 + div*4, 3 + div*4, div*4, 2 + div*4}; //Third one: 0 +
            int[] southSet = new int[]{2 + div*4, div*4, 1 + div*4, 3 + div*4}; //Second one: 0 +
            int[] westSet = new int[]{3 + div*4, 1 + div*4, 2 + div*4, div*4}; //Last one: 0 +



            switch (mod)
            {
                case 0:
                    metadata = northSet;
                    break;
                case 1:
                    metadata = eastSet;
                    break;
                case 2:
                    metadata = southSet;
                    break;
                case 3:
                    metadata = westSet;
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
        return getBlockForString(blockid);
    }

    public static String getPairedIdForBlock(Block block)
    {
        UniqueIdentifier un = GameRegistry.findUniqueIdentifierFor(block);
        String name = "";

        if (un != null)
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

    public int getMetaForDirection(EnumFacing dir)
    {
        if (metadata.length < 4)
        {
            return 0;
        }

        switch (dir)
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

    public void buildAtIndex(TEDemonPortal teDemonPortal, World world, int xCoord, int yCoord, int zCoord, EnumFacing dir, int index, boolean populateInventories, int tier)
    {
        Block block = this.getBlock();
        if (index >= positions.size() || block == null)
        {
            return;
        }

        Int3 position = positions.get(index);
        int xOff = position.xCoord;
        int yOff = position.yCoord;
        int zOff = position.zCoord;
        int meta = this.getMetaForDirection(dir);

        switch (dir)
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

        BlockPos newPos = new BlockPos(xCoord + xOff, yCoord + yOff, zCoord + zOff);
        world.setBlockState(newPos, block.getStateFromMeta(meta), 3);
        if(populateInventories)
        {
        	this.populateIfIInventory(world, newPos, tier);
        }
        if(block instanceof IBlockPortalNode)
        {
        	TileEntity tile = world.getTileEntity(newPos);
        	if(tile instanceof ITilePortalNode)
        	{
        		((ITilePortalNode) tile).setPortalLocation(teDemonPortal); 
        	}
        }
    }
    
    public void populateIfIInventory(World world, BlockPos pos, int tier)
    {
    	TileEntity tile = world.getTileEntity(pos);
    	if(tile instanceof IInventory)
    	{
    		DemonVillageLootRegistry.populateChest((IInventory)tile, tier);
    	}
    }

    public void buildAll(TEDemonPortal teDemonPortal, World world, int xCoord, int yCoord, int zCoord, EnumFacing dir, boolean populateInventories, int tier)
    {
        for (int i = 0; i < positions.size(); i++)
        {
            this.buildAtIndex(teDemonPortal, world, xCoord, yCoord, zCoord, dir, i, populateInventories, tier);
        }
    }

    public boolean isContained(Block block, int defaultMeta)
    {
        Block thisBlock = this.getBlock();
        return thisBlock != null && thisBlock.equals(block) && this.metadata[0] == defaultMeta;
    }
}