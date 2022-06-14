package wayoftime.bloodmagic.recipe;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import wayoftime.bloodmagic.recipe.helper.IgnoredIInventory;

public abstract class BloodMagicRecipe implements IRecipe<IgnoredIInventory>
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
	public abstract void write(PacketBuffer buffer);

	@Nonnull
	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public boolean matches(@Nonnull IgnoredIInventory inv, @Nonnull World world)
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
	public ItemStack assemble(@Nonnull IgnoredIInventory inv)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem()
	{
		return ItemStack.EMPTY;
	}
}
