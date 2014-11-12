package WayofTime.alchemicalWizardry.api.rituals;

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
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }
    
    public int getX(int direction)
    {
    	switch(direction)
    	{
    	case 2:
    		return -this.getZ();
    	case 3:
    		return -this.getX();
    	case 4:
    		return this.getZ();
    	default: return this.getX();
    	}
    }
    
    public int getZ(int direction)
    {
        switch(direction)
        {
        case 2:
        	return this.getX();
        case 3:
        	return -this.getZ();
        case 4:
        	return -this.getX();
        default: return this.getZ();
        }
    }

    public int getStoneType()
    {
        return this.stoneType;
    }
}