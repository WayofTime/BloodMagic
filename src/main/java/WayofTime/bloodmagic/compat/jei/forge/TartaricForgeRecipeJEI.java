package WayofTime.bloodmagic.compat.jei.forge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class TartaricForgeRecipeJEI extends BlankRecipeWrapper
{
    @Getter
    private TartaricForgeRecipe recipe;
    @Getter
    private ArrayList<ItemStack> validGems = new ArrayList<ItemStack>();

    public TartaricForgeRecipeJEI(TartaricForgeRecipe recipe)
    {
        this.recipe = recipe;

        for (DefaultWill will : DefaultWill.values())
            if (will.minSouls >= recipe.getMinimumSouls())
                this.validGems.add(will.willStack);
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

    public enum DefaultWill
    {
        SOUL(new ItemStack(ModItems.monsterSoul, 1, 0), 64),
        PETTY(new ItemStack(ModItems.soulGem, 1, 0), 64),
        LESSER(new ItemStack(ModItems.soulGem, 1, 1), 256),
        COMMON(new ItemStack(ModItems.soulGem, 1, 2), 1024),
        GREATER(new ItemStack(ModItems.soulGem, 1, 3), 4096),
        GRAND(new ItemStack(ModItems.soulGem, 1, 4), 16384);

        public final ItemStack willStack;
        public final double minSouls;

        DefaultWill(ItemStack willStack, double minSouls)
        {
            this.willStack = willStack;
            this.minSouls = minSouls;
        }
    }
}
