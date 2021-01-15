package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.common.block.BlockShapedExplosive;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;

public class EntityShapedCharge extends ThrowableEntity
{
//	private static final DataParameter<Optional<BlockState>> ITEMSTACK_DATA = EntityDataManager.createKey(ProjectileItemEntity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private BlockState fallTile = BloodMagicBlocks.SHAPED_CHARGE.get().getDefaultState();

	public EntityShapedCharge(EntityType<EntityShapedCharge> p_i50159_1_, World p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntityShapedCharge(World worldIn, Block block, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.SHAPED_CHARGE.getEntityType(), throwerIn, worldIn);
		this.fallTile = block.getDefaultState();
	}

	public EntityShapedCharge(World worldIn, Block block, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.SHAPED_CHARGE.getEntityType(), x, y, z, worldIn);
		this.fallTile = block.getDefaultState();
	}

	@Override
	public void tick()
	{
		super.tick();
		RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
//		boolean flag = false;
		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK)
		{
			Direction faceHit = ((BlockRayTraceResult) raytraceresult).getFace();
			BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getPos().offset(((BlockRayTraceResult) raytraceresult).getFace());
			BlockState blockstate = this.world.getBlockState(blockpos);
			Material material = blockstate.getMaterial();
//		      return state.isAir() || state.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
			if (blockstate.isAir() || blockstate.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable())
			{
				this.getEntityWorld().setBlockState(blockpos, fallTile.with(BlockShapedExplosive.ATTACHED, faceHit));
				this.setDead();
			} else
			{
//				BlockItem d;
				this.entityDropItem(fallTile.getBlock());
				this.setDead();
//				blockstate.isReplaceable(BlockItemUseContext)
			}
		}
	}

	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.put("BlockState", NBTUtil.writeBlockState(this.fallTile));
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
	protected void readAdditional(CompoundNBT compound)
	{
		this.fallTile = NBTUtil.readBlockState(compound.getCompound("BlockState"));
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

		System.out.println("Reading additional data");

		if (this.fallTile.isAir())
		{
			this.fallTile = BloodMagicBlocks.SHAPED_CHARGE.get().getDefaultState();
		}

	}

	@Override
	protected void registerData()
	{
		// TODO Auto-generated method stub

	}

	public BlockState getBlockState()
	{
		// TODO Auto-generated method stub
		return fallTile;
	}

	@OnlyIn(Dist.CLIENT)
	public World getWorldObj()
	{
		return this.world;
	}

//	@Override
//	public IPacket<?> createSpawnPacket()
//	{
//		return new SSpawnObjectPacket(this, Block.getStateId(this.getBlockState()));
//	}

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
