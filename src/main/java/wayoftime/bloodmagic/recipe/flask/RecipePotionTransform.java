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
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.EffectHolder;

public class RecipePotionTransform extends RecipePotionFlaskBase
{
	public List<Pair<Effect, Integer>> outputEffectList;
	public List<Effect> inputEffectList;

	public RecipePotionTransform(ResourceLocation id, List<Ingredient> input, List<Pair<Effect, Integer>> outputEffectList, List<Effect> inputEffectList, int syphon, int ticks, int minimumTier)
	{
		super(id, input, syphon, ticks, minimumTier);
		this.outputEffectList = outputEffectList;
		this.inputEffectList = inputEffectList;
	}

	@Override
	public IRecipeSerializer<? extends RecipePotionTransform> getSerializer()
	{
		return BloodMagicRecipeSerializers.POTIONTRANSFORM.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK;
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

		List<Effect> recipeInput = new ArrayList<>(inputEffectList);

		for (int i = 0; i < flaskEffectList.size(); i++)
		{
			Effect flaskEffect = flaskEffectList.get(i).getPotion();

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
		buffer.writeInt(outputEffectList.size());
		for (Pair<Effect, Integer> effectHolder : outputEffectList)
		{
			buffer.writeInt(Effect.getId(effectHolder.getKey()));
			buffer.writeInt(effectHolder.getValue());
		}

		buffer.writeInt(inputEffectList.size());
		for (Effect effect : inputEffectList)
		{
			buffer.writeInt(Effect.getId(effect));
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
			Effect inputEffect = inputEffectList.get(i);
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
			Effect outputEffect = outputEffectList.get(i).getKey();
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
		for (Effect inputEffect : inputEffectList)
		{
			holderList.add(new EffectHolder(inputEffect, 3600, 0, 1, 1));
		}

		return holderList;
	}
}
