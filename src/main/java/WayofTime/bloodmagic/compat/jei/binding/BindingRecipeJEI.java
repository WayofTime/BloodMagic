package WayofTime.bloodmagic.compat.jei.binding;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class BindingRecipeJEI extends BlankRecipeWrapper
{
    @Nonnull
    private final List<ItemStack> inputs;

    @Nonnull
    private final ItemStack catalyst;

    @Nonnull
    private final ItemStack output;

    @SuppressWarnings("unchecked")
    public BindingRecipeJEI(@Nonnull List<ItemStack> input, @Nonnull ItemStack catalyst, @Nonnull ItemStack output)
    {
        this.inputs = input;
        this.catalyst = catalyst;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {

        ingredients.setInputLists(ItemStack.class, Lists.newArrayList(inputs, Lists.newArrayList(catalyst)));
        ingredients.setOutput(ItemStack.class, output);
    }
}
