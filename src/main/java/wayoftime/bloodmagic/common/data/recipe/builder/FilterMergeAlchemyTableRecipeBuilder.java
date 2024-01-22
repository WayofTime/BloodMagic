package wayoftime.bloodmagic.common.data.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FilterMergeAlchemyTableRecipeBuilder extends BloodMagicRecipeBuilder<FilterMergeAlchemyTableRecipeBuilder>
{
	private final Ingredient filter;
	private final List<Ingredient> input;
	private final int syphon;
	private final int ticks;
	private final int minimumTier;
	private ItemStack outputStack = new ItemStack(BloodMagicItems.ITEM_ROUTER_FILTER.get());

	protected FilterMergeAlchemyTableRecipeBuilder(Ingredient filter, List<Ingredient> input, int syphon, int ticks, int minimumTier)
	{
		super(bmSerializer("filteralchemytable"));
		this.filter = filter;
		this.input = input;
		this.syphon = syphon;
		this.ticks = ticks;
		this.minimumTier = minimumTier;
	}

	public static FilterMergeAlchemyTableRecipeBuilder alchemyTable(Ingredient filter, int syphon, int ticks, int minimumTier)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();

		return new FilterMergeAlchemyTableRecipeBuilder(filter, inputList, syphon, ticks, minimumTier);
	}

	public FilterMergeAlchemyTableRecipeBuilder addIngredient(Ingredient ing)
	{
		if (input.size() < RecipeAlchemyTable.MAX_INPUTS)
		{
			input.add(ing);
		}

		return this;
	}

	public FilterMergeAlchemyTableRecipeBuilder addOptionalOutputStack(ItemStack outputStack)
	{
		this.outputStack = outputStack;
		return this;
	}

	@Override
	protected AlchemyTableRecipeResult getResult(ResourceLocation id)
	{
		return new AlchemyTableRecipeResult(id);
	}

	public class AlchemyTableRecipeResult extends RecipeResult
	{
		protected AlchemyTableRecipeResult(ResourceLocation id)
		{
			super(id);
		}

		@Override
		public void serializeRecipeData(@Nonnull JsonObject json)
		{
			json.add(Constants.JSON.FILTER, filter.toJson());

			if (input.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (Ingredient ing : input)
				{
					JsonElement jsonObj = ing.toJson();

					mainArray.add(jsonObj);
				}

				json.add(Constants.JSON.INPUT, mainArray);
			}

			json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(outputStack));
			json.addProperty(Constants.JSON.SYPHON, syphon);
			json.addProperty(Constants.JSON.TICKS, ticks);
			json.addProperty(Constants.JSON.ALTAR_TIER, minimumTier);
		}
	}
}
