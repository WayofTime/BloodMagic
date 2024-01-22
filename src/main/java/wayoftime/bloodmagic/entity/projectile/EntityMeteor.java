package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.util.Constants;

public class EntityMeteor extends ThrowableProjectile
{
	private ItemStack containedStack = ItemStack.EMPTY;

	public EntityMeteor(EntityType<EntityMeteor> p_i50159_1_, Level p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntityMeteor(Level worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.METEOR.getEntityType(), throwerIn, worldIn);
	}

	public EntityMeteor(Level worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.METEOR.getEntityType(), x, y, z, worldIn);
	}

	public void setContainedStack(ItemStack stack)
	{
		this.containedStack = stack;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound)
	{
		compound.put(Constants.NBT.ITEM, containedStack.save(new CompoundTag()));

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
	protected void readAdditionalSaveData(CompoundTag tagCompound)
	{
		CompoundTag tag = tagCompound.getCompound(Constants.NBT.ITEM);
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
		if (level().isClientSide)
		{
			return;
		}

//		System.out.println("Now inside a block: " + state.getBlock());
		int i = Mth.floor(position().x);
		int j = Mth.floor(position().y);
		int k = Mth.floor(position().z);
		BlockPos blockpos = new BlockPos(i, j, k);

		if (!state.canOcclude())
		{
			return;
		}

//		System.out.println("Contained item: " + containedStack.toString());

		RecipeMeteor recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getMeteor(level(), containedStack);
		if (recipe != null)
		{
			recipe.spawnMeteorInWorld(level(), blockpos);
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