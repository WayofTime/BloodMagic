package wayoftime.bloodmagic.common.data.recipe.builder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

public class AlchemyTableRecipeBuilder extends BloodMagicRecipeBuilder<AlchemyTableRecipeBuilder>
{
	private final List<Ingredient> input;
	private final ItemStack output;
	private final int syphon;
	private final int ticks;
	private final int minimumTier;

	protected AlchemyTableRecipeBuilder(List<Ingredient> input, ItemStack output, int syphon, int ticks, int minimumTier)
	{
		super(bmSerializer("alchemytable"));
		this.input = input;
		this.output = output;
		this.syphon = syphon;
		this.ticks = ticks;
		this.minimumTier = minimumTier;
	}

	public static AlchemyTableRecipeBuilder alchemyTable(ItemStack output, int syphon, int ticks, int minimumTier)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();

		return new AlchemyTableRecipeBuilder(inputList, output, syphon, ticks, minimumTier);
	}

	public AlchemyTableRecipeBuilder addIngredient(Ingredient ing)
	{
		if (input.size() < RecipeAlchemyTable.MAX_INPUTS)
		{
			input.add(ing);
		}

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

			json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(output));
			json.addProperty(Constants.JSON.SYPHON, syphon);
			json.addProperty(Constants.JSON.TICKS, ticks);
			json.addProperty(Constants.JSON.ALTAR_TIER, minimumTier);
		}
	}
}
