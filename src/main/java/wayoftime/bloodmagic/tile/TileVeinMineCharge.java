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
		if (world.isRemote)
		{
			return;
		}
//		System.out.println("Counter: " + internalCounter);

		Direction explosiveDirection = this.getBlockState().get(BlockShapedExplosive.ATTACHED).getOpposite();
		BlockState attachedState = world.getBlockState(pos.offset(explosiveDirection));
		Block attachedBlock = attachedState.getBlock();
//		if (!BlockTags.LOGS.contains(attachedState.getBlock()) && !BlockTags.LEAVES.contains(attachedState.getBlock()))
//		{
//			return;
//		}

		if (veinPartsMap == null)
		{
			veinPartsMap = new HashMap<BlockPos, Boolean>();
			veinPartsMap.put(pos.offset(explosiveDirection), false);
			veinPartsCache = new LinkedList<BlockPos>();
			veinPartsCache.add(pos.offset(explosiveDirection));
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
					BlockPos checkPos = currentPos.offset(dir);
					if (veinPartsMap.containsKey(checkPos))
					{
						continue;
					}

					BlockState checkState = world.getBlockState(checkPos);

					boolean isTree = false;
					if (currentBlocks >= maxBlocks)
					{
						continue;
					}
					if (attachedBlock.equals(checkState.getBlock()))
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
			world.playSound((PlayerEntity) null, this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
			((ServerWorld) this.world).spawnParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, 0.02, 0.03, 0.02, 0);
		}

		if (internalCounter == 30)
		{
			world.playSound((PlayerEntity) null, this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}

		if (internalCounter < 30)
		{
			return;
		}

		if (world.rand.nextDouble() < 0.3)
		{
			((ServerWorld) this.world).spawnParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0.0D, 0.0D, 0.0D, 0);
		}

		if (internalCounter == 100)
		{
			ItemStack toolStack = this.getHarvestingTool();
			world.playSound((PlayerEntity) null, this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

			int numParticles = 10;

			((ServerWorld) this.world).spawnParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5 + explosiveDirection.getXOffset(), pos.getY() + 0.5 + explosiveDirection.getYOffset(), pos.getZ() + 0.5 + explosiveDirection.getZOffset(), numParticles, 1.0D, 1.0D, 1.0D, 0);

			ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();

			for (BlockPos blockPos : veinPartsCache)
			{
//				BlockPos blockpos = initialPos.offset(explosiveDirection, i).offset(sweepDir1, j).offset(sweepDir2, k);

				BlockState blockstate = this.world.getBlockState(blockPos);
				Block block = blockstate.getBlock();
				if (!blockstate.isAir(this.world, blockPos))
				{
					BlockPos blockpos1 = blockPos.toImmutable();
//				this.world.getProfiler().startSection("explosion_blocks");
					if (this.world instanceof ServerWorld)
					{
						TileEntity tileentity = blockstate.hasTileEntity() ? this.world.getTileEntity(blockPos) : null;
						LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.world)).withRandom(this.world.rand).withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(blockPos)).withParameter(LootParameters.TOOL, toolStack).withNullableParameter(LootParameters.BLOCK_ENTITY, tileentity);
//                  if (this.mode == Explosion.Mode.DESTROY) {
//                     lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, this.size);
//                  }

						blockstate.getDrops(lootcontext$builder).forEach((stack) -> {
							handleExplosionDrops(objectarraylist, stack, blockpos1);
						});

						world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);

//				blockstate.onBlockExploded(this.world, blockpos, null);
//               this.world.getProfiler().endSection();
					}
				}
			}

			for (Pair<ItemStack, BlockPos> pair : objectarraylist)
			{
				Block.spawnAsEntity(this.world, pair.getSecond(), pair.getFirst());
			}

			world.setBlockState(getPos(), Blocks.AIR.getDefaultState());
		}
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		internalCounter = tag.getDouble("internalCounter");
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		tag.putDouble("internalCounter", internalCounter);
		return tag;
	}
}
