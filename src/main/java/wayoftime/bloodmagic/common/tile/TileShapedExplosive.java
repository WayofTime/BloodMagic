package wayoftime.bloodmagic.common.tile;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import wayoftime.bloodmagic.common.block.BlockShapedExplosive;

public class TileShapedExplosive extends TileExplosiveCharge
{
	public double internalCounter = 0;
	public int explosionRadius;
	public int explosionDepth;

	public TileShapedExplosive(BlockEntityType<?> type, int explosionRadius, int explosionDepth, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
		this.explosionRadius = explosionRadius;
		this.explosionDepth = explosionDepth;
	}

	public TileShapedExplosive(BlockPos pos, BlockState state)
	{
		this(2, 5, pos, state);
	}

	public TileShapedExplosive(int explosionRadius, int explosionDepth, BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.SHAPED_EXPLOSIVE_TYPE.get(), explosionRadius, explosionDepth, pos, state);
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
			level.playSound((Player) null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.4F + 0.8F);
			((ServerLevel) this.level).sendParticles(ParticleTypes.FLAME, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 5, 0.02, 0.03, 0.02, 0);
		}

		if (internalCounter == 30)
		{
			level.playSound((Player) null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
		}

		if (internalCounter < 30)
		{
			return;
		}

		if (level.random.nextDouble() < 0.3)
		{
			((ServerLevel) this.level).sendParticles(ParticleTypes.SMOKE, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 1, 0.0D, 0.0D, 0.0D, 0);
		}

		if (internalCounter == 100)
		{
			level.playSound((Player) null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);

			Direction explosiveDirection = this.getBlockState().getValue(BlockShapedExplosive.ATTACHED).getOpposite();
			Direction sweepDir1 = Direction.UP;
			Direction sweepDir2 = Direction.UP;

			int numParticles = explosionDepth * (explosionRadius + 1);

			((ServerLevel) this.level).sendParticles(ParticleTypes.EXPLOSION, worldPosition.getX() + 0.5 + explosiveDirection.getStepX() * explosionDepth / 2d, worldPosition.getY() + 0.5 + explosiveDirection.getStepY() * explosionDepth / 2d, worldPosition.getZ() + 0.5 + explosiveDirection.getStepZ() * explosionDepth / 2d, numParticles, 1.0D, 1.0D, 1.0D, 0);

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
						if (!blockstate.isAir() && blockstate.getDestroySpeed(level, blockpos) != -1.0F)
						{
							BlockPos blockpos1 = blockpos.immutable();
//							this.world.getProfiler().startSection("explosion_blocks");
							if (this.level instanceof ServerLevel)
							{
								BlockEntity tileentity = blockstate.getBlock() instanceof EntityBlock
										? this.level.getBlockEntity(blockpos)
										: null;
								//TODO RANDOMNESS - look at git history
								LootParams.Builder lootcontext$builder = (new LootParams.Builder((ServerLevel) this.level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, toolStack).withOptionalParameter(LootContextParams.BLOCK_ENTITY, tileentity);
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
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);
		internalCounter = tag.getDouble("internalCounter");
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);
		tag.putDouble("internalCounter", internalCounter);

		return tag;
	}
}
