package WayofTime.bloodmagic.api;

import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;

/**
 * The main interface between a plugin and Blood Magic's internals.
 *
 * This API is intended for <i>compatibility</i> between other mods and Blood Magic. More advanced integration is out of the scope of this API and are considered "addons".
 *
 * To get an instance of this without actually creating an {@link IBloodMagicPlugin}, use {@link BloodMagicPlugin.Inject}.
 */
public interface IBloodMagicAPI {

    /**
     * Retrieves the instance of the blacklist.
     *
     * @return the active {@link IBloodMagicBlacklist} instance
     */
    @Nonnull
    IBloodMagicBlacklist getBlacklist();

    /**
     * Retrieves the instance of the recipe registrar.
     *
     * @return the active {@link IBloodMagicRecipeRegistrar} instance
     */
    @Nonnull
    IBloodMagicRecipeRegistrar getRecipeRegistrar();

    /**
     * Retrieves the instance of the value manager.
     *
     * @return the active {@link IBloodMagicValueManager} instance
     */
    @Nonnull
    IBloodMagicValueManager getValueManager();

    /**
     * Registers an {@link IBlockState} as a given component for the Blood Altar.
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
    void registerAltarComponent(@Nonnull IBlockState state, @Nonnull String componentType);

    /**
     * Removes an {@link IBlockState} from the component mappings
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
    void unregisterAltarComponent(@Nonnull IBlockState state, @Nonnull String componentType);


}
