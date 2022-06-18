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

public class RecipeAlchemyTable extends BloodMagicRecipe
{
	@Nonnull
	protected final List<Ingredient> input;
	@Nonnull
	private final ItemStack output;
	@Nonnegative
	private final int syphon;
	@Nonnegative
	private final int ticks;
	@Nonnegative
	private final int minimumTier;

	public static final int MAX_INPUTS = 6;

	public RecipeAlchemyTable(ResourceLocation id, List<Ingredient> input, @Nonnull ItemStack output, int syphon, int ticks, int minimumTier)
	{
		super(id);
		Preconditions.checkNotNull(input, "input cannot be null.");
		Preconditions.checkNotNull(output, "output cannot be null.");
		Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
		Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative.");
		Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");

		this.input = input;
		this.output = output;
		this.syphon = syphon;
		this.ticks = ticks;
		this.minimumTier = minimumTier;
	}

	@Nonnull
	public List<Ingredient> getInput()
	{
		return input;
	}

	@Nonnull
	public ItemStack getOutput(List<ItemStack> inputs)
	{
		return output;
	}

	@Nonnull
	public final ItemStack getOutput()
	{
		return output;
	}

	public final int getSyphon()
	{
		return syphon;
	}

	public final int getTicks()
	{
		return ticks;
	}

	public final int getMinimumTier()
	{
		return minimumTier;
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
		buffer.writeInt(syphon);
		buffer.writeInt(ticks);
		buffer.writeInt(minimumTier);
	}

	@Override
	public RecipeSerializer<? extends RecipeAlchemyTable> getSerializer()
	{
		return BloodMagicRecipeSerializers.ALCHEMYTABLE.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipeAlchemyTable> getType()
	{
		return BloodMagicRecipeType.ALCHEMYTABLE.get();
	}
}
