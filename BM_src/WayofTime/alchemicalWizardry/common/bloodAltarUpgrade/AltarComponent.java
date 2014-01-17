package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

public class AltarComponent
{
    private int x;
    private int y;
    private int z;
    private int blockID;
    private int metadata;
    private boolean isBloodRune;
    private boolean isUpgradeSlot;

    public AltarComponent(int x, int y, int z, int blockID, int metadata, boolean isBloodRune, boolean isUpgradeSlot)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockID = blockID;
        this.metadata = metadata;
        this.isBloodRune = isBloodRune;
        this.isUpgradeSlot = isUpgradeSlot;
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

    public int getBlockID()
    {
        return blockID;
    }

    public int getMetadata()
    {
        return metadata;
    }

    public boolean isBloodRune()
    {
        return isBloodRune;
    }

    public boolean isUpgradeSlot()
    {
        return isUpgradeSlot;
    }
}
