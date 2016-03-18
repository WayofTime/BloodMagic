package WayofTime.bloodmagic.compat.jei.alchemyArray;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class AlchemyArrayCraftingRecipeJEI extends BlankRecipeWrapper
{
    @Nonnull
    private final List<ItemStack> inputs;

    @Nonnull
    private final ItemStack output;

    @SuppressWarnings("unchecked")
    public AlchemyArrayCraftingRecipeJEI(@Nonnull ItemStack input, @Nullable ItemStack catalyst, @Nonnull ItemStack output)
    {
        this.inputs = Arrays.asList(input, catalyst);
        this.output = output;
    }

    @Override
    public List getInputs()
    {
        return inputs;
    }

    @Override
    public List getOutputs()
    {
        return Collections.singletonList(output);
    }
}
