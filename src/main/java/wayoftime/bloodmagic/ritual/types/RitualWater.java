package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;

@RitualRegister("water")
public class RitualWater extends Ritual
{
	public static final String WATER_RANGE = "waterRange";
	public static final String WATER_TANK_RANGE = "waterTank";

	public RitualWater()
	{
		super("ritualWater", 0, 500, "ritual." + BloodMagic.MODID + ".waterRitual");
		addBlockRange(WATER_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));
		addBlockRange(WATER_TANK_RANGE, new AreaDescriptor.Rectangle(new BlockPos(0, 1, 0), 1));

		setMaximumVolumeAndDistanceOfRange(WATER_RANGE, 9, 3, 3);
		setMaximumVolumeAndDistanceOfRange(WATER_TANK_RANGE, 1, 10, 10);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		BlockPos pos = masterRitualStone.getMasterBlockPos();
		List<EnumDemonWillType> willConfig = masterRitualStone.getActiveWillConfig();

		int maxEffects = currentEssence / getRefreshCost();
		int totalEffects = 0;
		int lpDrain = 0;

		AreaDescriptor waterRange = masterRitualStone.getBlockRange(WATER_RANGE);

		for (BlockPos newPos : waterRange.getContainedPositions(masterRitualStone.getMasterBlockPos()))
		{
			if (world.isEmptyBlock(newPos))
			{
				world.setBlockAndUpdate(newPos, Blocks.WATER.defaultBlockState());
				totalEffects++;
			}

			if (totalEffects >= maxEffects)
			{
				break;
			}
		}

		lpDrain += getRefreshCost() * totalEffects;

		double rawWill = this.getWillRespectingConfig(world, pos, EnumDemonWillType.DEFAULT, willConfig);
		double rawDrained = 0;

		if (rawWill > 0)
		{
			AreaDescriptor chestRange = masterRitualStone.getBlockRange(WATER_TANK_RANGE);
			BlockEntity tile = world.getBlockEntity(chestRange.getContainedPositions(pos).get(0));
			double drain = getWillCostForRawWill(rawWill);
			int lpCost = getLPCostForRawWill(rawWill);

			if (rawWill >= drain && currentEssence >= lpCost)
			{
				if (tile != null)
				{
					LazyOptional<IFluidHandler> capability = tile.getCapability(ForgeCapabilities.FLUID_HANDLER, null);
					if (capability.isPresent())
					{
						IFluidHandler handler = capability.resolve().get();
						double filled = handler.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);

						double ratio = filled / 1000;

						rawWill -= drain * ratio;
						rawDrained += drain * ratio;

						currentEssence -= Math.ceil(lpCost * ratio);
						lpDrain += Math.ceil(lpCost * ratio);
					}
				}
			}
		}

		if (rawDrained > 0)
		{
			WorldDemonWillHandler.drainWill(world, pos, EnumDemonWillType.DEFAULT, rawDrained, true);
		}

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(lpDrain));
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

	@Override
	public int getRefreshCost()
	{
		return 25;
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		addCornerRunes(components, 1, 0, EnumRuneType.WATER);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualWater();
	}

	public int getLPCostForRawWill(double raw)
	{
		return Math.max((int) (20 - raw / 10), 0);
	}

	public double getWillCostForRawWill(double raw)
	{
		return Math.min(1, raw / 1000);
	}
}
