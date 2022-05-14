package wayoftime.bloodmagic.common.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.PotionEffectRecipeBuilder;

public class PotionEffectRecipeProvider implements ISubRecipeProvider
{
	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer)
	{
		ItemStack waterbottleStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);

		String basePath = "flask/";

		PotionEffectRecipeBuilder.PotionEffect(Effects.SPEED, 100, 200, 200, 1).addIngredient(Ingredient.fromItems(Items.SUGAR)).build(consumer, BloodMagic.rl(basePath + "speed_boost"));
		PotionEffectRecipeBuilder.PotionEffect(Effects.FIRE_RESISTANCE, 100, 200, 200, 1).addIngredient(Ingredient.fromItems(Items.BLAZE_POWDER)).build(consumer, BloodMagic.rl(basePath + "fire_resist"));
	}
}