package wayoftime.bloodmagic.recipe.flask;

import java.util.ArrayList;
import java.util.List;

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

public class RecipePotionEffect extends RecipePotionFlaskBase
{
	public MobEffect outputEffect;
	public int baseDuration;

	public RecipePotionEffect(ResourceLocation id, List<Ingredient> input, MobEffect outputEffect, int baseDuration, int syphon, int ticks, int minimumTier)
	{
		super(id, input, syphon, ticks, minimumTier);
		this.outputEffect = outputEffect;
		this.baseDuration = baseDuration;
	}

	@Override
	public RecipeSerializer<? extends RecipePotionEffect> getSerializer()
	{
		return BloodMagicRecipeSerializers.POTIONEFFECT.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK.get();
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
	public void write(FriendlyByteBuf buffer)
	{
		super.write(buffer);
		buffer.writeInt(MobEffect.getId(outputEffect));
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
