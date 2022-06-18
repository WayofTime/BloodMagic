package wayoftime.bloodmagic.recipe;

import javax.annotation.Nonnull;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import wayoftime.bloodmagic.common.recipe.BloodMagicRecipeType;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

public class RecipeAlchemyArray extends BloodMagicRecipe
{
	private final ResourceLocation id;
	private final ResourceLocation texture;
	@Nonnull
	private final Ingredient baseInput;
	@Nonnull
	private final Ingredient addedInput;
	@Nonnull
	private final ItemStack output;

	public RecipeAlchemyArray(ResourceLocation id, ResourceLocation texture, @Nonnull Ingredient baseIngredient, @Nonnull Ingredient addedIngredient, @Nonnull ItemStack result)
	{
		super(id);
		this.id = id;
		this.texture = texture;
		this.baseInput = baseIngredient;
		this.addedInput = addedIngredient;
		this.output = result;
	}

	@Nonnull
	public final ResourceLocation getId()
	{
		return id;
	}

	@Nonnull
	public final ResourceLocation getTexture()
	{
		return texture;
	}

	@Nonnull
	public final Ingredient getBaseInput()
	{
		return baseInput;
	}

	@Nonnull
	public final Ingredient getAddedInput()
	{
		return addedInput;
	}

	@Override
	public final NonNullList<Ingredient> getIngredients()
	{
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(getBaseInput());
		list.add(getAddedInput());
		return list;
	}

	@Nonnull
	public final ItemStack getOutput()
	{
		return output;
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		if (texture != null)
		{
			buffer.writeBoolean(true);
			buffer.writeResourceLocation(texture);
		} else
		{
			buffer.writeBoolean(false);
		}

		baseInput.toNetwork(buffer);
		addedInput.toNetwork(buffer);
		buffer.writeItem(output);
	}

	@Override
	public RecipeSerializer<RecipeAlchemyArray> getSerializer()
	{
		return BloodMagicRecipeSerializers.ARRAY.getRecipeSerializer();
	}

	@Override
	public RecipeType<RecipeAlchemyArray> getType()
	{
		return BloodMagicRecipeType.ARRAY.get();
	}
}
