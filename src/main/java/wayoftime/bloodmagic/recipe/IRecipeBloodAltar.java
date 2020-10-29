package wayoftime.bloodmagic.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class IRecipeBloodAltar extends RecipeBloodAltar
{
	public IRecipeBloodAltar(ResourceLocation id, Ingredient input, ItemStack output, int minimumTier, int syphon, int consumeRate, int drainRate)
	{
		super(id, input, output, minimumTier, syphon, consumeRate, drainRate);
	}

	@Override
	public IRecipeSerializer<RecipeBloodAltar> getSerializer()
	{
		return BloodMagicRecipeSerializers.ALTAR.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipeBloodAltar> getType()
	{
		return BloodMagicRecipeType.ALTAR;
	}
}
