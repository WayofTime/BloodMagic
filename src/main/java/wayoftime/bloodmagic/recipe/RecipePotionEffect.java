package wayoftime.bloodmagic.recipe;

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

public class RecipePotionEffect extends RecipePotionFlaskBase
{
	public Effect outputEffect;
	public int baseDuration;

	public RecipePotionEffect(ResourceLocation id, List<Ingredient> input, Effect outputEffect, int baseDuration, int syphon, int ticks, int minimumTier)
	{
		super(id, input, syphon, ticks, minimumTier);
		this.outputEffect = outputEffect;
		this.baseDuration = baseDuration;
	}

	@Override
	public IRecipeSerializer<? extends RecipePotionEffect> getSerializer()
	{
		return BloodMagicRecipeSerializers.POTIONEFFECT.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK;
	}

	@Override
	public boolean canModifyFlask(List<EffectHolder> flaskEffectList)
	{
		for (EffectHolder holder : flaskEffectList)
		{
			if (holder.getPotion().equals(outputEffect))
			{
				return false;
			}
		}

		return true;
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

//		Collection<EffectInstance> flaskInstanceList = PotionUtils.getEffectsFromStack(flaskStack);
//		flaskInstanceList.add(new EffectInstance(outputEffect, baseDuration));
		flaskEffectList.add(new EffectHolder(outputEffect, baseDuration, 0, 1, 1));
//		((ItemAlchemyFlask) copyStack.getItem()).setEffectsOfFlask(copyStack, flaskInstanceList);
		((ItemAlchemyFlask) copyStack.getItem()).setEffectHoldersOfFlask(copyStack, flaskEffectList);

		return copyStack;
	}
}
