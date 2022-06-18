package wayoftime.bloodmagic.recipe.flask;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.EffectHolder;

public class RecipePotionTransform extends RecipePotionFlaskBase
{
	public List<Pair<MobEffect, Integer>> outputEffectList;
	public List<MobEffect> inputEffectList;

	public RecipePotionTransform(ResourceLocation id, List<Ingredient> input, List<Pair<MobEffect, Integer>> outputEffectList, List<MobEffect> inputEffectList, int syphon, int ticks, int minimumTier)
	{
		super(id, input, syphon, ticks, minimumTier);
		this.outputEffectList = outputEffectList;
		this.inputEffectList = inputEffectList;
	}

	@Override
	public RecipeSerializer<? extends RecipePotionTransform> getSerializer()
	{
		return BloodMagicRecipeSerializers.POTIONTRANSFORM.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK.get();
	}

	@Override
	public boolean canModifyFlask(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
//		System.out.println("Passed ingredient check");
		if (flaskEffectList.size() < inputEffectList.size())
			return false;

//		System.out.println("Checking if the effects are valid. Recipe inputs: " + inputEffectList.size() + ", recipe outputs: " + outputEffectList.size());

		int duplicateCount = getDuplicateEffects(flaskEffectList);
		if (duplicateCount >= outputEffectList.size())
		{
			return false;
		}

		List<MobEffect> recipeInput = new ArrayList<>(inputEffectList);

		for (int i = 0; i < flaskEffectList.size(); i++)
		{
			MobEffect flaskEffect = flaskEffectList.get(i).getPotion();

			boolean matched = false;
			for (int j = 0; j < recipeInput.size(); j++)
			{
				MobEffect ingredient = recipeInput.get(j);
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
		List<Pair<MobEffect, Integer>> recipeOutput = new ArrayList<>(outputEffectList);

		for (int i = 0; i < flaskEffectList.size(); i++)
		{
			MobEffect flaskEffect = flaskEffectList.get(i).getPotion();
			int flaskBaseDuration = flaskEffectList.get(i).getBaseDuration();

			for (int j = 0; j < recipeOutput.size(); j++)
			{
				Pair<MobEffect, Integer> output = recipeOutput.get(j);
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
	public void write(FriendlyByteBuf buffer)
	{
		super.write(buffer);
		buffer.writeInt(outputEffectList.size());
		for (Pair<MobEffect, Integer> effectHolder : outputEffectList)
		{
			buffer.writeInt(MobEffect.getId(effectHolder.getKey()));
			buffer.writeInt(effectHolder.getValue());
		}

		buffer.writeInt(inputEffectList.size());
		for (MobEffect effect : inputEffectList)
		{
			buffer.writeInt(MobEffect.getId(effect));
		}
	}

	@Override
	public ItemStack getOutput(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
		ItemStack copyStack = flaskStack.copy();

		boolean savePotencies = outputEffectList.size() == 1 && inputEffectList.size() == 1;
		int amplifier = 0;
		double ampDurMod = 1;
		double lengthDurMod = 1;

		// Remove inputs
		List<EffectHolder> flaskEffectCopyList = new ArrayList<>(flaskEffectList);
		for (int i = 0; i < inputEffectList.size(); i++)
		{
			MobEffect inputEffect = inputEffectList.get(i);
//			int outputBaseDuration = outputEffectList.get(i).getValue();

			for (int j = 0; j < flaskEffectCopyList.size(); j++)
			{
				EffectHolder flaskEffect = flaskEffectCopyList.get(j);
				if (flaskEffect.getPotion().equals(inputEffect))
				{
					if (savePotencies)
					{
						amplifier = flaskEffect.getAmplifier();
						ampDurMod = flaskEffect.getAmpDurationMod();
						lengthDurMod = flaskEffect.getLengthDurationMod();
					}

					flaskEffectCopyList.remove(j);
					break;
				}
			}
		}

		// Check if the outputs already exist in here. If it does, check/change the
		// duration
		boolean[] alreadyAddedArray = new boolean[outputEffectList.size()];

		outputLoop: for (int i = 0; i < outputEffectList.size(); i++)
		{
			MobEffect outputEffect = outputEffectList.get(i).getKey();
			int outputBaseDuration = outputEffectList.get(i).getValue();

			for (int j = 0; j < flaskEffectCopyList.size(); j++)
			{
				EffectHolder flaskEffect = flaskEffectCopyList.get(j);
				if (flaskEffect.getPotion().equals(outputEffect))
				{
					alreadyAddedArray[i] = true;
					if (flaskEffect.getBaseDuration() < outputBaseDuration)
					{
						flaskEffect.setBaseDuration(outputBaseDuration);
					}

					continue outputLoop;
				}
			}

			alreadyAddedArray[i] = false;
		}

		for (int i = 0; i < outputEffectList.size(); i++)
		{
			flaskEffectCopyList.add(new EffectHolder(outputEffectList.get(i).getKey(), outputEffectList.get(i).getValue(), amplifier, ampDurMod, lengthDurMod));
		}

		((ItemAlchemyFlask) copyStack.getItem()).setEffectHoldersOfFlask(copyStack, flaskEffectCopyList);

		return copyStack;
	}

	@Override
	public int getPriority(List<EffectHolder> flaskEffectList)
	{
		int prio = 0;
		for (int i = 0; i < flaskEffectList.size(); i++)
		{
			EffectHolder holder = flaskEffectList.get(i);
			for (int j = 0; j < inputEffectList.size(); j++)
			{
				if (holder.getPotion().equals(inputEffectList.get(j)))
				{
					prio += i + 1;
				}
			}
		}

		return prio;
	}

	@Override
	public List<EffectHolder> getExampleEffectList()
	{
		List<EffectHolder> holderList = new ArrayList<>();
		for (MobEffect inputEffect : inputEffectList)
		{
			holderList.add(new EffectHolder(inputEffect, 3600, 0, 1, 1));
		}

		return holderList;
	}
}
