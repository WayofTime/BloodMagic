package wayoftime.bloodmagic.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class IRecipeAlchemyTable extends RecipeAlchemyTable
{
	public IRecipeAlchemyTable(ResourceLocation id, List<Ingredient> input, ItemStack output, int syphon, int ticks, int minimumTier)
	{
		super(id, input, output, syphon, ticks, minimumTier);
	}

	@Override
	public IRecipeSerializer<RecipeAlchemyTable> getSerializer()
	{
		return BloodMagicRecipeSerializers.ALCHEMYTABLE.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipeAlchemyTable> getType()
	{
		return BloodMagicRecipeType.ALCHEMYTABLE;
	}
}