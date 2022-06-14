package wayoftime.bloodmagic.impl;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.api.IBloodMagicValueManager;
import wayoftime.bloodmagic.incense.EnumTranquilityType;
import wayoftime.bloodmagic.incense.TranquilityStack;
import wayoftime.bloodmagic.util.BMLog;

public class BloodMagicValueManager implements IBloodMagicValueManager
{

	private final Map<ResourceLocation, Integer> sacrificial;
	private final Map<BlockState, TranquilityStack> tranquility;

	public BloodMagicValueManager()
	{
		this.sacrificial = Maps.newHashMap();
		this.tranquility = Maps.newHashMap();
	}

	@Override
	public void setSacrificialValue(@Nonnull ResourceLocation entityId, int value)
	{
		BMLog.API_VERBOSE.info("Value Manager: Set sacrificial value of {} to {}.", entityId, value);
		sacrificial.put(entityId, value);
	}

	@Override
	public void setTranquility(@Nonnull BlockState state, @Nonnull String tranquilityType, double value)
	{
		EnumTranquilityType tranquility = null;
		for (EnumTranquilityType type : EnumTranquilityType.values())
		{
			if (type.name().equalsIgnoreCase(tranquilityType))
			{
				tranquility = type;
				break;
			}
		}

		if (tranquility != null)
		{
			BMLog.API_VERBOSE.info("Value Manager: Set tranquility value of {} to {} @ {}", state, tranquilityType, value);
			this.tranquility.put(state, new TranquilityStack(tranquility, value));
		} else
			BMLog.API.warn("Invalid tranquility type: {}.", tranquilityType);
	}

	public void setTranquility(Block block, TranquilityStack tranquilityStack)
	{
		for (BlockState state : block.getStateDefinition().getPossibleStates())
		{
			BMLog.API_VERBOSE.info("Value Manager: Set tranquility value of {} to {} @ {}", state, tranquilityStack.type, tranquilityStack.value);
			tranquility.put(state, tranquilityStack);
		}
	}

	public Map<ResourceLocation, Integer> getSacrificial()
	{
		return ImmutableMap.copyOf(sacrificial);
	}

	public Map<BlockState, TranquilityStack> getTranquility()
	{
		return ImmutableMap.copyOf(tranquility);
	}
}