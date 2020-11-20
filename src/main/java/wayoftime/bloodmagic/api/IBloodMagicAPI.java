package wayoftime.bloodmagic.api;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.LazyValue;
import org.apache.logging.log4j.LogManager;

import java.util.function.Predicate;

/**
 * The main interface between a plugin and Blood Magic's internals.
 *
 * This API is intended for <i>compatibility</i> between other mods and Blood
 * Magic. More advanced integration is out of the scope of this API and are
 * considered "addons".
 *
 * Use INSTANCE to get an instance of the API without actually implementing anything
 */
public interface IBloodMagicAPI
{
	LazyValue<IBloodMagicAPI> INSTANCE = new LazyValue<>(() ->
	{
		try
		{
			return (IBloodMagicAPI) Class.forName("wayoftime.bloodmagic.impl.BloodMagicAPI").getDeclaredField("INSTANCE").get(null);
		}
		catch (ReflectiveOperationException e)
		{
			LogManager.getLogger().warn("Unable to find BloodMagicAPI, things will start breaking now...");
			return null;
		}
	});
//	/**
//	 * Retrieves the instance of the blacklist.
//	 *
//	 * @return the active {@link IBloodMagicBlacklist} instance
//	 */
//	@Nonnull
//	IBloodMagicBlacklist getBlacklist();

	/**
	 * Retrieves the instance of the value manager.
	 *
	 * @return the active {@link IBloodMagicValueManager} instance
	 */
	@Nonnull
	IBloodMagicValueManager getValueManager();

	/**
	 * Registers a {@link BlockState} as a given component for the Blood Altar.
	 * <p>
	 * Valid component types:
	 * <ul>
	 * <li>GLOWSTONE</li>
	 * <li>BLOODSTONE</li>
	 * <li>BEACON</li>
	 * <li>BLOODRUNE</li>
	 * <li>CRYSTAL</li>
	 * <li>NOTAIR</li>
	 * </ul>
	 *
	 * @param state         The state to register
	 * @param componentType The type of Blood Altar component to register as.
	 */
	void registerAltarComponent(@Nonnull BlockState state, @Nonnull String componentType);

	/**
	 * Removes a {@link BlockState} from the component mappings
	 * <p>
	 * Valid component types:
	 * <ul>
	 * <li>GLOWSTONE</li>
	 * <li>BLOODSTONE</li>
	 * <li>BEACON</li>
	 * <li>BLOODRUNE</li>
	 * <li>CRYSTAL</li>
	 * <li>NOTAIR</li>
	 * </ul>
	 *
	 * @param state         The state to unregister
	 * @param componentType The type of Blood Altar component to unregister from.
	 */
	void unregisterAltarComponent(@Nonnull BlockState state, @Nonnull String componentType);

	/**
	 * Registers a {@link Predicate<BlockState>} for tranquility handling
	 * <p>
	 * Valid tranquility types:
	 * <ul>
	 * <li>PLANT</li>
	 * <li>CROP</li>
	 * <li>TREE</li>
	 * <li>EARTHEN</li>
	 * <li>WATER</li>
	 * <li>FIRE</li>
	 * <li>LAVA</li>
	 * </ul>
	 *
	 * @param predicate Predicate to be used for the handler (goes to ITranquilityHandler)
	 * @param tranquilityType Tranquility type that the handler holds
	 * @param value The amount of tranquility that the handler has
	 */
	void registerTranquilityHandler(Predicate<BlockState> predicate, String tranquilityType, double value);

	/**
	 * Gets the total Will that a Player contains
	 * <p>
	 * Valid tranquility types:
	 * <ul>
	 * <li>DEFAULT</li>
	 * <li>CORROSIVE</li>
	 * <li>DESTRUCTIVE</li>
	 * <li>VENGEFUL</li>
	 * <li>STEADFAST</li>
	 * </ul>
	 */
	double getTotalDemonWill(String willType, PlayerEntity player);
}