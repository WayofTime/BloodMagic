package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.event.recipes.FluidStackIngredient;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.data.recipe.builder.ARCRecipeBuilder;

public class ARCRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "arc/";
		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromTag(Tags.Items.BONES), null, new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), null).addRandomOutput(new ItemStack(Items.DIAMOND), 0.5).build(consumer, BloodMagic.rl(basePath + "test1"));
		ARCRecipeBuilder.arc(Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), Ingredient.fromItems(Items.ACACIA_BOAT), FluidStackIngredient.from(Fluids.LAVA, 1000), new ItemStack(BloodMagicBlocks.BLOOD_ALTAR.get()), new FluidStack(Fluids.WATER, 100)).build(consumer, BloodMagic.rl(basePath + "test2"));
	}
}
