package WayofTime.bloodmagic.compat.jei.binding;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

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
