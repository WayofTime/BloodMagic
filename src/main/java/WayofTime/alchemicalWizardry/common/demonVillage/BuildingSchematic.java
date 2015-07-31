package WayofTime.alchemicalWizardry.common.demonVillage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class BuildingSchematic
{
    public String name;
    public int doorX;
    public int doorZ;
    public int doorY;
    public int buildingTier;
    public int buildingType;
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
        this.doorY = 0;
        this.buildingTier = 0;
        this.buildingType = DemonBuilding.BUILDING_HOUSE;
    }

    public void addBlockWithMeta(Block block, int meta, int xOffset, int yOffset, int zOffset)
    {
        for (BlockSet set : blockList)
        {
            if (set.isContained(block, meta))
            {
                set.addPositionToBlock(xOffset, yOffset, zOffset);
                return;
            }
        }

        BlockSet set = new BlockSet(block, meta);
        set.addPositionToBlock(xOffset, yOffset, zOffset);
        blockList.add(set);
    }

    public void buildAll(TEDemonPortal teDemonPortal, World world, int xCoord, int yCoord, int zCoord, ForgeDirection dir, boolean populateInventories)
    {
        for (BlockSet set : blockList)
        {
            set.buildAll(teDemonPortal, world, xCoord, yCoord, zCoord, dir, populateInventories, this.buildingTier);
        }
    }

    public GridSpaceHolder createGSH()
    {
        GridSpaceHolder holder = new GridSpaceHolder();

        for (BlockSet set : blockList)
        {
            for (Int3 coords : set.getPositions())
            {
                int gridX = (int) ((coords.xCoord + 2 * Math.signum(coords.xCoord)) / 5);
                int gridZ = (int) ((coords.zCoord + 2 * Math.signum(coords.zCoord)) / 5);

                holder.setGridSpace(gridX, gridZ, new GridSpace(GridSpace.HOUSE, 0));
            }
        }

        return holder;
    }

    public Int3 getGridSpotOfDoor()
    {
        int gridX = (int) ((doorX + 2 * Math.signum(doorX)) / 5);
        int gridZ = (int) ((doorZ + 2 * Math.signum(doorZ)) / 5);

        return new Int3(gridX, doorY, gridZ);
    }

    public List<Int3> getGriddedPositions(ForgeDirection dir)
    {
        List<Int3> positionList = new ArrayList();

        for (BlockSet blockSet : blockList)
        {
            for (Int3 pos : blockSet.getPositions())
            {
                int xOff = pos.xCoord;
                int zOff = pos.zCoord;

                switch (dir)
                {
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

                Int3 nextPos = new Int3(xOff, 0, zOff);
                if (!positionList.contains(nextPos))
                {
                    positionList.add(nextPos);
                }
            }
        }

        return positionList;
    }

    public void destroyAllInField(World world, int xCoord, int yCoord, int zCoord, ForgeDirection dir)
    {
//        GridSpaceHolder grid = this.createGSH(); //GridSpaceHolder is not aware of the buildings - need to use the schematic

        List<Int3> positionList = getGriddedPositions(dir);

        for (int i = this.getMinY(); i <= this.getMaxY(); i++)
        {
            for (Int3 pos : positionList)
            {
                Block block = world.getBlock(xCoord + pos.xCoord, yCoord + i, zCoord + pos.zCoord);
                if (block != ModBlocks.blockDemonPortal)
                {
                    world.setBlockToAir(xCoord + pos.xCoord, yCoord + i, zCoord + pos.zCoord);
                }
            }
        }
    }

    public int getMinY()
    {
        int min = 0;
        for (BlockSet set : blockList)
        {
            for (Int3 pos : set.getPositions())
            {
                if (pos.yCoord < min)
                {
                    min = pos.yCoord;
                }
            }
        }

        return min;
    }

    public int getMaxY()
    {
        int max = 0;
        for (BlockSet set : blockList)
        {
            for (Int3 pos : set.getPositions())
            {
                if (pos.yCoord > max)
                {
                    max = pos.yCoord;
                }
            }
        }
        return max;
    }
    
    public String getName()
    {
    	return this.name;
    }
}