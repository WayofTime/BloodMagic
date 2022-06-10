package wayoftime.bloodmagic.recipe.flask;

import java.util.ArrayList;
import java.util.List;

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

public class RecipePotionIncreaseLength extends RecipePotionFlaskBase
{
	public Effect outputEffect;
	public double lengthDurationMod;

	public RecipePotionIncreaseLength(ResourceLocation id, List<Ingredient> input, Effect outputEffect, double lengthDurationMod, int syphon, int ticks, int minimumTier)
	{
		super(id, input, syphon, ticks, minimumTier);
		this.outputEffect = outputEffect;
		this.lengthDurationMod = lengthDurationMod;
	}

	@Override
	public IRecipeSerializer<? extends RecipePotionIncreaseLength> getSerializer()
	{
		return BloodMagicRecipeSerializers.POTIONLENGTH.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK;
	}

	@Override
	public boolean canModifyFlask(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
		for (EffectHolder holder : flaskEffectList)
		{
			if (holder.getPotion().equals(outputEffect))
			{
				return holder.getLengthDurationMod() < lengthDurationMod;
			}
		}

		return false;
	}

	@Override
	public void write(PacketBuffer buffer)
	{
		super.write(buffer);
		buffer.writeInt(Effect.getId(outputEffect));
		buffer.writeDouble(lengthDurationMod);
	}

	@Override
	public ItemStack getOutput(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
		ItemStack copyStack = flaskStack.copy();

		for (EffectHolder holder : flaskEffectList)
		{
			if (holder.getPotion().equals(outputEffect))
			{
				holder.setLengthDurationMod(lengthDurationMod);
			}
		}

		((ItemAlchemyFlask) copyStack.getItem()).setEffectHoldersOfFlask(copyStack, flaskEffectList);

		return copyStack;
	}

	@Override
	public int getPriority(List<EffectHolder> flaskEffectList)
	{
		for (int i = 0; i < flaskEffectList.size(); i++)
		{
			EffectHolder holder = flaskEffectList.get(i);
			if (holder.getPotion().equals(outputEffect))
			{
				return i + 1;
			}
		}

		return 0;
	}

	@Override
	public List<EffectHolder> getExampleEffectList()
	{
		List<EffectHolder> holderList = new ArrayList<>();
		holderList.add(new EffectHolder(outputEffect, 3600, 0, 1, 1));
		return holderList;
	}
}