package wayoftime.bloodmagic.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.common.item.routing.ICompositeItemFilterProvider;
import wayoftime.bloodmagic.common.item.routing.INestableItemFilterProvider;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class RecipeFilterMergeAlchemyTable extends RecipeAlchemyTable
{
	final Ingredient filterIngredient;

	public RecipeFilterMergeAlchemyTable(ResourceLocation id, Ingredient filterIngredient, List<Ingredient> input, ItemStack output, int syphon, int ticks, int minimumTier)
	{
		super(id, input, output, syphon, ticks, minimumTier);
		this.filterIngredient = filterIngredient;
	}

	@Nonnull
	@Override
	public ItemStack getOutput(List<ItemStack> inputs)
	{
		int index = -1;
		for (int i = 0; i < inputs.size(); i++)
		{
			if (filterIngredient.test(inputs.get(i)))
			{
				index = i;
			}
		}

		if (index == -1)
		{
			return ItemStack.EMPTY;
		}

		ItemStack filterStack = inputs.get(index);
		if (!(filterStack.getItem() instanceof ICompositeItemFilterProvider))
		{
			return ItemStack.EMPTY;
		}

//		filterStack = filterStack.copy();
		// TODO: Add logic for combining.
		for (int i = 0; i < inputs.size(); i++)
		{
			if (i == index || inputs.get(i).isEmpty())
			{
				continue;
			}

			ItemStack inputStack = inputs.get(i);
			if (!(inputStack.getItem() instanceof INestableItemFilterProvider))
			{
				continue;
			}

			if (((ICompositeItemFilterProvider) filterStack.getItem()).canReceiveNestedFilter(filterStack, inputStack))
			{
				filterStack = ((ICompositeItemFilterProvider) filterStack.getItem()).nestFilter(filterStack, inputStack);
			}
		}

		return filterStack;
	}

	@Override
	public List<Ingredient> getInput()
	{
		List<Ingredient> returnInputs = new ArrayList<Ingredient>(input);
		returnInputs.add(filterIngredient);
		return returnInputs;
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		filterIngredient.toNetwork(buffer);
		super.write(buffer);
	}

	@Override
	public RecipeSerializer<RecipeFilterMergeAlchemyTable> getSerializer()
	{
		return BloodMagicRecipeSerializers.FILTERALCHEMYTABLE.getRecipeSerializer();
	}
}
