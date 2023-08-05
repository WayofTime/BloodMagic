package wayoftime.bloodmagic.api;

import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;

import net.minecraft.core.NonNullList;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The main interface between a plugin and Blood Magic's internals.
 *
 * This API is intended for <i>compatibility</i> between other mods and Blood
 * Magic. More advanced integration is out of the scope of this API and are
 * considered "addons".
 *
 * Use INSTANCE to get an instance of the API without actually implementing
 * anything
 */
public interface IBloodMagicAPI
{
	LazyLoadedValue<IBloodMagicAPI> INSTANCE = new LazyLoadedValue<>(() -> {
		try
		{
			return (IBloodMagicAPI) Class.forName("wayoftime.bloodmagic.impl.BloodMagicAPI").getDeclaredField("INSTANCE").get(null);
		} catch (ReflectiveOperationException e)
		{
			LogManager.getLogger().warn("Unable to find BloodMagicAPI, using a dummy instance instead...");
			return new IBloodMagicAPI()
			{
			};
		}
	});

	/**
	 * Retrieves the instance of the blacklist.
	 *
	 * @return the active {@link IBloodMagicBlacklist} instance
	 */
	@Nonnull
	default IBloodMagicBlacklist getBlacklist()
	{
		return new IBloodMagicBlacklist()
		{
		};
	};

	/**
	 * Retrieves the instance of the value manager.
	 *
	 * @return the active {@link IBloodMagicValueManager} instance
	 */
	@Nonnull
	default IBloodMagicValueManager getValueManager()
	{
		return new IBloodMagicValueManager()
		{
		};
	}

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
	default void registerAltarComponent(@Nonnull BlockState state, @Nonnull String componentType)
	{
	}

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
	default void unregisterAltarComponent(@Nonnull BlockState state, @Nonnull String componentType)
	{
	}

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
	 * @param predicate       Predicate to be used for the handler (goes to
	 *                        ITranquilityHandler)
	 * @param tranquilityType Tranquility type that the handler holds
	 * @param value           The amount of tranquility that the handler has
	 */
	default void registerTranquilityHandler(Predicate<BlockState> predicate, String tranquilityType, double value)
	{
	}

	/**
	 * Registers a {@link Function<PlayerEntity, NonNullList<ItemStack>>} for
	 * inventory handling.
	 * 
	 * @param inventoryIdentifier String identifier for the inventory.
	 * @param function            Function which inputs a Player Entity and outputs
	 *                            a NonNullList of ItemStacks.
	 */
	default void registerInventoryProvider(String inventoryIdentifier, Function<Player, NonNullList<ItemStack>> provider)
	{
	}

	/**
	 * Registers an already registered inventory to be considered active (eg.
	 * Main/Offhand, Curios)
	 * 
	 * @param inventoryIdentifier String identifier for the inventory.
	 */
	default void registerActiveInventoryProvider(String inventoryIdentifier)
	{
	}

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
	default double getTotalDemonWill(String willType, Player player)
	{
		return 0;
	}
}