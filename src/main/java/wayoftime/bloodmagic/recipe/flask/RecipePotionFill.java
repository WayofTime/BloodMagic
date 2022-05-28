package wayoftime.bloodmagic.recipe.flask;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
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
	public IRecipeSerializer<? extends RecipePotionFill> getSerializer()
	{
		return BloodMagicRecipeSerializers.POTIONFILL.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK;
	}

	@Override
	public boolean canModifyFlask(List<EffectHolder> flaskEffectList)
	{
		return flaskEffectList.size() > 0;
	}

	@Override
	public void write(PacketBuffer buffer)
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
		copyStack.setDamage(0);

		return copyStack;
	}

	@Override
	public ItemStack getExamplePotionFlask()
	{
		ItemStack flaskStack = new ItemStack(BloodMagicItems.ALCHEMY_FLASK.get());
		flaskStack.setDamage(8);
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

		holderList.add(new EffectHolder(Effects.SPEED, 3600, 0, 1, 1));
		holderList.add(new EffectHolder(Effects.FIRE_RESISTANCE, 3600, 0, 1, 1));
		holderList.add(new EffectHolder(Effects.HASTE, 3600, 0, 1, 1));

		return holderList;
	}
}
