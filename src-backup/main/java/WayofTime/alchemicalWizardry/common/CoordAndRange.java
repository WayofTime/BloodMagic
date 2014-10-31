package WayofTime.alchemicalWizardry.common;

public class CoordAndRange 
{
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int horizRadius;
	public int vertRadius;
	
	public CoordAndRange(int x, int y, int z, int horiz, int vert)
	{
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		this.horizRadius = horiz;
		this.vertRadius = vert;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof CoordAndRange ? ((CoordAndRange)o).xCoord == this.xCoord && ((CoordAndRange)o).yCoord == this.yCoord && ((CoordAndRange)o).zCoord == this.zCoord && ((CoordAndRange)o).horizRadius == this.horizRadius && ((CoordAndRange)o).vertRadius == this.vertRadius: false;
	}
}
