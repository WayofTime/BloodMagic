package WayofTime.bloodmagic.compat.jei.alchemyTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.recipe.AlchemyTableRecipe;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class AlchemyTableRecipeJEI extends BlankRecipeWrapper
{
    @Getter
    private AlchemyTableRecipe recipe;

//    @Getter
//    private ArrayList<ItemStack> validGems = new ArrayList<ItemStack>();

    public AlchemyTableRecipeJEI(AlchemyTableRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    @Nonnull
    public List<Collection> getInputs()
    {
        ArrayList<Collection> ret = new ArrayList<Collection>();
        ret.add(recipe.getInput());
        ret.add(OrbRegistry.getOrbsDownToTier(recipe.getTierRequired()));
        return ret;
    }

    @Override
    @Nonnull
    public List<ItemStack> getOutputs()
    {
        return Collections.singletonList(recipe.getRecipeOutput());
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        ArrayList<String> ret = new ArrayList<String>();
        if (mouseX >= 58 && mouseX <= 78 && mouseY >= 21 && mouseY <= 34)
        {
            ret.add(TextHelper.localize("jei.BloodMagic.recipe.lpDrained", recipe.getLpDrained()));
            ret.add(TextHelper.localize("jei.BloodMagic.recipe.ticksRequired", recipe.getTicksRequired()));
            return ret;
        }
        return null;
    }
}
