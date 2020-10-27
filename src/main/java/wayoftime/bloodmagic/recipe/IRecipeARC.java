package wayoftime.bloodmagic.recipe;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.api.impl.recipe.RecipeARC;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class IRecipeARC extends RecipeARC
{
	public IRecipeARC(ResourceLocation id, Ingredient input, Ingredient arc_tool, ItemStack output)
	{
		super(id, input, arc_tool, output, new ArrayList<Pair<ItemStack, Double>>());
	}

	public IRecipeARC(ResourceLocation id, Ingredient input, Ingredient arc_tool, ItemStack output, List<Pair<ItemStack, Double>> addedItems)
	{
		super(id, input, arc_tool, output, addedItems);
	}

	@Override
	public IRecipeSerializer<RecipeARC> getSerializer()
	{
		return BloodMagicRecipeSerializers.ARC.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipeARC> getType()
	{
		return BloodMagicRecipeType.ARC;
	}
}
