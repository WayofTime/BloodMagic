package wayoftime.bloodmagic.demonaura;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.will.DemonWillHolder;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;

public class WorldDemonWillHandler
{
	public static DemonWillHolder getWillHolder(Level world, BlockPos pos)
	{
		LazyOptional<DemonWillHolder> lazyWill = world.getChunkAt(pos).getCapability(BloodMagic.WILL_AURA_CAP);
		return lazyWill.resolve().get();
	}

	public static double getCurrentWill(Level world, BlockPos pos, EnumDemonWillType type)
	{
		return getWillHolder(world, pos).getWill(type);
	}

	public static EnumDemonWillType getHighestDemonWillType(Level world, BlockPos pos)
	{
		double currentMax = 0;
		EnumDemonWillType currentHighest = EnumDemonWillType.DEFAULT;

		DemonWillHolder willHolder = getWillHolder(world, pos);

		for (EnumDemonWillType type : EnumDemonWillType.values())
		{
			if (willHolder.getWill(type) > currentMax)
			{
				currentMax = willHolder.getWill(type);
				currentHighest = type;
			}
		}

		return currentHighest;
	}

	public static double drainWill(Level world, BlockPos pos, EnumDemonWillType type, double amount, boolean doDrain)
	{
		DemonWillHolder willHolder = getWillHolder(world, pos);

		double drain = Math.min(willHolder.getWill(type), amount);
		if (!doDrain)
		{
			return drain;
		}

		drain = willHolder.drainWill(type, drain);

		return drain;
	}

	public static double fillWillToMaximum(Level world, BlockPos pos, EnumDemonWillType type, double amount, double max, boolean doFill)
	{
		DemonWillHolder willHolder = getWillHolder(world, pos);

		double fill = Math.min(amount, max - willHolder.getWill(type));
		if (!doFill || fill <= 0)
		{
			return fill > 0 ? fill : 0;
		}

		fill = willHolder.addWill(type, amount, max);

		return fill;
	}

	public static double fillWill(Level world, BlockPos pos, EnumDemonWillType type, double amount, boolean doFill)
	{
		DemonWillHolder willHolder = getWillHolder(world, pos);
		if (!doFill)
		{
			return amount;
		}
		willHolder.addWill(type, amount);

		return amount;
	}
}