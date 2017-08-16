package WayofTime.bloodmagic.api_impl;

import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.apiv2.IBloodMagicAPI;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class BloodMagicAPI implements IBloodMagicAPI {

    public static final BloodMagicAPI INSTANCE = new BloodMagicAPI();

    private final BloodMagicBlacklist blacklist;
    private final Map<ResourceLocation, Integer> sacrificialValues;
    private final Map<IBlockState, EnumAltarComponent> altarComponents;

    public BloodMagicAPI() {
        this.blacklist = new BloodMagicBlacklist();
        this.sacrificialValues = Maps.newHashMap();
        this.altarComponents = Maps.newHashMap();
    }

    @Override
    public BloodMagicBlacklist getBlacklist() {
        return blacklist;
    }

    @Override
    public void setSacrificialValue(ResourceLocation entityId, int value) {
        sacrificialValues.put(entityId, value);
    }

    @Override
    public void registerAltarComponent(IBlockState state, String componentType) {
        EnumAltarComponent component = EnumAltarComponent.NOTAIR;
        for (EnumAltarComponent type : EnumAltarComponent.VALUES) {
            if (type.name().equalsIgnoreCase(componentType)) {
                component = type;
                break;
            }
        }

        altarComponents.put(state, component);
    }

    public Map<ResourceLocation, Integer> getSacrificialValues() {
        return ImmutableMap.copyOf(sacrificialValues);
    }

    public Map<IBlockState, EnumAltarComponent> getAltarComponents() {
        return ImmutableMap.copyOf(altarComponents);
    }
}
