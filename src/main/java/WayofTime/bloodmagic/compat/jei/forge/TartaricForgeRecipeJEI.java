package WayofTime.bloodmagic.compat.jei.forge;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;
import lombok.Getter;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.List;

public class TartaricForgeRecipeJEI extends BlankRecipeWrapper
{
    @Getter
    private TartaricForgeRecipe recipe;
    @Getter
    private Set<ItemStack> validGems = new HashSet<ItemStack>();

    public TartaricForgeRecipeJEI(TartaricForgeRecipe recipe)
    {
        this.recipe = recipe;

        this.validGems.add(new ItemStack(ModItems.soulGem, 1, 0));
        this.validGems.add(new ItemStack(ModItems.soulGem, 1, 1));
        this.validGems.add(new ItemStack(ModItems.soulGem, 1, 2));
        this.validGems.add(new ItemStack(ModItems.soulGem, 1, 3));
        this.validGems.add(new ItemStack(ModItems.monsterSoul));
    }

    @Override
    @Nonnull
    public List<Collection> getInputs()
    {
        ArrayList<Collection> ret = new ArrayList<Collection>();
        ret.add(recipe.getInput());
        ret.add(validGems);
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
        if (mouseX >= 40 && mouseX <= 60 && mouseY >= 21 && mouseY <= 34)
        {
            ret.add(TextHelper.localize("jei.BloodMagic.recipe.minimumSouls", recipe.getMinimumSouls()));
            ret.add(TextHelper.localize("jei.BloodMagic.recipe.soulsDrained", recipe.getSoulsDrained()));
            return ret;
        }
        return null;
    }
}
