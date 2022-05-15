package wayoftime.bloodmagic.recipe.flask;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.EffectHolder;

public class RecipePotionTransform extends RecipePotionFlaskBase
{
	public List<Pair<Effect, Integer>> outputEffectList = new ArrayList<>();
	public List<Effect> inputEffectList;

	public RecipePotionTransform(ResourceLocation id, List<Ingredient> input, Effect outputEffect, int baseDuration, List<Effect> inputEffectList, int syphon, int ticks, int minimumTier)
	{
		super(id, input, syphon, ticks, minimumTier);
		outputEffectList.add(Pair.of(outputEffect, baseDuration));
		this.inputEffectList = inputEffectList;
	}

	@Override
	public IRecipeSerializer<? extends RecipePotionTransform> getSerializer()
	{
		return null;
//		return BloodMagicRecipeSerializers.POTIONEFFECT.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK;
	}

	@Override
	public boolean canModifyFlask(List<EffectHolder> flaskEffectList)
	{
		if (flaskEffectList.size() < inputEffectList.size())
			return false;

		int duplicateCount = getDuplicateEffects(flaskEffectList);
		if (duplicateCount >= outputEffectList.size())
		{
			return false;
		}

		List<Effect> recipeInput = new ArrayList<>(inputEffectList);

		for (int i = 0; i < flaskEffectList.size(); i++)
		{
			Effect flaskEffect = flaskEffectList.get(i).getPotion();
//			if (flaskEffect.equals(outputEffect))
//			{
//				return false;
//			}
			boolean matched = false;
			for (int j = 0; j < recipeInput.size(); j++)
			{
				Effect ingredient = recipeInput.get(j);
				if (ingredient.equals(flaskEffect))
				{
					recipeInput.remove(j);
					matched = true;
					break;
				}
			}

			if (!matched)
				continue;
		}

		return recipeInput.isEmpty();
	}

	// Checks the contained effects of the flask. If they are a match, also compares
	// base duration.
	protected int getDuplicateEffects(List<EffectHolder> flaskEffectList)
	{
		int duplicateCount = 0;
		List<Pair<Effect, Integer>> recipeOutput = new ArrayList<>(outputEffectList);

		for (int i = 0; i < flaskEffectList.size(); i++)
		{
			Effect flaskEffect = flaskEffectList.get(i).getPotion();
			int flaskBaseDuration = flaskEffectList.get(i).getBaseDuration();

			for (int j = 0; j < recipeOutput.size(); j++)
			{
				Pair<Effect, Integer> output = recipeOutput.get(j);
				if (output.getKey().equals(flaskEffect) && output.getValue() <= flaskBaseDuration)
				{
					recipeOutput.remove(j);
					duplicateCount++;
					break;
				}
			}
		}

		return duplicateCount;
	}

	@Override
	public void write(PacketBuffer buffer)
	{
		super.write(buffer);
		buffer.writeInt(Effect.getId(outputEffect));
		buffer.writeInt(baseDuration);
	}

	@Override
	public ItemStack getOutput(ItemStack flaskStack, List<EffectHolder> flaskEffectList, List<ItemStack> inputs)
	{
		ItemStack copyStack = flaskStack.copy();

		flaskEffectList.add(new EffectHolder(outputEffect, baseDuration, 0, 1, 1));
		((ItemAlchemyFlask) copyStack.getItem()).setEffectHoldersOfFlask(copyStack, flaskEffectList);

		return copyStack;
	}

	@Override
	public int getPriority(List<EffectHolder> flaskEffectList)
	{
		return 1;
	}
}
