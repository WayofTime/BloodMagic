package WayofTime.alchemicalWizardry.common;

public class PlinthComponent
{
    public int xOffset;
    public int yOffset;
    public int zOffset;
    public int ring;

    public PlinthComponent(int xOffset, int yOffset, int zOffset, int ring)
    {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.ring = ring;
    }

    public int getXOffset()
    {
        return xOffset;
    }

    public int getYOffset()
    {
        return yOffset;
    }

    public int getZOffset()
    {
        return zOffset;
    }

    public int getRing()
    {
        return ring;
    }
}
