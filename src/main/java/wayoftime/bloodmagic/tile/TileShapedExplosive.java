package wayoftime.bloodmagic.tile;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.block.BlockShapedExplosive;

public class TileShapedExplosive extends TileExplosiveCharge
{
	@ObjectHolder("bloodmagic:shaped_explosive")
	public static TileEntityType<TileShapedExplosive> TYPE;

	public double internalCounter = 0;
	public int explosionRadius;
	public int explosionDepth;

	public TileShapedExplosive(TileEntityType<?> type, int explosionRadius, int explosionDepth)
	{
		super(type);
		this.explosionRadius = explosionRadius;
		this.explosionDepth = explosionDepth;
	}

	public TileShapedExplosive()
	{
		this(TYPE, 2, 5);
	}

	@Override
	public void onUpdate()
	{
		if (level.isClientSide)
		{
			return;
		}
//		System.out.println("Counter: " + internalCounter);

		internalCounter++;
		if (internalCounter == 20)
		{
//			worldIn.playSound((PlayerEntity)null, tntentity.getPosX(), tntentity.getPosY(), tntentity.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
			level.playSound((PlayerEntity) null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, level.random.nextFloat() * 0.4F + 0.8F);
			((ServerWorld) this.level).sendParticles(ParticleTypes.FLAME, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 5, 0.02, 0.03, 0.02, 0);
		}

		if (internalCounter == 30)
		{
			level.playSound((PlayerEntity) null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}

		if (internalCounter < 30)
		{
			return;
		}

		if (level.random.nextDouble() < 0.3)
		{
			((ServerWorld) this.level).sendParticles(ParticleTypes.SMOKE, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 1, 0.0D, 0.0D, 0.0D, 0);
		}

		if (internalCounter == 100)
		{
			level.playSound((PlayerEntity) null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);

			Direction explosiveDirection = this.getBlockState().getValue(BlockShapedExplosive.ATTACHED).getOpposite();
			Direction sweepDir1 = Direction.UP;
			Direction sweepDir2 = Direction.UP;

			int numParticles = explosionDepth * (explosionRadius + 1);

			((ServerWorld) this.level).sendParticles(ParticleTypes.EXPLOSION, worldPosition.getX() + 0.5 + explosiveDirection.getStepX() * explosionDepth / 2d, worldPosition.getY() + 0.5 + explosiveDirection.getStepY() * explosionDepth / 2d, worldPosition.getZ() + 0.5 + explosiveDirection.getStepZ() * explosionDepth / 2d, numParticles, 1.0D, 1.0D, 1.0D, 0);

			switch (explosiveDirection)
			{
			case UP:
			case DOWN:
				sweepDir1 = Direction.NORTH;
				sweepDir2 = Direction.EAST;
				break;
			case EAST:
			case WEST:
				sweepDir1 = Direction.NORTH;
				sweepDir2 = Direction.UP;
				break;
			case NORTH:
			case SOUTH:
				sweepDir1 = Direction.EAST;
				sweepDir2 = Direction.UP;
				break;
			}

			ItemStack toolStack = this.getHarvestingTool();

			ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();

			BlockPos initialPos = getBlockPos();
			for (int i = 1; i <= explosionDepth; i++)
			{
				for (int j = -explosionRadius; j <= explosionRadius; j++)
				{
					for (int k = -explosionRadius; k <= explosionRadius; k++)
					{
						BlockPos blockpos = initialPos.relative(explosiveDirection, i).relative(sweepDir1, j).relative(sweepDir2, k);

						BlockState blockstate = this.level.getBlockState(blockpos);
						Block block = blockstate.getBlock();
						if (!blockstate.isAir(this.level, blockpos) && blockstate.getDestroySpeed(level, blockpos) != -1.0F)
						{
							BlockPos blockpos1 = blockpos.immutable();
//							this.world.getProfiler().startSection("explosion_blocks");
							if (this.level instanceof ServerWorld)
							{
								TileEntity tileentity = blockstate.hasTileEntity() ? this.level.getBlockEntity(blockpos)
										: null;
								LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.level)).withRandom(this.level.random).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(blockpos)).withParameter(LootParameters.TOOL, toolStack).withOptionalParameter(LootParameters.BLOCK_ENTITY, tileentity);
//			                  if (this.mode == Explosion.Mode.DESTROY) {
//			                     lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, this.size);
//			                  }

								blockstate.getDrops(lootcontext$builder).forEach((stack) -> {
									handleExplosionDrops(objectarraylist, stack, blockpos1);
								});

								level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);

//							blockstate.onBlockExploded(this.world, blockpos, null);
//			               this.world.getProfiler().endSection();
							}
						}
					}
				}
			}

			for (Pair<ItemStack, BlockPos> pair : objectarraylist)
			{
				Block.popResource(this.level, pair.getSecond(), pair.getFirst());
			}

			level.setBlockAndUpdate(getBlockPos(), Blocks.AIR.defaultBlockState());
		}
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);
		internalCounter = tag.getDouble("internalCounter");
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		super.serialize(tag);
		tag.putDouble("internalCounter", internalCounter);

		return tag;
	}
}
