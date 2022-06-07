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
	public boolean canModifyFlask(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
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
	public ItemStack getOutput(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
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

	@Override
	public List<EffectHolder> getExampleEffectList()
	{
		List<EffectHolder> holderList = new ArrayList<>();
		return holderList;
	}
}
