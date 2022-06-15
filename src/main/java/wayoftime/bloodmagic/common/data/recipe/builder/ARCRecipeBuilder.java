package wayoftime.bloodmagic.common.data.recipe.builder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

public class ARCRecipeBuilder extends BloodMagicRecipeBuilder<ARCRecipeBuilder>
{
	private final Ingredient input;
	private final Ingredient arcTool;
	private final FluidStackIngredient inputFluid;
	private final ItemStack output;
	private final FluidStack outputFluid;
	private final List<Pair<ItemStack, Double>> addedItems = new ArrayList<Pair<ItemStack, Double>>();
	private final boolean consumeIngredient;

	protected ARCRecipeBuilder(Ingredient input, Ingredient arcTool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid, boolean consumeIngredient)
	{
		super(bmSerializer("arc"));
		this.input = input;
		this.arcTool = arcTool;
		this.inputFluid = inputFluid;
		this.output = output;
		this.outputFluid = outputFluid == null ? FluidStack.EMPTY : outputFluid;
		this.consumeIngredient = consumeIngredient;
	}

	public static ARCRecipeBuilder arc(Ingredient input, Ingredient arcTool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid)
	{
		return new ARCRecipeBuilder(input, arcTool, inputFluid, output, outputFluid, false);
	}

	public static ARCRecipeBuilder arcConsume(Ingredient input, Ingredient arcTool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid)
	{
		return new ARCRecipeBuilder(input, arcTool, inputFluid, output, outputFluid, true);
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
		public void serializeRecipeData(@Nonnull JsonObject json)
		{
			json.add(Constants.JSON.INPUT, input.toJson());
			json.add(Constants.JSON.TOOL, arcTool.toJson());

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
			json.addProperty("consumeingredient", consumeIngredient);
		}
	}
}