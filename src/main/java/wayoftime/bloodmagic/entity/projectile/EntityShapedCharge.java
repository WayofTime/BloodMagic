package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.block.BlockShapedExplosive;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.common.tile.TileExplosiveCharge;

public class EntityShapedCharge extends ThrowableProjectile
{
	private static final EntityDataAccessor<BlockState> ITEMSTACK_DATA = SynchedEntityData.defineId(EntityShapedCharge.class, EntityDataSerializers.BLOCK_STATE);
//	private BlockState fallTile = BloodMagicBlocks.SHAPED_CHARGE.get().getDefaultState();
	private AnointmentHolder holder;

	public EntityShapedCharge(EntityType<EntityShapedCharge> p_i50159_1_, Level p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntityShapedCharge(Level worldIn, Block block, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.SHAPED_CHARGE.getEntityType(), throwerIn, worldIn);
//		this.fallTile = block.getDefaultState();
		this.setFallTile(block.defaultBlockState());
	}

	public EntityShapedCharge(Level worldIn, Block block, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.SHAPED_CHARGE.getEntityType(), x, y, z, worldIn);
//		this.fallTile = block.getDefaultState();
		this.setFallTile(block.defaultBlockState());
	}

	public void setAnointmentHolder(AnointmentHolder holder)
	{
		this.holder = holder;
	}

	public void setFallTile(BlockState state)
	{
		this.entityData.set(ITEMSTACK_DATA, state);
	}

	@Override
	public void tick()
	{
		super.tick();
		HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
//		boolean flag = false;
		if (level().isClientSide)
		{
			return;
		}
		if (raytraceresult.getType() == HitResult.Type.BLOCK)
		{
			Direction faceHit = ((BlockHitResult) raytraceresult).getDirection();
			BlockPos blockpos = ((BlockHitResult) raytraceresult).getBlockPos().relative(((BlockHitResult) raytraceresult).getDirection());
			BlockState blockstate = this.level().getBlockState(blockpos);
//		      return state.isAir() || state.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
			BlockState fallTile = this.getBlockState();
			if (blockstate.isAir() || blockstate.is(BlockTags.FIRE) || blockstate.liquid() || blockstate.canBeReplaced())
			{
				this.getCommandSenderWorld().setBlockAndUpdate(blockpos, fallTile.setValue(BlockShapedExplosive.ATTACHED, faceHit));
				BlockEntity tile = this.getCommandSenderWorld().getBlockEntity(blockpos);
				if (tile instanceof TileExplosiveCharge)
				{
					((TileExplosiveCharge) tile).setAnointmentHolder(holder);
				}
				this.removeAfterChangingDimensions();
			} else
			{
//				BlockItem d;
				this.spawnAtLocation(fallTile.getBlock());
				this.removeAfterChangingDimensions();
//				blockstate.isReplaceable(BlockItemUseContext)
			}
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound)
	{
		compound.put("BlockState", NbtUtils.writeBlockState(this.getBlockState()));
		if (holder != null)
			compound.put("holder", holder.serialize());
//	      compound.putInt("Time", this.fallTime);
//	      compound.putBoolean("DropItem", this.shouldDropItem);
//	      compound.putBoolean("HurtEntities", this.hurtEntities);
//	      compound.putFloat("FallHurtAmount", this.fallHurtAmount);
//	      compound.putInt("FallHurtMax", this.fallHurtMax);
//	      if (this.tileEntityData != null) {
//	         compound.put("TileEntityData", this.tileEntityData);
//	      }

	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readAdditionalSaveData(CompoundTag compound)
	{
		BlockState fallTile = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK),compound.getCompound("BlockState"));
		this.setFallTile(fallTile);
		if (compound.contains("holder"))
			this.holder = AnointmentHolder.fromNBT(compound.getCompound("holder"));
//	      this.fallTime = compound.getInt("Time");
//	      if (compound.contains("HurtEntities", 99)) {
//	         this.hurtEntities = compound.getBoolean("HurtEntities");
//	         this.fallHurtAmount = compound.getFloat("FallHurtAmount");
//	         this.fallHurtMax = compound.getInt("FallHurtMax");
//	      } else if (this.fallTile.isIn(BlockTags.ANVIL)) {
//	         this.hurtEntities = true;
//	      }
//
//	      if (compound.contains("DropItem", 99)) {
//	         this.shouldDropItem = compound.getBoolean("DropItem");
//	      }
//
//	      if (compound.contains("TileEntityData", 10)) {
//	         this.tileEntityData = compound.getCompound("TileEntityData");
//	      }

		if (fallTile.isAir())
		{
			fallTile = BloodMagicBlocks.SHAPED_CHARGE.get().defaultBlockState();
		}

	}

	@Override
	protected void defineSynchedData()
	{
//		FallingBlockEntity d;
//		super.registerData();
		// TODO Auto-generated method stub
//		super.registerData();
		this.entityData.define(ITEMSTACK_DATA, Blocks.SAND.defaultBlockState());
	}

	public BlockState getBlockState()
	{
		// TODO Auto-generated method stub
		return this.entityData.get(ITEMSTACK_DATA);
	}

	@OnlyIn(Dist.CLIENT)
	public Level getWorldObj()
	{
		return this.level();
	}

//	@Override
//	public IPacket<?> createSpawnPacket()
//	{
//		return new SSpawnObjectPacket(this, Block.getStateId(this.getBlockState()));
//	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
