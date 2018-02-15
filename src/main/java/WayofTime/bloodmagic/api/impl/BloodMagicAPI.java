package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.api.IBloodMagicAPI;
import WayofTime.bloodmagic.apibutnotreally.altar.EnumAltarComponent;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;
import java.util.List;

public class BloodMagicAPI implements IBloodMagicAPI {

    public static final BloodMagicAPI INSTANCE = new BloodMagicAPI();

    private final BloodMagicBlacklist blacklist;
    private final BloodMagicRecipeRegistrar recipeRegistrar;
    private final BloodMagicValueManager valueManager;
    private final Multimap<EnumAltarComponent, IBlockState> altarComponents;

    public BloodMagicAPI() {
        this.blacklist = new BloodMagicBlacklist();
        this.recipeRegistrar = new BloodMagicRecipeRegistrar();
        this.valueManager = new BloodMagicValueManager();
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

    @Nonnull
    @Override
    public BloodMagicValueManager getValueManager() {
        return valueManager;
    }

    @Override
    public void registerAltarComponent(@Nonnull IBlockState state, @Nonnull String componentType) {
        EnumAltarComponent component = EnumAltarComponent.NOTAIR;
        for (EnumAltarComponent type : EnumAltarComponent.VALUES) {
            if (type.name().equalsIgnoreCase(componentType)) {
                component = type;
                break;
            }
        }

        altarComponents.put(component, state);
    }

    @Nonnull
    public List<IBlockState> getComponentStates(EnumAltarComponent component) {
        return (List<IBlockState>) altarComponents.get(component);
    }
}
