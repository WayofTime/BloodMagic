package wayoftime.bloodmagic.tile;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.block.BlockShapedExplosive;

public class TileVeinMineCharge extends TileExplosiveCharge
{
	@ObjectHolder("bloodmagic:veinmine_charge")
	public static TileEntityType<TileVeinMineCharge> TYPE;

	private Map<BlockPos, Boolean> veinPartsMap;
	private List<BlockPos> veinPartsCache;
	private boolean finishedAnalysis;

	private Vector3i[] diagonals = new Vector3i[] { new Vector3i(0, 1, 1), new Vector3i(0, 1, -1),
			new Vector3i(0, -1, 1), new Vector3i(0, -1, -1), new Vector3i(1, 0, 1), new Vector3i(-1, 0, 1),
			new Vector3i(1, 0, -1), new Vector3i(-1, 0, -1), new Vector3i(1, 1, 0), new Vector3i(-1, 1, 0),
			new Vector3i(1, -1, 0), new Vector3i(-1, -1, 0) };

	public double internalCounter = 0;

	public int currentBlocks = 0;

	public int maxBlocks = 128;

	public TileVeinMineCharge(TileEntityType<?> type, int maxBlocks)
	{
		super(type);

		this.maxBlocks = maxBlocks;
	}

	public TileVeinMineCharge()
	{
		this(TYPE, 64 * 3);
	}

	@Override
	public void onUpdate()
	{
		if (level.isClientSide)
		{
			return;
		}
//		System.out.println("Counter: " + internalCounter);

		Direction explosiveDirection = this.getBlockState().getValue(BlockShapedExplosive.ATTACHED).getOpposite();
		BlockState attachedState = level.getBlockState(worldPosition.relative(explosiveDirection));
		Block attachedBlock = attachedState.getBlock();
		if (!isValidStartingBlock(attachedState))
		{
			return;
		}
//		if (!BlockTags.LOGS.contains(attachedState.getBlock()) && !BlockTags.LEAVES.contains(attachedState.getBlock()))
//		{
//			return;
//		}

		if (veinPartsMap == null)
		{
			veinPartsMap = new HashMap<BlockPos, Boolean>();
			veinPartsMap.put(worldPosition.relative(explosiveDirection), false);
			veinPartsCache = new LinkedList<BlockPos>();
			veinPartsCache.add(worldPosition.relative(explosiveDirection));
			internalCounter = 0;
//			veinPartsMap.add(pos.offset(explosiveDirection));
		}

		boolean foundNew = false;
		List<BlockPos> newPositions = new LinkedList<BlockPos>();
		for (BlockPos currentPos : veinPartsCache)
		{
			if (!veinPartsMap.getOrDefault(currentPos, false)) // If the BlockPos wasn't checked yet
			{
//				BlockPos currentPos = entry.getKey();
				for (Direction dir : Direction.values())
				{
					BlockPos checkPos = currentPos.relative(dir);
					if (veinPartsMap.containsKey(checkPos))
					{
						continue;
					}

					BlockState checkState = level.getBlockState(checkPos);

					boolean isTree = false;
					if (currentBlocks >= maxBlocks)
					{
						continue;
					}
					if (isValidBlock(attachedState, checkState))
					{
						currentBlocks++;
						isTree = true;

					}

					if (isTree)
					{
						veinPartsMap.put(checkPos, false);
						newPositions.add(checkPos);
						foundNew = true;
					}
				}

				if (this.checkDiagonals())
				{
					for (Vector3i vec : this.diagonals)
					{
						BlockPos checkPos = currentPos.offset(vec);
						if (veinPartsMap.containsKey(checkPos))
						{
							continue;
						}

						BlockState checkState = level.getBlockState(checkPos);

						boolean isTree = false;
						if (currentBlocks >= maxBlocks)
						{
							continue;
						}
						if (isValidBlock(attachedState, checkState))
						{
							currentBlocks++;
							isTree = true;

						}

						if (isTree)
						{
							veinPartsMap.put(checkPos, false);
							newPositions.add(checkPos);
							foundNew = true;
						}
					}
				}

				veinPartsMap.put(currentPos, true);
				if (currentBlocks >= maxBlocks)
				{
					finishedAnalysis = true;
					break;
				}
			}
		}

		veinPartsCache.addAll(newPositions);

//		System.out.println("Found blocks: " + veinPartsMap.size());

		if (foundNew)
		{
			return;
		}

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
			ItemStack toolStack = this.getHarvestingTool();
			level.playSound((PlayerEntity) null, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5, SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);

			int numParticles = 10;

			((ServerWorld) this.level).sendParticles(ParticleTypes.EXPLOSION, worldPosition.getX() + 0.5 + explosiveDirection.getStepX(), worldPosition.getY() + 0.5 + explosiveDirection.getStepY(), worldPosition.getZ() + 0.5 + explosiveDirection.getStepZ(), numParticles, 1.0D, 1.0D, 1.0D, 0);

			ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();

			for (BlockPos blockPos : veinPartsCache)
			{
//				BlockPos blockpos = initialPos.offset(explosiveDirection, i).offset(sweepDir1, j).offset(sweepDir2, k);

				BlockState blockstate = this.level.getBlockState(blockPos);
				Block block = blockstate.getBlock();
				if (!blockstate.isAir(this.level, blockPos))
				{
					BlockPos blockpos1 = blockPos.immutable();
//				this.world.getProfiler().startSection("explosion_blocks");
					if (this.level instanceof ServerWorld)
					{
						TileEntity tileentity = blockstate.hasTileEntity() ? this.level.getBlockEntity(blockPos) : null;
						LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.level)).withRandom(this.level.random).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(blockPos)).withParameter(LootParameters.TOOL, toolStack).withOptionalParameter(LootParameters.BLOCK_ENTITY, tileentity);
//                  if (this.mode == Explosion.Mode.DESTROY) {
//                     lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, this.size);
//                  }

						blockstate.getDrops(lootcontext$builder).forEach((stack) -> {
							handleExplosionDrops(objectarraylist, stack, blockpos1);
						});

						level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);

//				blockstate.onBlockExploded(this.world, blockpos, null);
//               this.world.getProfiler().endSection();
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
		internalCounter = tag.getDouble("internalCounter");
		maxBlocks = tag.getInt("maxBlocks");
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		tag.putDouble("internalCounter", internalCounter);
		tag.putInt("maxBlocks", maxBlocks);
		return tag;
	}

	public boolean isValidBlock(BlockState originalBlockState, BlockState testState)
	{
		return originalBlockState.getBlock() == testState.getBlock();
	}

	public boolean isValidStartingBlock(BlockState originalBlockState)
	{
		return originalBlockState.getDestroySpeed(level, worldPosition) != -1.0F;
	}

	public boolean checkDiagonals()
	{
		return true;
	}
}
