package wayoftime.bloodmagic.impl;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import wayoftime.bloodmagic.altar.ComponentType;
import wayoftime.bloodmagic.api.IBloodMagicAPI;
import wayoftime.bloodmagic.api.IBloodMagicRecipeRegistrar;
import wayoftime.bloodmagic.util.BMLog;

public class BloodMagicAPI implements IBloodMagicAPI
{

	public static final BloodMagicAPI INSTANCE = new BloodMagicAPI();

//	private final BloodMagicBlacklist blacklist;
	private final BloodMagicRecipeRegistrar recipeRegistrar;
	private final BloodMagicValueManager valueManager;
	private final Multimap<ComponentType, BlockState> altarComponents;

	public BloodMagicAPI()
	{
//		this.blacklist = new BloodMagicBlacklist();
		this.recipeRegistrar = new BloodMagicRecipeRegistrar();
		this.valueManager = new BloodMagicValueManager();
		this.altarComponents = ArrayListMultimap.create();
	}

//	@Nonnull
//	@Override
//	public BloodMagicBlacklist getBlacklist()
//	{
//		return blacklist;
//	}

	@Nonnull
	@Override
	public BloodMagicRecipeRegistrar getRecipeRegistrar()
	{
		return recipeRegistrar;
	}

//
	@Nonnull
	@Override
	public BloodMagicValueManager getValueManager()
	{
		return valueManager;
	}

	@Override
	public void registerAltarComponent(@Nonnull BlockState state, @Nonnull String componentType)
	{
		ComponentType component = null;
		for (ComponentType type : ComponentType.VALUES)
		{
			if (type.name().equalsIgnoreCase(componentType))
			{
				component = type;
				break;
			}
		}

		if (component != null)
		{
			BMLog.API_VERBOSE.info("Registered {} as a {} altar component.", state, componentType);
			altarComponents.put(component, state);
		} else
			BMLog.API.warn("Invalid Altar component type: {}.", componentType);
	}

	@Override
	public void unregisterAltarComponent(@Nonnull BlockState state, @Nonnull String componentType)
	{
		ComponentType component = null;
		for (ComponentType type : ComponentType.VALUES)
		{
			if (type.name().equalsIgnoreCase(componentType))
			{
				component = type;
				break;
			}
		}

		if (component != null)
		{
			BMLog.API_VERBOSE.info("Unregistered {} from being a {} altar component.", state, componentType);
			altarComponents.remove(component, state);
		} else
			BMLog.API.warn("Invalid Altar component type: {}.", componentType);
	}

	@Nonnull
	public List<BlockState> getComponentStates(ComponentType component)
	{
		return (List<BlockState>) altarComponents.get(component);
	}
}