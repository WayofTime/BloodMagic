package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.nbt.NBTTagCompound;

public class GridSpace 
{
	public static final int EMPTY = 0;
	public static final int MAIN_PORTAL = 1;
	public static final int MINI_PORTAL = 2;
	public static final int ROAD = 3;
	public static final int CROSSROAD = 4;
	public static final int HOUSE = 5;
	
	private int yLevel;
	private int type;
	
	public GridSpace()
	{
		this(EMPTY, -1);
	}
	
	public GridSpace(int type, int yLevel)
	{
		this.type = type;
		this.yLevel = yLevel;
	}
	
	public int getGridType()
	{
		return this.type;
	}
	
	public void setGridType(int type)
	{
		this.type = type;
	}
	
	public int getYLevel()
	{
		return this.yLevel;
	}
	
	public void setYLevel(int yLevel)
	{
		this.yLevel = yLevel;
	}
	
	public boolean isEmpty()
	{
		return type == this.EMPTY;
	}
	
	public static GridSpace getGridFromTag(NBTTagCompound tag)
	{
		return new GridSpace(tag.getInteger("type"), tag.getInteger("yLevel"));
	}
	
	public NBTTagCompound getTag()
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setInteger("type", type);
		tag.setInteger("yLevel", yLevel);
		
		return tag;
	}
	
	public boolean isRoadSegment()
	{
		return type == this.ROAD || type == this.CROSSROAD;
	}
	
	public boolean isBuilding()
	{
		return type == this.HOUSE;
	}
}
