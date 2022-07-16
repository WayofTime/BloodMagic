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

public class ARCPotionRecipeBuilder extends BloodMagicRecipeBuilder<ARCPotionRecipeBuilder>
{
	private final Ingredient input;
	private int inputSize;
	private final Ingredient arcTool;
	private final FluidStackIngredient inputFluid;
	private final ItemStack output;
	private final FluidStack outputFluid;
	private final List<Pair<ItemStack, Pair<Double, Double>>> addedItems = new ArrayList<Pair<ItemStack, Pair<Double, Double>>>();
	private final boolean consumeIngredient;
	private double additionalMainOutputChance;

	protected ARCPotionRecipeBuilder(Ingredient input, Ingredient arcTool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid, boolean consumeIngredient, double additionalMainOutputChance)
	{
		super(bmSerializer("arc_potion"));
		this.input = input;
		this.arcTool = arcTool;
		this.inputFluid = inputFluid;
		this.output = output;
		this.outputFluid = outputFluid == null ? FluidStack.EMPTY : outputFluid;
		this.consumeIngredient = consumeIngredient;
		this.additionalMainOutputChance = additionalMainOutputChance;
	}

	public static ARCPotionRecipeBuilder arc(Ingredient input, Ingredient arcTool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid)
	{
		return new ARCPotionRecipeBuilder(input, arcTool, inputFluid, output, outputFluid, false, 0);
	}

	public static ARCPotionRecipeBuilder arcConsume(Ingredient input, Ingredient arcTool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid)
	{
		return new ARCPotionRecipeBuilder(input, arcTool, inputFluid, output, outputFluid, true, 0);
	}

	public ARCPotionRecipeBuilder setRequiredInputCount(int inputSize)
	{
		if (inputSize > 0)
			this.inputSize = inputSize;

		return this;
	}

	public ARCPotionRecipeBuilder addRandomOutput(ItemStack stack, double secondaryChance)
	{
		return addRandomOutput(stack, 0, secondaryChance);
	}

	public ARCPotionRecipeBuilder addRandomOutput(ItemStack stack, double mainChance, double secondaryChance)
	{
		if (addedItems.size() >= RecipeARC.MAX_RANDOM_OUTPUTS || mainChance < 0 || secondaryChance < 0)
		{
			return this;
		}

		addedItems.add(Pair.of(stack, Pair.of(mainChance, secondaryChance)));

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
			json.addProperty(Constants.JSON.INPUT_SIZE, inputSize);
			json.add(Constants.JSON.TOOL, arcTool.toJson());

			if (inputFluid != null)
				json.add(Constants.JSON.INPUT_FLUID, inputFluid.serialize());

			if (addedItems.size() > 0)
			{
				JsonArray mainArray = new JsonArray();
				for (Pair<ItemStack, Pair<Double, Double>> pair : addedItems)
				{
					JsonObject jsonObj = new JsonObject();
					jsonObj.addProperty(Constants.JSON.MAIN_CHANCE, pair.getValue().getLeft().floatValue());
					jsonObj.addProperty(Constants.JSON.CHANCE, pair.getValue().getRight().floatValue());
					jsonObj.add(Constants.JSON.TYPE, SerializerHelper.serializeItemStack(pair.getKey()));
					mainArray.add(jsonObj);
				}

				json.add(Constants.JSON.ADDEDOUTPUT, mainArray);
			}

			if (outputFluid != null && !outputFluid.isEmpty())
				json.add(Constants.JSON.OUTPUT_FLUID, SerializerHelper.serializeFluidStack(outputFluid));

			json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(output));
			json.addProperty("consumeingredient", consumeIngredient);
			json.addProperty("mainoutputchance", additionalMainOutputChance);
		}
	}
}