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
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.put(Constants.NBT.ITEM, containedStack.write(new CompoundNBT()));

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
	protected void readAdditional(CompoundNBT tagCompound)
	{
		CompoundNBT tag = tagCompound.getCompound(Constants.NBT.ITEM);
		containedStack = ItemStack.read(tag);
	}

	@Override
	public void tick()
	{
		super.tick();
		// TODO: Check doBlockCollision

//		RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
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
		if (world.isRemote)
		{
			return;
		}

//		System.out.println("Now inside a block: " + state.getBlock());
		int i = MathHelper.floor(getPositionVec().x);
		int j = MathHelper.floor(getPositionVec().y);
		int k = MathHelper.floor(getPositionVec().z);
		BlockPos blockpos = new BlockPos(i, j, k);

		if (!state.isSolid())
		{
			return;
		}

//		System.out.println("Contained item: " + containedStack.toString());

		RecipeMeteor recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getMeteor(world, containedStack);
		if (recipe != null)
		{
			recipe.spawnMeteorInWorld(world, blockpos);
		}

//		this.getEntityWorld().setBlockState(blockpos, BloodMagicBlocks.AIR_RITUAL_STONE.get().getDefaultState());
//		spawnMeteorInWorld
		this.setDead();
	}

//	protected float getGravityVelocity()
//	{
//		return 0;
//	}

	@Override
	protected void registerData()
	{
		// TODO Auto-generated method stub

	}

}