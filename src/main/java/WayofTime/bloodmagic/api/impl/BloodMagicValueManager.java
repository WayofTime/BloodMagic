package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.api.IBloodMagicValueManager;
import WayofTime.bloodmagic.incense.EnumTranquilityType;
import WayofTime.bloodmagic.incense.TranquilityStack;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class BloodMagicValueManager implements IBloodMagicValueManager {

    private final Map<ResourceLocation, Integer> sacrificial;
    private final Map<IBlockState, TranquilityStack> tranquility;

    public BloodMagicValueManager() {
        this.sacrificial = Maps.newHashMap();
        this.tranquility = Maps.newHashMap();
    }

    @Override
    public void setSacrificialValue(@Nonnull ResourceLocation entityId, int value) {
        sacrificial.put(entityId, value);
    }

    @Override
    public void setTranquility(@Nonnull IBlockState state, @Nonnull String tranquilityType, double value) {
        EnumTranquilityType tranquility = null;
        for (EnumTranquilityType type : EnumTranquilityType.values()) {
            if (type.name().equalsIgnoreCase(tranquilityType)) {
                tranquility = type;
                break;
            }
        }

        if (tranquility != null)
            this.tranquility.put(state, new TranquilityStack(tranquility, value));
    }

    public void setTranquility(Block block, TranquilityStack tranquilityStack) {
        for (IBlockState state : block.getBlockState().getValidStates())
            tranquility.put(state, tranquilityStack);
    }

    public Map<ResourceLocation, Integer> getSacrificial() {
        return ImmutableMap.copyOf(sacrificial);
    }

    public Map<IBlockState, TranquilityStack> getTranquility() {
        return ImmutableMap.copyOf(tranquility);
    }
}
