package WayofTime.bloodmagic.compat.jei.forge;

import WayofTime.bloodmagic.api.recipe.SoulForgeRecipe;
import WayofTime.bloodmagic.registry.ModItems;
import lombok.Getter;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.List;

public class SoulForgeRecipeJEI extends BlankRecipeWrapper
{
    @Getter
    private SoulForgeRecipe recipe;
    @Getter
    private Set<ItemStack> validGems = new HashSet<ItemStack>();

    public SoulForgeRecipeJEI(SoulForgeRecipe recipe)
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

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
//        String minimum = TextHelper.localize("jei.BloodMagic.recipe.minimumSouls", recipe.getMinimumSouls());
//        String drained = TextHelper.localize("jei.BloodMagic.recipe.soulsDrained", recipe.getSoulsDrained());
//        minecraft.fontRendererObj.drawString(minimum, 90 - minecraft.fontRendererObj.getStringWidth(minimum) / 2, 0, Color.gray.getRGB());
//        minecraft.fontRendererObj.drawString(drained, 90 - minecraft.fontRendererObj.getStringWidth(drained) / 2, 10, Color.gray.getRGB());
    }
}
