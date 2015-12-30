package WayofTime.bloodmagic.compat.jei;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public abstract class BloodMagicRecipeWrapper implements IRecipeWrapper
{

    @Override
    public List<FluidStack> getFluidInputs()
    {
        return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
        return Collections.emptyList();
    }
}
