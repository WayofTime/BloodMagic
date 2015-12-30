package WayofTime.bloodmagic.compat.jei.alchemyArray;

import WayofTime.bloodmagic.compat.jei.BloodMagicRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class AlchemyArrayCraftingRecipeJEI extends BloodMagicRecipeWrapper
{

    @Nonnull
    private final List<ItemStack> inputs;

    @Nonnull
    private final ItemStack output;

    @SuppressWarnings("unchecked")
    public AlchemyArrayCraftingRecipeJEI(@Nonnull ItemStack input, @Nullable ItemStack catalyst, @Nonnull ItemStack output)
    {
        this.inputs = Arrays.asList(new ItemStack[] { input, catalyst });
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

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }
}
