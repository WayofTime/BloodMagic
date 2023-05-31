package wayoftime.bloodmagic.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.util.RandomSource;
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
	private int inputSize = 1;
	@Nonnull
	private final Ingredient arc_tool;
	private final FluidStackIngredient inputFluid;
	@Nonnull
	protected final ItemStack output;
	protected final FluidStack outputFluid;
	private final boolean consumeIngredient;

	protected final List<Pair<ItemStack, Pair<Double, Double>>> addedItems;

	public RecipeARC(ResourceLocation id, Ingredient input, Ingredient arc_tool, FluidStackIngredient inputFluid, ItemStack output, FluidStack outputFluid, boolean consumeIngredient)
	{
		this(id, input, 1, arc_tool, inputFluid, output, new ArrayList<Pair<ItemStack, Pair<Double, Double>>>(), outputFluid, consumeIngredient);
	}

	public RecipeARC(ResourceLocation id, Ingredient input, int inputSize, Ingredient arc_tool, FluidStackIngredient inputFluid, ItemStack output, List<Pair<ItemStack, Pair<Double, Double>>> addedItems, FluidStack outputFluid, boolean consumeIngredient)
	{
		super(id);
		this.input = input;
		this.arc_tool = arc_tool;
		this.inputFluid = inputFluid;
		this.output = output;
		this.addedItems = addedItems;
		this.outputFluid = outputFluid;
		this.consumeIngredient = consumeIngredient;
		this.inputSize = inputSize;
	}

	public RecipeARC addRandomOutput(ItemStack stack, double base, double secondary)
	{
		if (addedItems.size() >= MAX_RANDOM_OUTPUTS)
		{
			return this;
		}

		addedItems.add(Pair.of(stack, Pair.of(base, secondary)));

		return this;
	}

	public RecipeARC addRandomOutput(ItemStack stack, double secondary)
	{
		return addRandomOutput(stack, 0, secondary);
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

	public int getRequiredInputCount()
	{
		return this.inputSize;
	}

	public List<ItemStack> getAllListedOutputs()
	{
		return getAllListedOutputs(ItemStack.EMPTY, ItemStack.EMPTY);
	}

	public List<ItemStack> getAllListedOutputs(ItemStack inputStack, ItemStack toolStack)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();

		list.add(output.copy());
		for (Pair<ItemStack, Pair<Double, Double>> pair : addedItems)
		{
			list.add(pair.getLeft().copy());
		}

		return list;
	}

	public List<ItemStack> getAllOutputs(RandomSource rand, ItemStack inputStack, ItemStack toolStack, double secondaryBonus)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();

		list.add(output.copy());

		for (Pair<ItemStack, Pair<Double, Double>> pair : addedItems)
		{
			Pair<Double, Double> bonus = pair.getRight();
			if (rand.nextDouble() < (bonus.getLeft() + secondaryBonus * bonus.getRight()))
				list.add(pair.getLeft().copy());
		}

		return list;
	}

	public List<Pair<Double, Double>> getAllOutputChances()
	{
		List<Pair<Double, Double>> list = new ArrayList<>();
		for (Pair<ItemStack, Pair<Double, Double>> entry : addedItems)
		{
			list.add(entry.getRight());
		}
//		int size = addedItems.size();

//		double[] chanceArray = new double[size];
//		for (int i = 0; i < size; i++)
//		{
//			chanceArray[i] = addedItems.get(i).getRight();
//		}

		return list;
	}

	public boolean getConsumeIngredient()
	{
		return consumeIngredient;
	}

	public boolean breakTool()
	{
		return true;
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		input.toNetwork(buffer);
		buffer.writeInt(inputSize);
		arc_tool.toNetwork(buffer);
		buffer.writeItem(output);
		buffer.writeInt(addedItems.size());
		for (Pair<ItemStack, Pair<Double, Double>> pair : addedItems)
		{
			buffer.writeItem(pair.getLeft());
			buffer.writeDouble(pair.getValue().getKey());
			buffer.writeDouble(pair.getValue().getValue());
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
	public RecipeSerializer<? extends RecipeARC> getSerializer()
	{
		return BloodMagicRecipeSerializers.ARC.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipeARC> getType()
	{
		return BloodMagicRecipeType.ARC.get();
	}
}
