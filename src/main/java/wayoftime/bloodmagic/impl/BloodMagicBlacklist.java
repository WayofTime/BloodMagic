package wayoftime.bloodmagic.impl;

import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.api.IBloodMagicBlacklist;
import wayoftime.bloodmagic.util.BMLog;

public class BloodMagicBlacklist implements IBloodMagicBlacklist
{

	private final Set<BlockState> teleposer;
	private final Set<ResourceLocation> teleposerEntities;
	private final Set<BlockState> transposition;
	private final Set<BlockState> greenGrove;
	private final Set<ResourceLocation> sacrifice;

	public BloodMagicBlacklist()
	{
		this.teleposer = Sets.newHashSet();
		this.teleposerEntities = Sets.newHashSet();
		this.transposition = Sets.newHashSet();
		this.greenGrove = Sets.newHashSet();
		this.sacrifice = Sets.newHashSet();
	}

	@Override
	public void addTeleposer(@Nonnull BlockState state)
	{
		if (!teleposer.contains(state))
		{
			BMLog.API_VERBOSE.info("Blacklist: Added {} to the Teleposer blacklist.", state);
			teleposer.add(state);
		}
	}

	public void addTeleposer(@Nonnull Block block)
	{
		for (BlockState state : block.getStateDefinition().getPossibleStates()) addTeleposer(state);
	}

	@Override
	public void addTeleposer(@Nonnull ResourceLocation entityId)
	{
		if (!teleposerEntities.contains(entityId))
		{
			BMLog.API_VERBOSE.info("Blacklist: Added {} to the Teleposer blacklist.", entityId);
			teleposerEntities.add(entityId);
		}
	}

	@Override
	public void addTransposition(@Nonnull BlockState state)
	{
		if (!transposition.contains(state))
		{
			BMLog.API_VERBOSE.info("Blacklist: Added {} to the Transposition blacklist.", state);
			transposition.add(state);
		}
	}

	public void addTransposition(@Nonnull Block block)
	{
		for (BlockState state : block.getStateDefinition().getPossibleStates()) addTransposition(state);
	}

	@Override
	public void addGreenGrove(@Nonnull BlockState state)
	{
		if (!greenGrove.contains(state))
		{
			BMLog.API_VERBOSE.info("Blacklist: Added {} to the Green Grove blacklist.", state);
			greenGrove.add(state);
		}
	}

	public void addGreenGrove(@Nonnull Block block)
	{
		for (BlockState state : block.getStateDefinition().getPossibleStates()) addGreenGrove(state);
	}

	@Override
	public void addWellOfSuffering(@Nonnull ResourceLocation entityId)
	{
		if (!sacrifice.contains(entityId))
		{
			BMLog.API_VERBOSE.info("Blacklist: Added {} to the Well of Suffering blacklist.", entityId);
			sacrifice.add(entityId);
		}
	}

	// Internal use getters

	public Set<BlockState> getTeleposer()
	{
		return ImmutableSet.copyOf(teleposer);
	}

	public Set<ResourceLocation> getTeleposerEntities()
	{
		return ImmutableSet.copyOf(teleposerEntities);
	}

	public Set<BlockState> getTransposition()
	{
		return ImmutableSet.copyOf(transposition);
	}

	public Set<BlockState> getGreenGrove()
	{
		return ImmutableSet.copyOf(greenGrove);
	}

	public Set<ResourceLocation> getSacrifice()
	{
		return ImmutableSet.copyOf(sacrifice);
	}
}
