package wayoftime.bloodmagic.ritual.imperfect;

import java.util.function.Predicate;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Abstract class for creating new imperfect rituals. To register, annotate your
 * class with {@link WayofTime.bloodmagic.ritual.RitualRegister.Imperfect}
 */
public abstract class ImperfectRitual
{

	private final String name;
	private final Predicate<BlockState> blockRequirement;
	private final int activationCost;
	private final boolean lightShow;
	private final String unlocalizedName;

	public ImperfectRitual(String name, Predicate<BlockState> blockRequirement, int activationCost, boolean lightShow, String unlocalizedName)
	{
		this.name = name;
		this.blockRequirement = blockRequirement;
		this.activationCost = activationCost;
		this.lightShow = lightShow;
		this.unlocalizedName = unlocalizedName;
	}

	/**
	 * @param name             The name of the ritual
	 * @param blockRequirement The block required above the ImperfectRitualStone
	 * @param activationCost   Base LP cost for activating the ritual
	 */
	public ImperfectRitual(String name, Predicate<BlockState> blockRequirement, int activationCost, String unlocalizedName)
	{
		this(name, blockRequirement, activationCost, false, unlocalizedName);
	}

	/**
	 * Called when the player activates the ritual
	 * {@link WayofTime.bloodmagic.tile.TileImperfectRitualStone#performRitual(World, net.minecraft.util.math.BlockPos, ImperfectRitual, PlayerEntity)}
	 *
	 * @param imperfectRitualStone - The {@link IImperfectRitualStone} that the
	 *                             ritual is bound to
	 * @param player               - The player activating the ritual
	 * @return - Whether activation was successful
	 */
	public abstract boolean onActivate(IImperfectRitualStone imperfectRitualStone, Player player);

	public String getName()
	{
		return name;
	}

	public Predicate<BlockState> getBlockRequirement()
	{
		return blockRequirement;
	}

	public int getActivationCost()
	{
		return activationCost;
	}

	public boolean isLightShow()
	{
		return lightShow;
	}

	public String getTranslationKey()
	{
		return unlocalizedName;
	}

	@Override
	public String toString()
	{
		return getName() + "@" + getActivationCost();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof ImperfectRitual))
			return false;

		ImperfectRitual that = (ImperfectRitual) o;

		if (activationCost != that.activationCost)
			return false;
		if (name != null ? !name.equals(that.name) : that.name != null)
			return false;
		return unlocalizedName != null ? unlocalizedName.equals(that.unlocalizedName) : that.unlocalizedName == null;
	}

	@Override
	public int hashCode()
	{
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + activationCost;
		result = 31 * result + (unlocalizedName != null ? unlocalizedName.hashCode() : 0);
		return result;
	}
}