package WayofTime.bloodmagic.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface IBloodMagicValueManager {

    void setSacrificialValue(@Nonnull ResourceLocation entityId, @Nonnegative int value);

    void setTranquility(@Nonnull IBlockState state, @Nonnull String tranquilityType, double value);
}
