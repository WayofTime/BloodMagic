package wayoftime.bloodmagic.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

public class BlockTau extends CropBlock
{
	public final boolean isStrong;
	private static final VoxelShape[] SHAPES = new VoxelShape[] {
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D) };

	public static final double TRANSFORM_CHANCE = 0.2d;

	public BlockTau(BlockBehaviour.Properties properties, boolean isStrong)
	{
		super(properties);
		this.isStrong = isStrong;
	}

	protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos)
	{
		return state.is(Blocks.FARMLAND);
	}

	protected ItemLike getBaseSeedId()
	{
		return isStrong ? BloodMagicItems.STRONG_TAU_ITEM.get() : BloodMagicItems.WEAK_TAU_ITEM.get();
	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return SHAPES[state.getValue(this.getAgeProperty())];
	}

	/**
	 * Performs a random tick on a block.
	 */
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random)
	{
		if (!worldIn.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (worldIn.getRawBrightness(pos, 0) >= 9)
		{
			int i = this.getAge(state);
			if (i < this.getMaxAge())
			{
				float f = getGrowthSpeed(this, worldIn, pos);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0))
				{
					boolean doTransform = false;
					boolean doGrow = !isStrong;

					AABB boundingBox = new AABB(pos).inflate(1, 0, 1);
					List<LivingEntity> list = worldIn.getEntitiesOfClass(LivingEntity.class, boundingBox);
					for (LivingEntity entity : list)
					{
						if (entity.hurt(entity.damageSources().cactus(), 2))
						{
							if (isStrong)
							{
								doGrow = true;
								break;
							} else if (random.nextDouble() <= TRANSFORM_CHANCE)
							{
								doTransform = true;
								break;
							}
						}
					}

					if (doGrow)
					{
						if (doTransform)
						{
							worldIn.setBlock(pos, ((BlockTau) BloodMagicBlocks.STRONG_TAU.get()).getStateForAge(i + 1), 2);
						} else
						{
							worldIn.setBlock(pos, this.getStateForAge(i + 1), 2);
						}

						net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
					}
				}
			}
		}
	}

	/**
	 * Whether this IGrowable can grow
	 */
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		return !this.isMaxAge(state);
	}

	public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return false;
	}

	@Override
	public void growCrops(Level worldIn, BlockPos pos, BlockState state)
	{
		int i = this.getAge(state);
		if (i < this.getMaxAge())
		{
			int newAge = i + getBonemealAgeIncrease(worldIn);
			boolean doTransform = false;
			boolean doGrow = !isStrong;

			AABB boundingBox = new AABB(pos);
			List<LivingEntity> list = worldIn.getEntitiesOfClass(LivingEntity.class, boundingBox);
			for (LivingEntity entity : list)
			{
				if (entity.hurt(entity.damageSources().cactus(), 2))
				{
					if (isStrong)
					{
						doGrow = true;
						break;
					} else if (worldIn.random.nextDouble() <= TRANSFORM_CHANCE)
					{
						doTransform = true;
						break;
					}
				}
			}

			if (doGrow)
			{
				if (doTransform)
				{
					worldIn.setBlock(pos, ((BlockTau) BloodMagicBlocks.STRONG_TAU.get()).getStateForAge(newAge), 2);
				} else
				{
					worldIn.setBlock(pos, this.getStateForAge(newAge), 2);
				}

				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
			}
		}
	}

	@Override
	protected int getBonemealAgeIncrease(Level worldIn)
	{
		return Mth.nextInt(worldIn.random, 1, 1);
	}
}