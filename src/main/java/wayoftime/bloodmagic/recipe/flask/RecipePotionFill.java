package wayoftime.bloodmagic.recipe.flask;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.EffectHolder;

public class RecipePotionFill extends RecipePotionFlaskBase
{
	public int maxEffects;

	public RecipePotionFill(ResourceLocation id, List<Ingredient> input, int maxEffects, int syphon, int ticks, int minimumTier)
	{
		super(id, input, syphon, ticks, minimumTier);
		this.maxEffects = maxEffects;
	}

	@Override
	public RecipeSerializer<? extends RecipePotionFill> getSerializer()
	{
		return BloodMagicRecipeSerializers.POTIONFILL.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK.get();
	}

	@Override
	public boolean canModifyFlask(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
		return flaskEffectList.size() > 0;
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		super.write(buffer);
		buffer.writeInt(maxEffects);
	}

	@Override
	public ItemStack getOutput(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
		ItemStack copyStack = flaskStack.copy();

		List<EffectHolder> flaskEffectCopyList = new ArrayList<>();
		for (int i = 0; i < Math.min(flaskEffectList.size(), maxEffects); i++)
		{
			flaskEffectCopyList.add(flaskEffectList.get(i));
		}

		((ItemAlchemyFlask) copyStack.getItem()).setEffectHoldersOfFlask(copyStack, flaskEffectCopyList);
		copyStack.setDamageValue(0);

		return copyStack;
	}

	@Override
	public ItemStack getExamplePotionFlask()
	{
		ItemStack flaskStack = new ItemStack(BloodMagicItems.ALCHEMY_FLASK.get());
		flaskStack.setDamageValue(8);
		((ItemAlchemyFlask) flaskStack.getItem()).setEffectHoldersOfFlask(flaskStack, getExampleEffectList());
		((ItemAlchemyFlask) flaskStack.getItem()).resyncEffectInstances(flaskStack);

		return flaskStack;
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

		holderList.add(new EffectHolder(MobEffects.MOVEMENT_SPEED, 3600, 0, 1, 1));
		holderList.add(new EffectHolder(MobEffects.FIRE_RESISTANCE, 3600, 0, 1, 1));
		holderList.add(new EffectHolder(MobEffects.DIG_SPEED, 3600, 0, 1, 1));

		return holderList;
	}
}
