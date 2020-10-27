package wayoftime.bloodmagic.api.impl.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public abstract class RecipeARC extends BloodMagicRecipe
{
	public static final int MAX_RANDOM_OUTPUTS = 3;

	@Nonnull
	private final Ingredient input;
	@Nonnull
	private final Ingredient arc_tool;
	@Nonnull
	private final ItemStack output;

	private final List<Pair<ItemStack, Double>> addedItems;

	protected RecipeARC(ResourceLocation id, Ingredient input, Ingredient arc_tool, ItemStack output)
	{
		this(id, input, arc_tool, output, new ArrayList<Pair<ItemStack, Double>>());
	}

	protected RecipeARC(ResourceLocation id, Ingredient input, Ingredient arc_tool, ItemStack output, List<Pair<ItemStack, Double>> addedItems)
	{
		super(id);
		this.input = input;
		this.arc_tool = arc_tool;
		this.output = output;
		this.addedItems = addedItems;
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

	@Override
	public final NonNullList<Ingredient> getIngredients()
	{
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(getInput());
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

	@Override
	public void write(PacketBuffer buffer)
	{
		input.write(buffer);
		arc_tool.write(buffer);
		buffer.writeItemStack(output);
		buffer.writeInt(addedItems.size());
		for (Pair<ItemStack, Double> pair : addedItems)
		{
			buffer.writeItemStack(pair.getLeft());
			buffer.writeDouble(pair.getValue());
		}
	}
}
