package wayoftime.bloodmagic.altar;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wayoftime.bloodmagic.api.tile.IAltarComponent;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.common.block.BlockBloodRune;
import wayoftime.bloodmagic.tile.TileAltar;

public class AltarUtil
{

	@Nonnull
	public static AltarTier getTier(World world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileAltar))
			return AltarTier.ONE;

		AltarTier lastCheck = AltarTier.ONE;
		for (AltarTier tier : AltarTier.values())
		{
			for (AltarComponent component : tier.getAltarComponents())
			{
				BlockPos componentPos = pos.add(component.getOffset());
				BlockState worldState = world.getBlockState(componentPos);

				if (worldState.getBlock() instanceof IAltarComponent)
					if (((IAltarComponent) worldState.getBlock()).getType(world, worldState, componentPos) == component.getComponent())
						continue;

				if (component.getComponent() == ComponentType.NOTAIR && worldState.getMaterial() != Material.AIR
						&& !worldState.getMaterial().isLiquid())
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
	public static AltarUpgrade getUpgrades(World world, BlockPos pos, AltarTier currentTier)
	{
		AltarUpgrade upgrades = new AltarUpgrade();

		for (AltarComponent component : currentTier.getAltarComponents())
		{
			if (!component.isUpgradeSlot() || component.getComponent() != ComponentType.BLOODRUNE)
				continue;

			BlockPos componentPos = pos.add(component.getOffset());
			BlockState state = world.getBlockState(componentPos);
			if (state.getBlock() instanceof BlockBloodRune)
				upgrades.upgrade(((BlockBloodRune) state.getBlock()).getBloodRune(world, componentPos));
		}

		return upgrades;
	}

	@Nullable
	public static Pair<BlockPos, ComponentType> getFirstMissingComponent(World world, BlockPos pos, int altarTier)
	{
		if (altarTier >= AltarTier.MAXTIERS)
			return null;

		for (AltarTier tier : AltarTier.values())
		{
			for (AltarComponent component : tier.getAltarComponents())
			{
				BlockPos componentPos = pos.add(component.getOffset());
				BlockState worldState = world.getBlockState(componentPos);
				if (component.getComponent() == ComponentType.NOTAIR && worldState.getMaterial() != Material.AIR
						&& !worldState.getMaterial().isLiquid())
					continue;

				List<BlockState> validStates = BloodMagicAPI.INSTANCE.getComponentStates(component.getComponent());
				if (!validStates.contains(worldState))
					return Pair.of(componentPos, component.getComponent());
			}
		}

		return null;
	}
}
