package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class DemonBuilding
{
    public static final int BUILDING_HOUSE = 0;
    public static final int BUILDING_PORTAL = 1;

    public BuildingSchematic schematic;
    public GridSpaceHolder area;
    public int buildingTier;
    public int buildingType;
    public Int3 doorGridSpace;

    public DemonBuilding(BuildingSchematic schematic)
    {
        this.schematic = schematic;
        this.buildingType = schematic.buildingType;
        this.buildingTier = schematic.buildingTier;
        this.area = this.createGSHForSchematic(schematic);
        this.doorGridSpace = schematic.getGridSpotOfDoor();
    }

    public String getName()
    {
        return schematic.getName();
    }

    public boolean isValid(GridSpaceHolder master, int gridX, int gridZ, EnumFacing dir)
    {
        return area.doesContainAll(master, gridX, gridZ, dir);
    }

    public void buildAll(TEDemonPortal teDemonPortal, World world, int xCoord, int yCoord, int zCoord, EnumFacing dir, boolean populateInventories)
    {
        schematic.buildAll(teDemonPortal, world, xCoord, yCoord, zCoord, dir, populateInventories);
    }

    public void setAllGridSpaces(int xInit, int zInit, int yLevel, EnumFacing dir, int type, GridSpaceHolder master)
    {
        area.setAllGridSpaces(xInit, zInit, yLevel, dir, type, master);
    }

    public GridSpaceHolder createGSHForSchematic(BuildingSchematic scheme)
    {
        switch (this.buildingType)
        {
            case DemonBuilding.BUILDING_HOUSE:
                return scheme.createGSH();
            case DemonBuilding.BUILDING_PORTAL:

        }
        return scheme.createGSH();
    }

    public Int3 getDoorSpace(EnumFacing dir)
    {
        int x;
        int z;
        switch (dir)
        {
            case SOUTH:
                x = -doorGridSpace.xCoord;
                z = -doorGridSpace.zCoord;
                break;
            case WEST:
                x = doorGridSpace.zCoord;
                z = -doorGridSpace.xCoord;
                break;
            case EAST:
                x = -doorGridSpace.zCoord;
                z = doorGridSpace.xCoord;
                break;
            default:
                x = doorGridSpace.xCoord;
                z = doorGridSpace.zCoord;
                break;
        }

        return new Int3(x, doorGridSpace.yCoord, z);
    }

    public Int3 getGridOffsetFromRoad(EnumFacing sideOfRoad, int yLevel)
    {
        Int3 doorSpace = this.getDoorSpace(sideOfRoad);
        int x = doorSpace.xCoord;
        int z = doorSpace.zCoord;

        switch (sideOfRoad)
        {
            case SOUTH:
                z++;
                break;
            case EAST:
                x++;
                break;
            case WEST:
                x--;
                break;
            default:
                z--;
                break;
        }

        return new Int3(x, yLevel, z);
    }

    public void destroyAllInField(World world, int xCoord, int yCoord, int zCoord, EnumFacing dir)
    {
        schematic.destroyAllInField(world, xCoord, yCoord, zCoord, dir);
    }

    public int getNumberOfGridSpaces()
    {
        return area.getNumberOfGridSpaces();
    }
}
