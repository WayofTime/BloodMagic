package wayoftime.bloodmagic.tile;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.block.BlockShapedExplosive;
import wayoftime.bloodmagic.tile.base.TileTicking;

public class TileShapedExplosive extends TileTicking
{
	@ObjectHolder("bloodmagic:shaped_explosive")
	public static TileEntityType<TileShapedExplosive> TYPE;

	public double internalCounter = 0;
	public int explosionRadius;
	public int explosionDepth;

	public AnointmentHolder anointmentHolder = new AnointmentHolder();

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
		if (world.isRemote)
		{
			return;
		}
//		System.out.println("Counter: " + internalCounter);

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
			world.playSound((PlayerEntity) null, this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

			Direction explosiveDirection = this.getBlockState().get(BlockShapedExplosive.ATTACHED).getOpposite();
			Direction sweepDir1 = Direction.UP;
			Direction sweepDir2 = Direction.UP;

			int numParticles = explosionDepth * (explosionRadius + 1);

			((ServerWorld) this.world).spawnParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5 + explosiveDirection.getXOffset() * explosionDepth / 2d, pos.getY() + 0.5 + explosiveDirection.getYOffset() * explosionDepth / 2d, pos.getZ() + 0.5 + explosiveDirection.getZOffset() * explosionDepth / 2d, numParticles, 1.0D, 1.0D, 1.0D, 0);

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

			BlockPos initialPos = getPos();
			for (int i = 1; i <= explosionDepth; i++)
			{
				for (int j = -explosionRadius; j <= explosionRadius; j++)
				{
					for (int k = -explosionRadius; k <= explosionRadius; k++)
					{
						BlockPos blockpos = initialPos.offset(explosiveDirection, i).offset(sweepDir1, j).offset(sweepDir2, k);

						BlockState blockstate = this.world.getBlockState(blockpos);
						Block block = blockstate.getBlock();
						if (!blockstate.isAir(this.world, blockpos) && blockstate.getBlockHardness(world, blockpos) != -1.0F)
						{
							BlockPos blockpos1 = blockpos.toImmutable();
//							this.world.getProfiler().startSection("explosion_blocks");
							if (this.world instanceof ServerWorld)
							{
								TileEntity tileentity = blockstate.hasTileEntity() ? this.world.getTileEntity(blockpos)
										: null;
								LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.world)).withRandom(this.world.rand).withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(blockpos)).withParameter(LootParameters.TOOL, toolStack).withNullableParameter(LootParameters.BLOCK_ENTITY, tileentity);
//			                  if (this.mode == Explosion.Mode.DESTROY) {
//			                     lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, this.size);
//			                  }

								blockstate.getDrops(lootcontext$builder).forEach((stack) -> {
									handleExplosionDrops(objectarraylist, stack, blockpos1);
								});

								world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);

//							blockstate.onBlockExploded(this.world, blockpos, null);
//			               this.world.getProfiler().endSection();
							}
						}
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

	private static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos)
	{
		int i = dropPositionArray.size();

		for (int j = 0; j < i; ++j)
		{
			Pair<ItemStack, BlockPos> pair = dropPositionArray.get(j);
			ItemStack itemstack = pair.getFirst();
			if (ItemEntity.canMergeStacks(itemstack, stack))
			{
				ItemStack itemstack1 = ItemEntity.mergeStacks(itemstack, stack, 16);
				dropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
				if (stack.isEmpty())
				{
					return;
				}
			}
		}

		dropPositionArray.add(Pair.of(stack, pos));
	}

	public ItemStack getHarvestingTool()
	{
		ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
		if (anointmentHolder != null)
			anointmentHolder.toItemStack(stack);
		return stack;
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		internalCounter = tag.getDouble("internalCounter");
		if (tag.contains("holder"))
		{
			anointmentHolder = AnointmentHolder.fromNBT(tag.getCompound("holder"));
		}

	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		tag.putDouble("internalCounter", internalCounter);
		if (anointmentHolder != null)
		{
			tag.put("holder", anointmentHolder.serialize());
		}

		return tag;
	}

	public void setAnointmentHolder(AnointmentHolder holder)
	{
		this.anointmentHolder = holder;
	}

	public void dropSelf()
	{
		ItemStack stack = new ItemStack(getBlockState().getBlock());
		if (anointmentHolder != null && !anointmentHolder.isEmpty())
		{
			anointmentHolder.toItemStack(stack);
		}

		InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	}
}