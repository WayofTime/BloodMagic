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
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.recipe.EffectHolder;

public class RecipePotionFlaskTransform extends RecipePotionFlaskBase
{
	public ItemStack output;

	public RecipePotionFlaskTransform(ResourceLocation id, List<Ingredient> input, ItemStack output, int syphon, int ticks, int minimumTier)
	{
		super(id, input, syphon, ticks, minimumTier);
		this.output = output;
	}

	@Override
	public RecipeSerializer<? extends RecipePotionFlaskTransform> getSerializer()
	{
		return BloodMagicRecipeSerializers.POTIONFLASKTRANSFORM.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK.get();
	}

	@Override
	public boolean canModifyFlask(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
		return flaskStack.getItem() != output.getItem();
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		super.write(buffer);
		buffer.writeItem(output);
	}

	@Override
	public ItemStack getOutput(ItemStack flaskStack, List<EffectHolder> flaskEffectList)
	{
		ItemStack copyStack = output.copy();

		copyStack.setTag(flaskStack.getTag());
		copyStack.setDamageValue(flaskStack.getDamageValue());

		return copyStack;
	}

	@Override
	public int getPriority(List<EffectHolder> flaskEffectList)
	{
		return 0;
	}

	@Override
	public List<EffectHolder> getExampleEffectList()
	{
		List<EffectHolder> holderList = new ArrayList<>();

		holderList.add(new EffectHolder(MobEffects.MOVEMENT_SPEED, 3600, 0, 1, 1));

		return holderList;
	}
}
