package wayoftime.bloodmagic.impl;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import wayoftime.bloodmagic.altar.ComponentType;
import wayoftime.bloodmagic.api.IBloodMagicAPI;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.incense.EnumTranquilityType;
import wayoftime.bloodmagic.incense.IncenseTranquilityRegistry;
import wayoftime.bloodmagic.incense.TranquilityStack;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

public class BloodMagicAPI implements IBloodMagicAPI
{

	public static final BloodMagicAPI INSTANCE = new BloodMagicAPI();

	private final BloodMagicBlacklist blacklist;
	private final BloodMagicRecipeRegistrar recipeRegistrar;
	private final BloodMagicValueManager valueManager;
	private final Multimap<ComponentType, BlockState> altarComponents;

	public BloodMagicAPI()
	{
		this.blacklist = new BloodMagicBlacklist();
		this.recipeRegistrar = new BloodMagicRecipeRegistrar();
		this.valueManager = new BloodMagicValueManager();
		this.altarComponents = ArrayListMultimap.create();
	}

	@Nonnull
	@Override
	public BloodMagicBlacklist getBlacklist()
	{
		return blacklist;
	}

	@Nonnull
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
		ComponentType component = ComponentType.getType(componentType);

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
		ComponentType component = ComponentType.getType(componentType);

		if (component != null)
		{
			BMLog.API_VERBOSE.info("Unregistered {} from being a {} altar component.", state, componentType);
			altarComponents.remove(component, state);
		} else
			BMLog.API.warn("Invalid Altar component type: {}.", componentType);
	}

	@Override
	public void registerTranquilityHandler(@Nonnull Predicate<BlockState> blockState, @Nonnull String tranquilityType, double value)
	{
		EnumTranquilityType type = EnumTranquilityType.getType(tranquilityType);

		if (type != null)
		{
			IncenseTranquilityRegistry.registerTranquilityHandler((world, pos, block, state) -> blockState.test(state)
					? new TranquilityStack(type, value)
					: null);
		} else
		{
			BMLog.API.warn("Invalid Tranquility type: {}.", tranquilityType);
		}
	}

	@Override
	public double getTotalDemonWill(String willType, PlayerEntity player)
	{
		return PlayerDemonWillHandler.getTotalDemonWill(EnumDemonWillType.getType(willType), player);
	}

	@Nonnull
	public List<BlockState> getComponentStates(ComponentType component)
	{
		return (List<BlockState>) altarComponents.get(component);
	}
}