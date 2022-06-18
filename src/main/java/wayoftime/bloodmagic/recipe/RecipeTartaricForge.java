package wayoftime.bloodmagic.recipe;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class RecipeTartaricForge extends BloodMagicRecipe
{
	@Nonnull
	private final List<Ingredient> input;
	@Nonnull
	private final ItemStack output;
	@Nonnegative
	private final double minimumSouls;
	@Nonnegative
	private final double soulDrain;

	public RecipeTartaricForge(ResourceLocation id, @Nonnull List<Ingredient> input, @Nonnull ItemStack output, @Nonnegative double minimumSouls, @Nonnegative double soulDrain)
	{
		super(id);
		Preconditions.checkNotNull(input, "input cannot be null.");
		Preconditions.checkNotNull(output, "output cannot be null.");
		Preconditions.checkArgument(minimumSouls >= 0, "minimumSouls cannot be negative.");
		Preconditions.checkArgument(soulDrain >= 0, "soulDrain cannot be negative.");

		this.input = input;
		this.output = output;
		this.minimumSouls = minimumSouls;
		this.soulDrain = soulDrain;
	}

	@Nonnull
	public final List<Ingredient> getInput()
	{
		return input;
	}

	@Nonnull
	public final ItemStack getOutput()
	{
		return output;
	}

	@Nonnegative
	public final double getMinimumSouls()
	{
		return minimumSouls;
	}

	@Nonnegative
	public final double getSoulDrain()
	{
		return soulDrain;
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(input.size());
		for (int i = 0; i < input.size(); i++)
		{
			input.get(i).toNetwork(buffer);
		}
		buffer.writeItem(output);
		buffer.writeDouble(minimumSouls);
		buffer.writeDouble(soulDrain);
	}

	@Override
	public RecipeSerializer<RecipeTartaricForge> getSerializer()
	{
		return BloodMagicRecipeSerializers.TARTARIC.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipeTartaricForge> getType()
	{
		return BloodMagicRecipeType.TARTARICFORGE.get();
	}
}