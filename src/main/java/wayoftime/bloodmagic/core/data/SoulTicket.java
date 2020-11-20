package wayoftime.bloodmagic.core.data;

import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

/**
 * Wrapper for any interactions with the SoulNetwork
 * Contains a description on what the interaction is and any extra data
 */
public class SoulTicket
{
	private static final ITextComponent EMPTY = new StringTextComponent("");

	private final ITextComponent description;
	private final int amount;

	public SoulTicket(ITextComponent description, int amount)
	{
		this.description = description;
		this.amount = amount;
	}

	public SoulTicket(int amount)
	{
		this(EMPTY, amount);
	}

	public boolean isSyphon()
	{
		return amount < 0;
	}

	public ITextComponent getDescription()
	{
		return description;
	}

	public int getAmount()
	{
		return amount;
	}

	/**
	 * @return A description in the format block|dimensionID|pos
	 */
	public static SoulTicket block(World world, BlockPos pos, int amount)
	{
		// func_234923_W_() = getDimension
		return new SoulTicket(new StringTextComponent("block|" + world.getDimensionKey().getRegistryName() + "|"
				+ pos.toLong()), amount);
	}

	/**
	 * @return A description in the format item|item registry
	 *         name|dimensionID|entityName|entityPos
	 */
	public static SoulTicket item(ItemStack itemStack, World world, Entity entity, int amount)
	{
		return new SoulTicket(new StringTextComponent("item|" + itemStack.getItem().getRegistryName() + "|"
				+ world.getDimensionKey().getRegistryName() + "|" + entity.getCachedUniqueIdString()), amount);
	}

	/**
	 * @return A description in the format item|item registry name|dimensionID|pos
	 */
	public static SoulTicket item(ItemStack itemStack, World world, BlockPos pos, int amount)
	{
		return new SoulTicket(new StringTextComponent("item|" + itemStack.getItem().getRegistryName() + "|"
				+ world.getDimensionKey().getRegistryName() + "|" + pos.toLong()), amount);
	}

	/**
	 * @return A description in the format item|item registry name|dimensionID
	 */
	public static SoulTicket item(ItemStack itemStack, int amount)
	{
		return new SoulTicket(new StringTextComponent("item|" + itemStack.getItem().getRegistryName()), amount);
	}

	public static SoulTicket command(ICommandSource sender, String command, int amount)
	{
		return new SoulTicket(new StringTextComponent("command|" + command + "|" + sender.toString()), amount);
	}

	// TODO maybe make it check the amount??
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o instanceof SoulTicket)
			return ((SoulTicket) o).getDescription().equals(description);

		return false;
	}

	@Override
	public int hashCode()
	{
		return description.hashCode();
	}
}