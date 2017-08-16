package WayofTime.bloodmagic.compat.jei.alchemyArray;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AlchemyArrayCraftingRecipeJEI extends BlankRecipeWrapper {
    @Nonnull
    private final List<ItemStack> inputs;
    @Nullable
    private final ItemStack catalyst;
    @Nonnull
    private final ItemStack output;

    public AlchemyArrayCraftingRecipeJEI(@Nonnull List<ItemStack> input, @Nullable ItemStack catalyst, @Nonnull ItemStack output) {
        this.inputs = input;
        this.catalyst = catalyst;
        this.output = output;
    }

    public ItemStack getCatalyst() {
        return catalyst;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, Lists.newArrayList(inputs, Lists.newArrayList(catalyst)));
        ingredients.setOutput(ItemStack.class, output);
    }
}
