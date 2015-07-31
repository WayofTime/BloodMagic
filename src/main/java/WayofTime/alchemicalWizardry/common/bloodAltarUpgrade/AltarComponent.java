package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import net.minecraft.block.Block;

public class AltarComponent
{
    private int x;
    private int y;
    private int z;
    private Block block;
    private int metadata;
    private boolean isBloodRune;
    private boolean isUpgradeSlot;

    public AltarComponent(int x, int y, int z, Block block, int metadata, boolean isBloodRune, boolean isUpgradeSlot)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
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

    public Block getBlock()
    {
        return block;
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
