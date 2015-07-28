package WayofTime.alchemicalWizardry.common;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class LifeEssence extends Fluid
{
    public LifeEssence(String fluidName)
    {
        super(fluidName);
        this.setDensity(2000);
        this.setViscosity(2000);
    }

    @Override
    public int getColor()
    {
        return 0xEEEEEE;
    }

    @Override
    public String getLocalizedName(FluidStack fluidStack)
    {
        return "Life Essence";
    }
}
