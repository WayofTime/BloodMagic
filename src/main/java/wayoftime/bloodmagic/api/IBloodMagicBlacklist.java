package wayoftime.bloodmagic.api;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;

/**
 * Allows blacklisting of various objects from different Blood Magic systems.
 */
public interface IBloodMagicBlacklist
{

	/**
	 * Blacklists a given {@link BlockState} from being teleposed.
	 *
	 * @param state The {@link BlockState} to blacklist.
	 */
	default void addTeleposer(@Nonnull BlockState state)
	{
	};

	/**
	 * Blacklists a {@link net.minecraft.world.entity.Entity} from being teleposed based
	 * on the given registry name.
	 *
	 * @param entityId The registry name to blacklist.
	 */
	default void addTeleposer(@Nonnull ResourceLocation entityId)
	{
	};

	/**
	 * Blacklists a given {@link BlockState} from being transposed.
	 *
	 * @param state The {@link BlockState} to blacklist.
	 */
	default void addTransposition(@Nonnull BlockState state)
	{
	};

	/**
	 * Blacklists a given {@link BlockState} from being accelerated by the growth
	 * enhancement ritual and sigil.
	 *
	 * @param state The {@link BlockState} to blacklist.
	 */
	default void addGreenGrove(@Nonnull BlockState state)
	{
	};

	/**
	 * Blacklists a {@link net.minecraft.world.entity.Entity} from being sacrificed via
	 * the Well of Suffering ritual.
	 *
	 * @param entityId The registry name to blacklist.
	 */
	default void addWellOfSuffering(@Nonnull ResourceLocation entityId)
	{
	};
}
