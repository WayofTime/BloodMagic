package WayofTime.alchemicalWizardry.common;


public class Int3 
{
	public int xCoord;
	public int yCoord;
	public int zCoord;
	
	public Int3(int xCoord, int yCoord, int zCoord)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Int3 ? ((Int3)o).xCoord == this.xCoord && ((Int3)o).yCoord == this.yCoord && ((Int3)o).zCoord == this.zCoord : false;
	}
}
