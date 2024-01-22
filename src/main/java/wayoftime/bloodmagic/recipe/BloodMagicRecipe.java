package wayoftime.bloodmagic.recipe;

import javax.annotation.Nonnull;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.recipe.helper.IgnoredIInventory;

public abstract class BloodMagicRecipe implements Recipe<IgnoredIInventory>
{
	private final ResourceLocation id;

	protected BloodMagicRecipe(ResourceLocation id)
	{
		this.id = id;
	}

	/**
	 * Writes this recipe to a PacketBuffer.
	 *
	 * @param buffer The buffer to write to.
	 */
	public abstract void write(FriendlyByteBuf buffer);

	@Nonnull
	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public boolean matches(@Nonnull IgnoredIInventory inv, @Nonnull Level world)
	{
		return true;
	}

	@Override
	public boolean isSpecial()
	{
		// Note: If we make this non dynamic, we can make it show in vanilla's crafting
		// book and also then obey the recipe locking.
		// For now none of that works/makes sense in our concept so don't lock it
		return true;
	}

	@Nonnull
	@Override
	public ItemStack assemble(IgnoredIInventory inventory, RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return ItemStack.EMPTY;
	}
}
