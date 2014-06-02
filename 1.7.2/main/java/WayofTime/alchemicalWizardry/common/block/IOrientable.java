package WayofTime.alchemicalWizardry.common.block;

import net.minecraftforge.common.util.ForgeDirection;

public interface IOrientable
{
    public ForgeDirection getInputDirection();

    public ForgeDirection getOutputDirection();

    public void setInputDirection(ForgeDirection direction);

    public void setOutputDirection(ForgeDirection direction);
}
