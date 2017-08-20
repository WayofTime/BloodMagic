package WayofTime.bloodmagic.api_impl;

import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.apiv2.IBloodMagicAPI;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.*;

public class BloodMagicAPI implements IBloodMagicAPI {

    public static final BloodMagicAPI INSTANCE = new BloodMagicAPI();

    private final BloodMagicBlacklist blacklist;
    private final BloodMagicRecipeRegistrar recipeRegistrar;
    private final Map<ResourceLocation, Integer> sacrificialValues;
    private final Multimap<EnumAltarComponent, IBlockState> altarComponents;

    public BloodMagicAPI() {
        this.blacklist = new BloodMagicBlacklist();
        this.recipeRegistrar = new BloodMagicRecipeRegistrar();
        this.sacrificialValues = Maps.newHashMap();
        this.altarComponents = ArrayListMultimap.create();
    }

    @Nonnull
    @Override
    public BloodMagicBlacklist getBlacklist() {
        return blacklist;
    }

    @Nonnull
    @Override
    public BloodMagicRecipeRegistrar getRecipeRegistrar() {
        return recipeRegistrar;
    }

    @Override
    public void setSacrificialValue(@Nonnull ResourceLocation entityId, @Nonnegative int value) {
        Preconditions.checkNotNull(entityId, "entityId cannot be null.");
        Preconditions.checkArgument(value >= 0, "value cannot be negative.");

        sacrificialValues.put(entityId, value);
    }

    @Override
    public void registerAltarComponent(@Nonnull IBlockState state, @Nonnull String componentType) {
        Preconditions.checkNotNull(state, "state cannot be null.");
        Preconditions.checkNotNull(componentType, "componentType cannot be null.");

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
