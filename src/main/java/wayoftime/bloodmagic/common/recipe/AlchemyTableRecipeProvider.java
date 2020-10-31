package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.AlchemyTableRecipeBuilder;

public class AlchemyTableRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		String basePath = "alchemytable/";
		AlchemyTableRecipeBuilder.alchemyTable(new ItemStack(Items.STRING, 4), 0, 100, 0).addIngredient(Ingredient.fromTag(ItemTags.WOOL)).addIngredient(Ingredient.fromItems(Items.FLINT)).build(consumer, BloodMagic.rl(basePath + "string"));
	}

}
