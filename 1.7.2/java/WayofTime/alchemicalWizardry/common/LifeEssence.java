package WayofTime.alchemicalWizardry.common;

import net.minecraftforge.fluids.Fluid;

public class LifeEssence extends Fluid
{
    public LifeEssence(String fluidName)
    {
        super(fluidName);
        //setUnlocalizedName("lifeEssence");
        //setBlockID(id);
        this.setDensity(2000);
        this.setViscosity(2000);
        //this.setFlowingIcon(flowingIcon)
    }

    @Override
    public int getColor()
    {
        return 0xEEEEEE;
    }

    @Override
    public String getLocalizedName()
    {
        return "Life Essence";
    }
}
