package wayoftime.bloodmagic.recipe.flask;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.recipe.BloodMagicRecipe;
import wayoftime.bloodmagic.recipe.EffectHolder;

public abstract class RecipePotionFlaskBase extends BloodMagicRecipe
{
	@Nonnull
	protected final List<Ingredient> input;

	@Nonnegative
	private final int syphon;
	@Nonnegative
	private final int ticks;
	@Nonnegative
	private final int minimumTier;

	public static final int MAX_INPUTS = 5;

	public RecipePotionFlaskBase(ResourceLocation id, List<Ingredient> input, int syphon, int ticks, int minimumTier)
	{
		super(id);
		Preconditions.checkNotNull(input, "input cannot be null.");
		Preconditions.checkArgument(syphon >= 0, "syphon cannot be negative.");
		Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative.");
		Preconditions.checkArgument(minimumTier >= 0, "minimumTier cannot be negative.");

		this.input = input;
		this.syphon = syphon;
		this.ticks = ticks;
		this.minimumTier = minimumTier;
	}

	public abstract boolean canModifyFlask(ItemStack flaskStack, List<EffectHolder> flaskEffectList);

	// Higher priority recipe is preferred.
	public abstract int getPriority(List<EffectHolder> flaskEffectList);

	@Nonnull
	public List<Ingredient> getInput()
	{
		return input;
	}

	@Nonnull
	public abstract ItemStack getOutput(ItemStack flaskStack, List<EffectHolder> flaskEffectList);

	@Nonnull
	public ItemStack getExamplePotionFlask()
	{
		ItemStack flaskStack = new ItemStack(BloodMagicItems.ALCHEMY_FLASK.get());
		((ItemAlchemyFlask) flaskStack.getItem()).setEffectHoldersOfFlask(flaskStack, getExampleEffectList());
		((ItemAlchemyFlask) flaskStack.getItem()).resyncEffectInstances(flaskStack);

		return flaskStack;
	}

	public abstract List<EffectHolder> getExampleEffectList();

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
		buffer.writeInt(syphon);
		buffer.writeInt(ticks);
		buffer.writeInt(minimumTier);
	}

//	@Override
//	public IRecipeSerializer<? extends RecipePotionEffectBase> getSerializer()
//	{
//		return BloodMagicRecipeSerializers.POTIONEFFECT.getRecipeSerializer();
//	}

	@Override
	public RecipeType<RecipePotionFlaskBase> getType()
	{
		return BloodMagicRecipeType.POTIONFLASK.get();
	}
}