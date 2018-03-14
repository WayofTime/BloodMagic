package WayofTime.bloodmagic.compat.jei.alchemyTable;

import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.recipe.alchemyTable.AlchemyTableRecipe;
import WayofTime.bloodmagic.compat.jei.BloodMagicJEIPlugin;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AlchemyTableRecipeJEI implements IRecipeWrapper {

    private final List input;
    private final ItemStack output;
    private final int tier;
    private final int syphon;
    private final int ticks;

    public AlchemyTableRecipeJEI(AlchemyTableRecipe recipe) {
        this.input = recipe.getInput();
        this.output = recipe.getRecipeOutput(Lists.newArrayList());
        this.tier = recipe.getTierRequired();
        this.syphon = recipe.getLpDrained();
        this.ticks = recipe.getTicksRequired();
    }

    public AlchemyTableRecipeJEI(RecipeAlchemyTable recipe) {
        this.input = recipe.getInput();
        this.output = recipe.getOutput();
        this.tier = recipe.getMinimumTier();
        this.syphon = recipe.getSyphon();
        this.ticks = recipe.getTicks();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> expanded = BloodMagicJEIPlugin.jeiHelper.getStackHelper().expandRecipeItemStackInputs(input);
        ingredients.setInputLists(ItemStack.class, expanded);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        ArrayList<String> ret = new ArrayList<>();
        if (mouseX >= 58 && mouseX <= 78 && mouseY >= 21 && mouseY <= 34) {
            ret.add(TextHelper.localize("tooltip.bloodmagic.tier", tier));
            ret.add(TextHelper.localize("jei.bloodmagic.recipe.lpDrained", syphon));
            ret.add(TextHelper.localize("jei.bloodmagic.recipe.ticksRequired", ticks));
        }
        return ret;
    }

    public int getTier() {
        return tier;
    }
}
