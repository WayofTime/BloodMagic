package WayofTime.alchemicalWizardry.common.rituals;

public class RitualComponent
{
	private int x;
	private int y;
	private int z;
	private int stoneType;
	public static final int BLANK = 0;
	public static final int WATER = 1;
	public static final int FIRE = 2;
	public static final int EARTH = 3;
	public static final int AIR = 4;
	public static final int DUSK = 5;

	public RitualComponent(int x, int y, int z, int stoneType)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.stoneType = stoneType;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getZ()
	{
		return z;
	}

	public int getStoneType()
	{
		return stoneType;
	}
}
