package WayofTime.bloodmagic.compat.jei.binding;

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
    @Nonnull
    public List<ItemStack> getInputs()
    {
        return inputs;
    }

    public ItemStack getCatalyst()
    {
        return catalyst;
    }

    @Override
    @Nonnull
    public List<ItemStack> getOutputs()
    {
        return Collections.singletonList(output);
    }
}
