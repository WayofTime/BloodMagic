package wayoftime.bloodmagic.recipe;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
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
	public void write(PacketBuffer buffer)
	{
		buffer.writeInt(input.size());
		for (int i = 0; i < input.size(); i++)
		{
			input.get(i).write(buffer);
		}
		buffer.writeItemStack(output);
		buffer.writeDouble(minimumSouls);
		buffer.writeDouble(soulDrain);
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