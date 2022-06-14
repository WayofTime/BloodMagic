package wayoftime.bloodmagic.entity.projectile;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.anointment.AnointmentHolder;
import wayoftime.bloodmagic.common.block.BlockShapedExplosive;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.tile.TileExplosiveCharge;

public class EntityShapedCharge extends ThrowableEntity
{
	private static final DataParameter<Optional<BlockState>> ITEMSTACK_DATA = EntityDataManager.defineId(EntityShapedCharge.class, DataSerializers.BLOCK_STATE);
//	private BlockState fallTile = BloodMagicBlocks.SHAPED_CHARGE.get().getDefaultState();
	private AnointmentHolder holder;

	public EntityShapedCharge(EntityType<EntityShapedCharge> p_i50159_1_, World p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntityShapedCharge(World worldIn, Block block, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.SHAPED_CHARGE.getEntityType(), throwerIn, worldIn);
//		this.fallTile = block.getDefaultState();
		this.setFallTile(block.defaultBlockState());
	}

	public EntityShapedCharge(World worldIn, Block block, double x, double y, double z)
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
		this.entityData.set(ITEMSTACK_DATA, Optional.of(state));
	}

	@Override
	public void tick()
	{
		super.tick();
		RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
//		boolean flag = false;
		if (level.isClientSide)
		{
			return;
		}
		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK)
		{
			Direction faceHit = ((BlockRayTraceResult) raytraceresult).getDirection();
			BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getBlockPos().relative(((BlockRayTraceResult) raytraceresult).getDirection());
			BlockState blockstate = this.level.getBlockState(blockpos);
			Material material = blockstate.getMaterial();
//		      return state.isAir() || state.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
			BlockState fallTile = this.getBlockState();
			if (blockstate.isAir() || blockstate.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable())
			{
				this.getCommandSenderWorld().setBlockAndUpdate(blockpos, fallTile.setValue(BlockShapedExplosive.ATTACHED, faceHit));
				TileEntity tile = this.getCommandSenderWorld().getBlockEntity(blockpos);
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
	protected void addAdditionalSaveData(CompoundNBT compound)
	{
		compound.put("BlockState", NBTUtil.writeBlockState(this.getBlockState()));
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
	protected void readAdditionalSaveData(CompoundNBT compound)
	{
		BlockState fallTile = NBTUtil.readBlockState(compound.getCompound("BlockState"));
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
		this.entityData.define(ITEMSTACK_DATA, Optional.of(Blocks.SAND.defaultBlockState()));
	}

	public BlockState getBlockState()
	{
		// TODO Auto-generated method stub
		return this.entityData.get(ITEMSTACK_DATA).get();
	}

	@OnlyIn(Dist.CLIENT)
	public World getWorldObj()
	{
		return this.level;
	}

//	@Override
//	public IPacket<?> createSpawnPacket()
//	{
//		return new SSpawnObjectPacket(this, Block.getStateId(this.getBlockState()));
//	}

	@Override
	public IPacket<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
