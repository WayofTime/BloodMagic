package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.util.EnumFacing;

public interface IOrientable
{
    EnumFacing getInputDirection();

    EnumFacing getOutputDirection();

    void setInputDirection(EnumFacing direction);

    void setOutputDirection(EnumFacing direction);
}
