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
    private final Object inputs;

    @Nullable
    private final ItemStack catalyst;

    @Nonnull
    private final ItemStack output;

    @SuppressWarnings("unchecked")
    public AlchemyArrayCraftingRecipeJEI(@Nonnull List<ItemStack> input, @Nullable ItemStack catalyst, @Nonnull ItemStack output)
    {
        this.inputs = input;
        this.catalyst = catalyst;
        this.output = output;
    }

    @Override
    public List getInputs()
    {
        return Arrays.asList(inputs, catalyst);
    }

    public ItemStack getCatalyst()
    {
        return catalyst;
    }

    @Override
    public List getOutputs()
    {
        return Collections.singletonList(output);
    }
}
