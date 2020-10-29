package wayoftime.bloodmagic.recipe;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.api.impl.recipe.RecipeTartaricForge;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class IRecipeTartaricForge extends RecipeTartaricForge
{
	public IRecipeTartaricForge(ResourceLocation id, @Nonnull List<Ingredient> input, @Nonnull ItemStack output, @Nonnegative double minimumSouls, @Nonnegative double soulDrain)
	{
		super(id, input, output, minimumSouls, soulDrain);
	}

	@Override
	public IRecipeSerializer<RecipeTartaricForge> getSerializer()
	{
		return BloodMagicRecipeSerializers.TARTARIC.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipeTartaricForge> getType()
	{
		return BloodMagicRecipeType.TARTARICFORGE;
	}
}
