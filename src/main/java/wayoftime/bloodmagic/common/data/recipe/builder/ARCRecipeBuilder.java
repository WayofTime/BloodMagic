package wayoftime.bloodmagic.common.data.recipe.builder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.api.SerializerHelper;
import wayoftime.bloodmagic.api.event.recipes.FluidStackIngredient;
import wayoftime.bloodmagic.api.impl.recipe.RecipeARC;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.util.Constants;

public class ARCRecipeBuilder extends BloodMagicRecipeBuilder<ARCRecipeBuilder>
{
	private final Ingredient input;
	private final Ingredient arcTool;
	private final FluidStackIngredient inputFluid;
	private final ItemStack output;
	private final FluidStack outputFluid;
	private final List<Pair<ItemStack, Double>> addedItems = new ArrayList<Pair<ItemStack, Double>>();

	protected ARCRecipeBuilder(Ingredient input, Ingredient arcTool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid)
	{
		super(bmSerializer("arc"));
		this.input = input;
		this.arcTool = arcTool;
		this.inputFluid = inputFluid;
		this.output = output;
		this.outputFluid = outputFluid == null ? FluidStack.EMPTY : outputFluid;
	}

	public static ARCRecipeBuilder arc(Ingredient input, Ingredient arcTool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid)
	{
		return new ARCRecipeBuilder(input, arcTool, inputFluid, output, outputFluid);
	}

	public ARCRecipeBuilder addRandomOutput(ItemStack stack, double chance)
	{
		if (addedItems.size() >= RecipeARC.MAX_RANDOM_OUTPUTS)
		{
			return this;
		}

		addedItems.add(Pair.of(stack, chance));

		return this;
	}

	@Override
	protected ARCRecipeResult getResult(ResourceLocation id)
	{
		return new ARCRecipeResult(id);
	}

	public class ARCRecipeResult extends RecipeResult
	{
		protected ARCRecipeResult(ResourceLocation id)
		{
			super(id);
		}

		@Override
		public void serialize(@Nonnull JsonObject json)
		{
			json.add(Constants.JSON.INPUT, input.serialize());
			json.add(Constants.JSON.TOOL, arcTool.serialize());

			if (inputFluid != null)
				json.add(Constants.JSON.INPUT_FLUID, inputFluid.serialize());

			if (addedItems.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (Pair<ItemStack, Double> pair : addedItems)
				{
					JsonObject jsonObj = new JsonObject();
					jsonObj.addProperty(Constants.JSON.CHANCE, pair.getValue().floatValue());
					jsonObj.add(Constants.JSON.TYPE, SerializerHelper.serializeItemStack(pair.getKey()));
					mainArray.add(jsonObj);
				}

				json.add(Constants.JSON.ADDEDOUTPUT, mainArray);
			}

			if (outputFluid != null && !outputFluid.isEmpty())
				json.add(Constants.JSON.OUTPUT_FLUID, SerializerHelper.serializeFluidStack(outputFluid));

			json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(output));
		}
	}
}