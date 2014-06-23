package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DemonBuilding 
{
	public BuildingSchematic schematic;
	public GridSpaceHolder area;
	public int buildingTier;
	public int type;
	
	public DemonBuilding(BuildingSchematic schematic)
	{
		this.schematic = schematic;
		this.type = 0;
		this.buildingTier = 0;
		this.area = this.createGSHForSchematic(schematic);
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
}
