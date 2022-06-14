package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.util.Constants;

public class EntityMeteor extends ThrowableEntity
{
	private ItemStack containedStack = ItemStack.EMPTY;

	public EntityMeteor(EntityType<EntityMeteor> p_i50159_1_, World p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntityMeteor(World worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.METEOR.getEntityType(), throwerIn, worldIn);
	}

	public EntityMeteor(World worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.METEOR.getEntityType(), x, y, z, worldIn);
	}

	public void setContainedStack(ItemStack stack)
	{
		this.containedStack = stack;
	}

	@Override
	public IPacket<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound)
	{
		compound.put(Constants.NBT.ITEM, containedStack.save(new CompoundNBT()));

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
	protected void readAdditionalSaveData(CompoundNBT tagCompound)
	{
		CompoundNBT tag = tagCompound.getCompound(Constants.NBT.ITEM);
		containedStack = ItemStack.of(tag);
	}

	@Override
	public void tick()
	{
		super.tick();
		// TODO: Check doBlockCollision

//		RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
////		boolean flag = false;
//		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK)
//		{
//			BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getPos().offset(((BlockRayTraceResult) raytraceresult).getFace());
//			BlockState blockstate = this.world.getBlockState(blockpos);
//			Material material = blockstate.getMaterial();
//			if (blockstate.isAir() || blockstate.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable())
//			{
//				this.getEntityWorld().setBlockState(blockpos, BloodMagicBlocks.BLOOD_LIGHT.get().getDefaultState());
//				this.setDead();
//			}
//		}
	}

	protected void onInsideBlock(BlockState state)
	{
		if (level.isClientSide)
		{
			return;
		}

//		System.out.println("Now inside a block: " + state.getBlock());
		int i = MathHelper.floor(position().x);
		int j = MathHelper.floor(position().y);
		int k = MathHelper.floor(position().z);
		BlockPos blockpos = new BlockPos(i, j, k);

		if (!state.canOcclude())
		{
			return;
		}

//		System.out.println("Contained item: " + containedStack.toString());

		RecipeMeteor recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getMeteor(level, containedStack);
		if (recipe != null)
		{
			recipe.spawnMeteorInWorld(level, blockpos);
		}

//		this.getEntityWorld().setBlockState(blockpos, BloodMagicBlocks.AIR_RITUAL_STONE.get().getDefaultState());
//		spawnMeteorInWorld
		this.removeAfterChangingDimensions();
	}

//	protected float getGravityVelocity()
//	{
//		return 0;
//	}

	@Override
	protected void defineSynchedData()
	{
		// TODO Auto-generated method stub

	}

}