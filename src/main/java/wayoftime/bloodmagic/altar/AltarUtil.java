package wayoftime.bloodmagic.altar;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wayoftime.bloodmagic.common.block.BlockBloodRune;
import wayoftime.bloodmagic.common.tile.TileAltar;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

public class AltarUtil
{

	@Nonnull
	public static AltarTier getTier(Level world, BlockPos pos)
	{
		BlockEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TileAltar))
			return AltarTier.ONE;

		AltarTier lastCheck = AltarTier.ONE;
		for (AltarTier tier : AltarTier.values())
		{
			for (AltarComponent component : tier.getAltarComponents())
			{
				BlockPos componentPos = pos.offset(component.getOffset());
				BlockState worldState = world.getBlockState(componentPos);

				if (component.getComponent() == ComponentType.NOTAIR && worldState.isSolid())
					continue;

				List<BlockState> validStates = BloodMagicAPI.INSTANCE.getComponentStates(component.getComponent());
				if (!validStates.contains(worldState))
					return lastCheck;
			}

			lastCheck = tier;
		}

		return lastCheck;
	}

	@Nonnull
	public static AltarUpgrade getUpgrades(Level world, BlockPos pos, AltarTier currentTier)
	{
		AltarUpgrade upgrades = new AltarUpgrade();

		for (AltarComponent component : currentTier.getAltarComponents())
		{
			if (!component.isUpgradeSlot() || component.getComponent() != ComponentType.BLOODRUNE)
				continue;

			BlockPos componentPos = pos.offset(component.getOffset());
			BlockState state = world.getBlockState(componentPos);
			if (state.getBlock() instanceof BlockBloodRune)
				upgrades.upgrade(((BlockBloodRune) state.getBlock()).getBloodRune(world, componentPos), ((BlockBloodRune) state.getBlock()).getRuneCount(world, componentPos));
		}

		return upgrades;
	}

	@Nullable
	public static Pair<BlockPos, ComponentType> getFirstMissingComponent(Level world, BlockPos pos, int altarTier)
	{
		if (altarTier >= AltarTier.MAXTIERS)
			return null;

		for (AltarTier tier : AltarTier.values())
		{
			for (AltarComponent component : tier.getAltarComponents())
			{
				BlockPos componentPos = pos.offset(component.getOffset());
				BlockState worldState = world.getBlockState(componentPos);
				if (component.getComponent() == ComponentType.NOTAIR && worldState.isSolid())
					continue;

				List<BlockState> validStates = BloodMagicAPI.INSTANCE.getComponentStates(component.getComponent());
				if (!validStates.contains(worldState))
					return Pair.of(componentPos, component.getComponent());
			}
		}

		return null;
	}
}
