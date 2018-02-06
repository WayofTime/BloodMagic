package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.apibutnotreally.altar.EnumAltarComponent;
import WayofTime.bloodmagic.api.IBloodMagicAPI;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class BloodMagicAPI implements IBloodMagicAPI {

    public static final BloodMagicAPI INSTANCE = new BloodMagicAPI();

    private final BloodMagicBlacklist blacklist;
    private final Map<ResourceLocation, Integer> sacrificialValues;
    private final Multimap<EnumAltarComponent, IBlockState> altarComponents;

    public BloodMagicAPI() {
        this.blacklist = new BloodMagicBlacklist();
        this.sacrificialValues = Maps.newHashMap();
        this.altarComponents = ArrayListMultimap.create();
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

        altarComponents.put(component, state);
    }

    public Map<ResourceLocation, Integer> getSacrificialValues() {
        return ImmutableMap.copyOf(sacrificialValues);
    }

    public List<IBlockState> getComponentStates(EnumAltarComponent component) {
        return (List<IBlockState>) altarComponents.get(component);
    }
}
