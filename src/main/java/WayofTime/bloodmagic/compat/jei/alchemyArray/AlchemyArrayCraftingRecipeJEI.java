package WayofTime.bloodmagic.compat.jei.alchemyArray;

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

    @Nullable
    private final ItemStack catalyst;

    @Nonnull
    private final ItemStack output;

    public AlchemyArrayCraftingRecipeJEI(@Nonnull List<ItemStack> input, @Nullable ItemStack catalyst, @Nonnull ItemStack output)
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
