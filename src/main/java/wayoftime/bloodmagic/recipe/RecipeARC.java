package wayoftime.bloodmagic.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;

public class RecipeARC extends BloodMagicRecipe
{
	public static final int MAX_RANDOM_OUTPUTS = 3;

	@Nonnull
	private final Ingredient input;
	@Nonnull
	private final Ingredient arc_tool;
	private final FluidStackIngredient inputFluid;
	@Nonnull
	private final ItemStack output;
	private final FluidStack outputFluid;
	private final boolean consumeIngredient;

	private final List<Pair<ItemStack, Double>> addedItems;

	public RecipeARC(ResourceLocation id, Ingredient input, Ingredient arc_tool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid, boolean consumeIngredient)
	{
		this(id, input, arc_tool, inputFluid, output, new ArrayList<Pair<ItemStack, Double>>(), outputFluid, consumeIngredient);
	}

	public RecipeARC(ResourceLocation id, Ingredient input, Ingredient arc_tool, FluidStackIngredient inputFluid, ItemStack output, List<Pair<ItemStack, Double>> addedItems, FluidStack outputFluid, boolean consumeIngredient)
	{
		super(id);
		this.input = input;
		this.arc_tool = arc_tool;
		this.inputFluid = inputFluid;
		this.output = output;
		this.addedItems = addedItems;
		this.outputFluid = outputFluid;
		this.consumeIngredient = consumeIngredient;
	}

	public RecipeARC addRandomOutput(ItemStack stack, double chance)
	{
		if (addedItems.size() >= MAX_RANDOM_OUTPUTS)
		{
			return this;
		}

		addedItems.add(Pair.of(stack, chance));

		return this;
	}

	@Nonnull
	public final Ingredient getInput()
	{
		return input;
	}

	@Nonnull
	public final Ingredient getTool()
	{
		return arc_tool;
	}

	public final FluidStackIngredient getFluidIngredient()
	{
		return inputFluid;
	}

	public final FluidStack getFluidOutput()
	{
		return outputFluid;
	}

	@Override
	public final NonNullList<Ingredient> getIngredients()
	{
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(getInput());
		list.add(getTool());
		return list;
	}

	public List<ItemStack> getAllListedOutputs()
	{
		List<ItemStack> list = new ArrayList<ItemStack>();

		list.add(output.copy());
		for (Pair<ItemStack, Double> pair : addedItems)
		{
			list.add(pair.getLeft().copy());
		}

		return list;
	}

	public List<ItemStack> getAllOutputs(Random rand)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();

		list.add(output.copy());
		for (Pair<ItemStack, Double> pair : addedItems)
		{
			if (rand.nextDouble() < pair.getRight())
				list.add(pair.getLeft().copy());
		}

		return list;
	}

	public double[] getAllOutputChances()
	{
		int size = addedItems.size();

		double[] chanceArray = new double[size];
		for (int i = 0; i < size; i++)
		{
			chanceArray[i] = addedItems.get(i).getRight();
		}

		return chanceArray;
	}

	public boolean getConsumeIngredient()
	{
		return consumeIngredient;
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		input.toNetwork(buffer);
		arc_tool.toNetwork(buffer);
		buffer.writeItem(output);
		buffer.writeInt(addedItems.size());
		for (Pair<ItemStack, Double> pair : addedItems)
		{
			buffer.writeItem(pair.getLeft());
			buffer.writeDouble(pair.getValue());
		}

		buffer.writeBoolean(inputFluid != null);
		if (inputFluid != null)
		{
			inputFluid.write(buffer);
		}

		buffer.writeBoolean(outputFluid != null);
		if (outputFluid != null)
		{
			outputFluid.writeToPacket(buffer);
		}
		buffer.writeBoolean(consumeIngredient);
	}

	@Override
	public RecipeSerializer<RecipeARC> getSerializer()
	{
		return BloodMagicRecipeSerializers.ARC.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipeARC> getType()
	{
		return BloodMagicRecipeType.ARC.get();
	}
}
