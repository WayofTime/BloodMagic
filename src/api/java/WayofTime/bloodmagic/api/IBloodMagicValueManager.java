package WayofTime.bloodmagic.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Allows value modification for various features of Blood Magic such as Sacrificial values.
 */
public interface IBloodMagicValueManager {

    /**
     * Sets the amount of LP received per health point from sacrificing the given entity. By default, this is 25. Setting
     * the value to 0 effectively disables sacrificing.
     *
     * @param entityId The registry name of the entity.
     * @param value    The amount of LP per health point to receive upon sacrifice.
     */
    void setSacrificialValue(@Nonnull ResourceLocation entityId, @Nonnegative int value);

    /**
     * Sets the Tranquility value of a given {@link IBlockState}.
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
     * @param state           The {@link IBlockState} to set the value of.
     * @param tranquilityType The type of Tranquility this block should provide.
     * @param value           The amount of tranquility this block should provide.
     */
    void setTranquility(@Nonnull IBlockState state, @Nonnull String tranquilityType, double value);
}
