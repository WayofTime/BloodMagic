package wayoftime.bloodmagic.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

public class BlockTau extends CropsBlock
{
	public final boolean isStrong;
	private static final VoxelShape[] SHAPES = new VoxelShape[] {
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D) };

	public static final double TRANSFORM_CHANCE = 0.2d;

	public BlockTau(AbstractBlock.Properties properties, boolean isStrong)
	{
		super(properties);
		this.isStrong = isStrong;
	}

	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return state.isIn(Blocks.FARMLAND);
	}

	protected IItemProvider getSeedsItem()
	{
		return isStrong ? BloodMagicItems.STRONG_TAU_ITEM.get() : BloodMagicItems.WEAK_TAU_ITEM.get();
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPES[state.get(this.getAgeProperty())];
	}

	/**
	 * Performs a random tick on a block.
	 */
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if (!worldIn.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (worldIn.getLightSubtracted(pos, 0) >= 9)
		{
			int i = this.getAge(state);
			if (i < this.getMaxAge())
			{
				float f = getGrowthChance(this, worldIn, pos);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0))
				{
					boolean doTransform = false;
					boolean doGrow = !isStrong;

					AxisAlignedBB boundingBox = new AxisAlignedBB(pos).grow(1, 0, 1);
					List<LivingEntity> list = worldIn.getEntitiesWithinAABB(LivingEntity.class, boundingBox);
					for (LivingEntity entity : list)
					{
						if (entity.attackEntityFrom(DamageSource.CACTUS, 2))
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
							worldIn.setBlockState(pos, ((BlockTau) BloodMagicBlocks.STRONG_TAU.get()).withAge(i + 1), 2);
						} else
						{
							worldIn.setBlockState(pos, this.withAge(i + 1), 2);
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
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		return !this.isMaxAge(state);
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return false;
	}

	@Override
	public void grow(World worldIn, BlockPos pos, BlockState state)
	{
		int i = this.getAge(state);
		if (i < this.getMaxAge())
		{
			int newAge = i + getBonemealAgeIncrease(worldIn);
			boolean doTransform = false;
			boolean doGrow = !isStrong;

			AxisAlignedBB boundingBox = new AxisAlignedBB(pos);
			List<LivingEntity> list = worldIn.getEntitiesWithinAABB(LivingEntity.class, boundingBox);
			for (LivingEntity entity : list)
			{
				if (entity.attackEntityFrom(DamageSource.CACTUS, 2))
				{
					if (isStrong)
					{
						doGrow = true;
						break;
					} else if (worldIn.rand.nextDouble() <= TRANSFORM_CHANCE)
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
					worldIn.setBlockState(pos, ((BlockTau) BloodMagicBlocks.STRONG_TAU.get()).withAge(newAge), 2);
				} else
				{
					worldIn.setBlockState(pos, this.withAge(newAge), 2);
				}

				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
			}
		}
	}

	@Override
	protected int getBonemealAgeIncrease(World worldIn)
	{
		return MathHelper.nextInt(worldIn.rand, 1, 1);
	}
}