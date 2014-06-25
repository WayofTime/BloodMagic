package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.common.Int3;

public class DemonBuilding 
{
	public BuildingSchematic schematic;
	public GridSpaceHolder area;
	public int buildingTier;
	public int type;
	public Int3 doorGridSpace;
	
	public DemonBuilding(BuildingSchematic schematic)
	{
		this.schematic = schematic;
		this.type = 0;
		this.buildingTier = 0;
		this.area = this.createGSHForSchematic(schematic);
		this.doorGridSpace = schematic.getGridSpotOfDoor();
	}
	
	public String getName()
	{
		return schematic.name;
	}
	
	public boolean isValid(GridSpaceHolder master, int gridX, int gridZ, ForgeDirection dir)
	{
		return area.doesContainAll(master, gridX, gridZ, dir);
	}
	
	public void buildAll(World world, int xCoord, int yCoord, int zCoord, ForgeDirection dir)
	{
		schematic.buildAll(world, xCoord, yCoord, zCoord, dir);
	}
	
	public void setAllGridSpaces(int xInit, int zInit, int yLevel, ForgeDirection dir, int type, GridSpaceHolder master)
	{
		area.setAllGridSpaces(xInit, zInit, yLevel, dir, type, master);
	}
	
	public GridSpaceHolder createGSHForSchematic(BuildingSchematic scheme)
	{
		return scheme.createGSH();
	}
	
	public Int3 getDoorSpace(ForgeDirection dir)
	{
		int x = 0;
		int z = 0;
		switch(dir)
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
		
		return new Int3(x, 0, z);
	}
	
	public Int3 getGridOffsetFromRoad(ForgeDirection sideOfRoad, int yLevel)
	{
		Int3 doorSpace = this.getDoorSpace(sideOfRoad);
		int x = doorSpace.xCoord;
		int z = doorSpace.zCoord;
		
		switch(sideOfRoad)
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
}
