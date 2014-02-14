package WayofTime.alchemicalWizardry.common;

public class ArmourComponent
{
    private int xOff;
    private int zOff;

    public ArmourComponent(int xOff, int zOff)
    {
        this.xOff = xOff;
        this.zOff = zOff;
    }

    public int getXOff()
    {
        return xOff;
    }

    public int getZOff()
    {
        return zOff;
    }
}
