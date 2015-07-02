package WayofTime.alchemicalWizardry.common.block;

import net.minecraftforge.common.util.ForgeDirection;

public interface IOrientable
{
    ForgeDirection getInputDirection();

    ForgeDirection getOutputDirection();

    void setInputDirection(ForgeDirection direction);

    void setOutputDirection(ForgeDirection direction);
}
