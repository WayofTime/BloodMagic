package WayofTime.bloodmagic.compat.jei.alchemyArray;

import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AlchemyArrayCraftingRecipeJEI implements IRecipeWrapper {
    @Nonnull
    private final List<ItemStack> inputs;
    @Nullable
    private final List<ItemStack> catalyst;
    @Nonnull
    private final ItemStack output;

    public AlchemyArrayCraftingRecipeJEI(RecipeAlchemyArray array) {
        this.inputs = NonNullList.from(ItemStack.EMPTY, array.getInput().getMatchingStacks());
        this.catalyst = NonNullList.from(ItemStack.EMPTY, array.getCatalyst().getMatchingStacks());
        this.output = array.getOutput();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, Lists.newArrayList(inputs, catalyst));
        ingredients.setOutput(ItemStack.class, output);
    }
}
