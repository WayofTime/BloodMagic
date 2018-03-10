package WayofTime.bloodmagic.api.impl;

import WayofTime.bloodmagic.api.IBloodMagicAPI;
import WayofTime.bloodmagic.altar.ComponentType;
import WayofTime.bloodmagic.util.BMLog;
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
    private final Multimap<ComponentType, IBlockState> altarComponents;

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
        ComponentType component = null;
        for (ComponentType type : ComponentType.VALUES) {
            if (type.name().equalsIgnoreCase(componentType)) {
                component = type;
                break;
            }
        }

        if (component != null) {
            BMLog.API_VERBOSE.info("Registered {} as a {} altar component.", state, componentType);
            altarComponents.put(component, state);
        } else BMLog.API.warn("Invalid Altar component type: {}.", componentType);
    }

    @Nonnull
    public List<IBlockState> getComponentStates(ComponentType component) {
        return (List<IBlockState>) altarComponents.get(component);
    }
}
