package wayoftime.bloodmagic.recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;

public class RecipeARCPotion extends RecipeARC
{
	public RecipeARCPotion(ResourceLocation id, Ingredient input, int inputSize, Ingredient arc_tool, FluidStackIngredient inputFluid, ItemStack output, List<Pair<ItemStack, Pair<Double, Double>>> addedItems, FluidStack outputFluid, boolean consumeIngredient)
	{
		super(id, input, inputSize, arc_tool, inputFluid, output, addedItems, outputFluid, consumeIngredient);
	}

	public List<ItemStack> getAllListedOutputs(ItemStack inputStack, ItemStack toolStack)
	{
		if (toolStack.isEmpty())
		{
			return super.getAllListedOutputs(inputStack, toolStack);
		}

		List<ItemStack> list = new ArrayList<ItemStack>();
		Collection<MobEffectInstance> collection = PotionUtils.getCustomEffects(toolStack);
		ItemStack outputCopyStack = output.copy();
		PotionUtils.setCustomEffects(outputCopyStack, collection);

		list.add(outputCopyStack);
		for (Pair<ItemStack, Pair<Double, Double>> pair : addedItems)
		{
			list.add(pair.getLeft().copy());
		}

		return list;
	}

	public List<ItemStack> getAllOutputs(RandomSource rand, ItemStack inputStack, ItemStack toolStack, double secondaryBonus)
	{
		if (toolStack.isEmpty())
		{
			return super.getAllOutputs(rand, inputStack, toolStack, secondaryBonus);
		}

		List<ItemStack> list = new ArrayList<ItemStack>();
		Collection<MobEffectInstance> collection = PotionUtils.getCustomEffects(toolStack);
		ItemStack outputCopyStack = output.copy();
		PotionUtils.setCustomEffects(outputCopyStack, collection);

		list.add(outputCopyStack);

		for (Pair<ItemStack, Pair<Double, Double>> pair : addedItems)
		{
			Pair<Double, Double> bonus = pair.getRight();
			if (rand.nextDouble() < (bonus.getLeft() + secondaryBonus * bonus.getRight()))
				list.add(pair.getLeft().copy());
		}

		return list;
	}

	@Override
	public boolean breakTool()
	{
		return false;
	}

	@Override
	public RecipeSerializer<? extends RecipeARCPotion> getSerializer()
	{
		return BloodMagicRecipeSerializers.ARC_POTION.getRecipeSerializer();
	}
}
