package wayoftime.bloodmagic.recipe;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class RecipeLivingDowngrade extends BloodMagicRecipe
{
	@Nonnull
	private final Ingredient input;
	@Nonnull
	private final ResourceLocation livingArmourRL;

	public RecipeLivingDowngrade(ResourceLocation id, @Nonnull Ingredient input, @Nonnull ResourceLocation livingArmourRL)
	{
		super(id);
		Preconditions.checkNotNull(input, "input cannot be null.");
		Preconditions.checkNotNull(livingArmourRL, "output cannot be null.");

		this.input = input;
		this.livingArmourRL = livingArmourRL;
	}

	@Nonnull
	public final Ingredient getInput()
	{
		return input;
	}

	@Override
	public final NonNullList<Ingredient> getIngredients()
	{
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(getInput());
		return list;
	}

	@Nonnull
	public final ResourceLocation getLivingArmourResource()
	{
		return livingArmourRL;
	}

	@Override
	public void write(PacketBuffer buffer)
	{
		input.write(buffer);
		buffer.writeResourceLocation(livingArmourRL);
	}

	@Override
	public IRecipeSerializer<RecipeLivingDowngrade> getSerializer()
	{
		return BloodMagicRecipeSerializers.LIVINGDOWNGRADE.getRecipeSerializer();
	}

	@Override
	public IRecipeType<RecipeLivingDowngrade> getType()
	{
		return BloodMagicRecipeType.LIVINGDOWNGRADE;
	}
}