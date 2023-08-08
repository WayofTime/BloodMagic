package wayoftime.bloodmagic.potion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import wayoftime.bloodmagic.common.registries.BloodMagicDamageTypes;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

public class BMPotionUtils
{
	public static Random rand = new Random();

	public static double damageMobAndGrowSurroundingPlants(LivingEntity entity, int horizontalRadius, int verticalRadius, double damageRatio, int maxPlantsGrown)
	{
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide)
		{
			return 0;
		}

		if (!entity.isAlive())
		{
			return 0;
		}

		double incurredDamage = 0;

		List<BlockPos> growList = new ArrayList<>();

		for (int i = 0; i < maxPlantsGrown; i++)
		{
			BlockPos blockPos = entity.blockPosition().offset(rand.nextInt(horizontalRadius * 2 + 1) - horizontalRadius, rand.nextInt(verticalRadius * 2 + 1) - verticalRadius, rand.nextInt(horizontalRadius * 2 + 1) - horizontalRadius);
			BlockState state = world.getBlockState(blockPos);

			if (!BloodMagicAPI.INSTANCE.getBlacklist().getGreenGrove().contains(state))
			{
				if (state.getBlock() instanceof BonemealableBlock)
				{
					growList.add(blockPos);
				}
			}
		}

		for (BlockPos blockPos : growList)
		{
			Block block = world.getBlockState(blockPos).getBlock();
//          if (world.rand.nextInt(50) == 0)
			{
				BlockState preBlockState = world.getBlockState(blockPos);
				for (int n = 0; n < 10; n++)
					block.randomTick(world.getBlockState(blockPos), (ServerLevel) world, blockPos, world.random);

				BlockState newState = world.getBlockState(blockPos);
				if (!newState.equals(preBlockState))
				{
					world.levelEvent(2005, blockPos, 0);
					incurredDamage += damageRatio;
				}
			}
		}

		if (incurredDamage > 0)
		{
			entity.hurt(entity.damageSources().source(BloodMagicDamageTypes.SACRIFICE), (float) incurredDamage);
		}

		return incurredDamage;
	}

}
