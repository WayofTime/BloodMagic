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
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.EffectHolder;

public class RecipePotionCycle extends RecipePotionFlaskBase
{
	public int numCycles;

	public RecipePotionCycle(ResourceLocation id, List<Ingredient> input, int numCycles, int syphon, int ticks, int minimumTier)
	{
		super(id, input, syphon, ticks, minimumTier);
		this.numCycles = numCycles;
	}

	@Override
	public IRecipeSerializer<? extends RecipePotionCycle> getSerializer()
	{
		return BloodMagicRecipeSerializers.POTIONCYCLE.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK;
	}

	@Override
	public boolean canModifyFlask(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
		return flaskEffectList.size() >= 2;
	}

	@Override
	public void write(PacketBuffer buffer)
	{
		super.write(buffer);
		buffer.writeInt(numCycles);
	}

	@Override
	public ItemStack getOutput(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
		ItemStack copyStack = flaskStack.copy();

		List<EffectHolder> flaskEffectCopyList = new ArrayList<>(flaskEffectList);

		for (int i = 0; i < numCycles; i++)
		{
			EffectHolder holder = flaskEffectCopyList.remove(0);
			flaskEffectCopyList.add(holder);
		}

		((ItemAlchemyFlask) copyStack.getItem()).setEffectHoldersOfFlask(copyStack, flaskEffectCopyList);

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

		holderList.add(new EffectHolder(Effects.SPEED, 3600, 0, 1, 1));
		holderList.add(new EffectHolder(Effects.FIRE_RESISTANCE, 3600, 0, 1, 1));
		holderList.add(new EffectHolder(Effects.HASTE, 3600, 0, 1, 1));

		return holderList;
	}
}
