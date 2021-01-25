package wayoftime.bloodmagic.entity.projectile;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

public class EntityThrowingDagger extends ProjectileItemEntity
{
	@Nullable
	private BlockState inBlockState;
	protected boolean inGround;
	protected int timeInGround;
	public AbstractArrowEntity.PickupStatus pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
	public int arrowShake;
	private int ticksInGround;
	private double damage = 2.0D;
	private int knockbackStrength;
	private SoundEvent hitSound = this.getHitEntitySound();
	private IntOpenHashSet piercedEntities;
	private List<Entity> hitEntities;

//	private ItemStack containedStack = ItemStack.EMPTY;
	private double willDrop = 0;
	private EnumDemonWillType willType = EnumDemonWillType.DEFAULT;

	public EntityThrowingDagger(EntityType<EntityThrowingDagger> p_i50159_1_, World p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntityThrowingDagger(ItemStack stack, World worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER.getEntityType(), throwerIn, worldIn);
		this.setItem(stack);
		if (throwerIn instanceof PlayerEntity)
		{
			this.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
		}
	}

	public EntityThrowingDagger(ItemStack stack, World worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER.getEntityType(), x, y, z, worldIn);
		this.setItem(stack);
	}

	@Override
	public void setItem(ItemStack stack)
	{
		super.setItem(stack);
//		this.containedStack = stack;
	}

	protected Item getDefaultItem()
	{
		return BloodMagicItems.THROWING_DAGGER.get();
	}

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick()
	{
//		super.tick();
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

//		super.tick();
		this.baseTick();
		boolean flag = this.getNoClip();
		flag = false;
		Vector3d vector3d = this.getMotion();
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt(horizontalMag(vector3d));
			this.rotationYaw = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}

		BlockPos blockpos = this.getPosition();
		BlockState blockstate = this.world.getBlockState(blockpos);
		if (!blockstate.isAir(this.world, blockpos) && !flag)
		{
			VoxelShape voxelshape = blockstate.getCollisionShape(this.world, blockpos);
			if (!voxelshape.isEmpty())
			{
				Vector3d vector3d1 = this.getPositionVec();

				for (AxisAlignedBB axisalignedbb : voxelshape.toBoundingBoxList())
				{
					if (axisalignedbb.offset(blockpos).contains(vector3d1))
					{
						this.inGround = true;
						break;
					}
				}
			}
		}

		if (this.arrowShake > 0)
		{
			--this.arrowShake;
		}

		if (this.isWet())
		{
			this.extinguish();
		}

//		this.inBlockState.getBlock()

		if (this.inGround && !flag)
		{
			if (this.inBlockState != blockstate && this.func_234593_u_())
			{
				this.func_234594_z_();
			} else if (!this.world.isRemote)
			{
				this.func_225516_i_();
			}

			++this.timeInGround;
		} else
		{
			this.timeInGround = 0;
			Vector3d vector3d2 = this.getPositionVec();
			Vector3d vector3d3 = vector3d2.add(vector3d);
			RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vector3d2, vector3d3, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
			if (raytraceresult.getType() != RayTraceResult.Type.MISS)
			{
				vector3d3 = raytraceresult.getHitVec();
			}

			while (!this.removed)
			{
				EntityRayTraceResult entityraytraceresult = this.rayTraceEntities(vector3d2, vector3d3);
				if (entityraytraceresult != null)
				{
					raytraceresult = entityraytraceresult;
				}

				if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY)
				{
					Entity entity = ((EntityRayTraceResult) raytraceresult).getEntity();
					Entity entity1 = this.func_234616_v_();
					if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity) entity1).canAttackPlayer((PlayerEntity) entity))
					{
						raytraceresult = null;
						entityraytraceresult = null;
					}
				}

				if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult))
				{
					this.onImpact(raytraceresult);
					this.isAirBorne = true;
				}

				if (entityraytraceresult == null || this.getPierceLevel() <= 0)
				{
					break;
				}

				raytraceresult = null;
			}

			vector3d = this.getMotion();
			double d3 = vector3d.x;
			double d4 = vector3d.y;
			double d0 = vector3d.z;
			if (this.getIsCritical())
			{
				for (int i = 0; i < 4; ++i)
				{
					this.world.addParticle(ParticleTypes.CRIT, this.getPosX() + d3 * (double) i / 4.0D, this.getPosY() + d4 * (double) i / 4.0D, this.getPosZ() + d0 * (double) i / 4.0D, -d3, -d4 + 0.2D, -d0);
				}
			}

			double d5 = this.getPosX() + d3;
			double d1 = this.getPosY() + d4;
			double d2 = this.getPosZ() + d0;
			float f1 = MathHelper.sqrt(horizontalMag(vector3d));
			if (flag)
			{
				this.rotationYaw = (float) (MathHelper.atan2(-d3, -d0) * (double) (180F / (float) Math.PI));
			} else
			{
				this.rotationYaw = (float) (MathHelper.atan2(d3, d0) * (double) (180F / (float) Math.PI));
			}

			this.rotationPitch = (float) (MathHelper.atan2(d4, (double) f1) * (double) (180F / (float) Math.PI));
			this.rotationPitch = func_234614_e_(this.prevRotationPitch, this.rotationPitch);
			this.rotationYaw = func_234614_e_(this.prevRotationYaw, this.rotationYaw);
			float f2 = 0.99F;
			float f3 = 0.05F;
			if (this.isInWater())
			{
				for (int j = 0; j < 4; ++j)
				{
					float f4 = 0.25F;
					this.world.addParticle(ParticleTypes.BUBBLE, d5 - d3 * 0.25D, d1 - d4 * 0.25D, d2 - d0 * 0.25D, d3, d4, d0);
				}

				f2 = this.getWaterDrag();
			}

			this.setMotion(vector3d.scale((double) f2));
			if (!this.hasNoGravity() && !flag)
			{
				Vector3d vector3d4 = this.getMotion();
				this.setMotion(vector3d4.x, vector3d4.y - (double) 0.05F, vector3d4.z);
			}

			this.setPosition(d5, d1, d2);
			this.doBlockCollisions();
		}
	}

	@Override
	public void move(MoverType typeIn, Vector3d pos)
	{
		super.move(typeIn, pos);
		if (typeIn != MoverType.SELF && this.func_234593_u_())
		{
			this.func_234594_z_();
		}

	}

	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putShort("life", (short) this.ticksInGround);
		if (this.inBlockState != null)
		{
			compound.put("inBlockState", NBTUtil.writeBlockState(this.inBlockState));
		}

		compound.putByte("shake", (byte) this.arrowShake);
		compound.putBoolean("inGround", this.inGround);
		compound.putByte("pickup", (byte) this.pickupStatus.ordinal());
		compound.putDouble("damage", this.damage);
//	      compound.putBoolean("crit", this.getIsCritical());
//	      compound.putByte("PierceLevel", this.getPierceLevel());
		compound.putString("SoundEvent", Registry.SOUND_EVENT.getKey(this.hitSound).toString());
//	      compound.putBoolean("ShotFromCrossbow", this.getShotFromCrossbow());
		compound.putDouble("willDrop", willDrop);
//		this.containedStack.write(compound);
		compound.putString("willType", this.willType.name);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		this.ticksInGround = compound.getShort("life");
		if (compound.contains("inBlockState", 10))
		{
			this.inBlockState = NBTUtil.readBlockState(compound.getCompound("inBlockState"));
		}

		this.arrowShake = compound.getByte("shake") & 255;
		this.inGround = compound.getBoolean("inGround");
		if (compound.contains("damage", 99))
		{
			this.damage = compound.getDouble("damage");
		}

		if (compound.contains("pickup", 99))
		{
			this.pickupStatus = AbstractArrowEntity.PickupStatus.getByOrdinal(compound.getByte("pickup"));
		} else if (compound.contains("player", 99))
		{
			this.pickupStatus = compound.getBoolean("player") ? AbstractArrowEntity.PickupStatus.ALLOWED
					: AbstractArrowEntity.PickupStatus.DISALLOWED;
		}
		this.willDrop = compound.getDouble("willDrop");
//		this.containedStack = ItemStack.read(compound);
		this.willType = EnumDemonWillType.getType(compound.getString("willType"));

//	      this.setIsCritical(compound.getBoolean("crit"));
//	      this.setPierceLevel(compound.getByte("PierceLevel"));
//	      if (compound.contains("SoundEvent", 8)) {
//	         this.hitSound = Registry.SOUND_EVENT.getOptional(new ResourceLocation(compound.getString("SoundEvent"))).orElse(this.getHitEntitySound());
//	      }
//
//	      this.setShotFromCrossbow(compound.getBoolean("ShotFromCrossbow"));
	}

	public void setDamage(double damage)
	{
		this.damage = damage;
	}

	public double getDamage()
	{
		return this.damage;
	}

	protected void onEntityHit(EntityRayTraceResult p_213868_1_)
	{
		super.onEntityHit(p_213868_1_);
		Entity entity = p_213868_1_.getEntity();
		float f = (float) this.getMotion().length();
		int i = MathHelper.ceil(MathHelper.clamp(this.damage, 0.0D, 2.147483647E9D));
		if (this.getPierceLevel() > 0)
		{
			if (this.piercedEntities == null)
			{
				this.piercedEntities = new IntOpenHashSet(5);
			}

			if (this.hitEntities == null)
			{
				this.hitEntities = Lists.newArrayListWithCapacity(5);
			}

			if (this.piercedEntities.size() >= this.getPierceLevel() + 1)
			{
				this.remove();
				return;
			}

			this.piercedEntities.add(entity.getEntityId());
		}

		if (this.getIsCritical())
		{
			long j = (long) this.rand.nextInt(i / 2 + 2);
			i = (int) Math.min(j + (long) i, 2147483647L);
		}

		Entity entity1 = this.func_234616_v_();
		DamageSource damagesource;
		if (entity1 == null)
		{
			damagesource = DamageSource.causeThrownDamage(this, this);
		} else
		{
			damagesource = DamageSource.causeThrownDamage(this, entity1);
			if (entity1 instanceof LivingEntity)
			{
				((LivingEntity) entity1).setLastAttackedEntity(entity);
			}
		}

		boolean flag = entity.getType() == EntityType.ENDERMAN;
		int k = entity.getFireTimer();
		if (this.isBurning() && !flag)
		{
			entity.setFire(5);
		}

		if (entity.attackEntityFrom(damagesource, (float) i))
		{
			if (flag)
			{
				return;
			}

			if (!entity.isAlive() && entity1 instanceof PlayerEntity && entity instanceof LivingEntity)
			{
				PlayerDemonWillHandler.addDemonWill(willType, (PlayerEntity) entity1, this.getWillDropForMobHealth(((LivingEntity) entity).getMaxHealth()));
			}

			if (entity instanceof LivingEntity)
			{
				LivingEntity livingentity = (LivingEntity) entity;
//				if (!this.world.isRemote && this.getPierceLevel() <= 0)
//				{
//					livingentity.setArrowCountInEntity(livingentity.getArrowCountInEntity() + 1);
//				}

				if (this.knockbackStrength > 0)
				{
					Vector3d vector3d = this.getMotion().mul(1.0D, 0.0D, 1.0D).normalize().scale((double) this.knockbackStrength * 0.6D);
					if (vector3d.lengthSquared() > 0.0D)
					{
						livingentity.addVelocity(vector3d.x, 0.1D, vector3d.z);
					}
				}

				if (!this.world.isRemote && entity1 instanceof LivingEntity)
				{
					EnchantmentHelper.applyThornEnchantments(livingentity, entity1);
					EnchantmentHelper.applyArthropodEnchantments((LivingEntity) entity1, livingentity);
				}

				this.daggerHit(livingentity);
				if (entity1 != null && livingentity != entity1 && livingentity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity && !this.isSilent())
				{
					((ServerPlayerEntity) entity1).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241770_g_, 0.0F));
				}

				if (!entity.isAlive() && this.hitEntities != null)
				{
					this.hitEntities.add(livingentity);
				}
			}

			this.playSound(this.hitSound, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			if (this.getPierceLevel() <= 0)
			{
				this.remove();
			}
		} else
		{
			entity.forceFireTicks(k);
			this.setMotion(this.getMotion().scale(-0.1D));
			this.rotationYaw += 180.0F;
			this.prevRotationYaw += 180.0F;
			if (!this.world.isRemote && this.getMotion().lengthSquared() < 1.0E-7D)
			{
				if (this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED)
				{
					this.entityDropItem(this.getArrowStack(), 0.1F);
				}

				this.remove();
			}
		}

	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(PlayerEntity entityIn)
	{
		if (!this.world.isRemote && (this.inGround || this.getNoClip()) && this.arrowShake <= 0)
		{
			boolean flag = this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED || this.pickupStatus == AbstractArrowEntity.PickupStatus.CREATIVE_ONLY && entityIn.abilities.isCreativeMode || this.getNoClip() && this.func_234616_v_().getUniqueID() == entityIn.getUniqueID();
			if (this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack()))
			{
				flag = false;
			}

			if (flag)
			{
//				System.out.println("Um test?");

//				entityIn.onItemPickup(this, 1);
				world.playSound(null, entityIn.getPosX(), entityIn.getPosY() + 0.5, entityIn.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				this.remove();
			}

		}
	}

	protected ItemStack getArrowStack()
	{
		// Gets the item from the data manager
		return getItem();
	}

	// OnHitBlock
	protected void func_230299_a_(BlockRayTraceResult p_230299_1_)
	{
		this.inBlockState = this.world.getBlockState(p_230299_1_.getPos());
		super.func_230299_a_(p_230299_1_);
		Vector3d vector3d = p_230299_1_.getHitVec().subtract(this.getPosX(), this.getPosY(), this.getPosZ());
		this.setMotion(vector3d);
		Vector3d vector3d1 = vector3d.normalize().scale((double) 0.05F);
		this.setRawPosition(this.getPosX() - vector3d1.x, this.getPosY() - vector3d1.y, this.getPosZ() - vector3d1.z);
		this.playSound(this.getHitGroundSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		this.inGround = true;
		this.arrowShake = 7;
//		this.setIsCritical(false);
//		this.setPierceLevel((byte) 0);
		this.setHitSound(SoundEvents.ENTITY_ARROW_HIT);
//		this.setShotFromCrossbow(false);
		this.func_213870_w();
	}

	private void func_234594_z_()
	{
		this.inGround = false;
		Vector3d vector3d = this.getMotion();
		this.setMotion(vector3d.mul((double) (this.rand.nextFloat() * 0.2F), (double) (this.rand.nextFloat() * 0.2F), (double) (this.rand.nextFloat() * 0.2F)));
		this.ticksInGround = 0;
	}

	private boolean func_234593_u_()
	{
		return this.inGround && this.world.hasNoCollisions((new AxisAlignedBB(this.getPositionVec(), this.getPositionVec())).grow(0.06D));
	}

	protected void func_225516_i_()
	{
		++this.ticksInGround;
		if (this.ticksInGround >= 1200)
		{
			this.remove();
		}

	}

	protected void daggerHit(LivingEntity living)
	{
	}

	/**
	 * The sound made when an entity is hit by this projectile
	 */
	protected SoundEvent getHitEntitySound()
	{
		return SoundEvents.ENTITY_ARROW_HIT;
	}

	protected final SoundEvent getHitGroundSound()
	{
		return this.hitSound;
	}

	private void func_213870_w()
	{
		if (this.hitEntities != null)
		{
			this.hitEntities.clear();
		}

		if (this.piercedEntities != null)
		{
			this.piercedEntities.clear();
		}

	}

	public void setHitSound(SoundEvent soundIn)
	{
		this.hitSound = soundIn;
	}

	public boolean getNoClip()
	{
		if (!this.world.isRemote)
		{
			return this.noClip;
		} else
		{
			return false;
//			return (this.dataManager.get(CRITICAL) & 2) != 0;
		}
	}

	public boolean getIsCritical()
	{
//		byte b0 = this.dataManager.get(CRITICAL);
//		return (b0 & 1) != 0;
		return false;
	}

	public byte getPierceLevel()
	{
		return 0;
//		return this.dataManager.get(PIERCE_LEVEL);
	}

	protected float getWaterDrag()
	{
		return 0.6F;
	}

	/**
	 * Gets the EntityRayTraceResult representing the entity hit
	 */
	@Nullable
	protected EntityRayTraceResult rayTraceEntities(Vector3d startVec, Vector3d endVec)
	{
		return ProjectileHelper.rayTraceEntities(this.world, this, startVec, endVec, this.getBoundingBox().expand(this.getMotion()).grow(1.0D), this::func_230298_a_);
	}

	protected boolean func_230298_a_(Entity p_230298_1_)
	{
		return super.func_230298_a_(p_230298_1_) && (this.piercedEntities == null || !this.piercedEntities.contains(p_230298_1_.getEntityId()));
	}

//	protected float getGravityVelocity()
//	{
//		return 0;
//	}

	public void setWillDrop(double willDrop)
	{
		this.willDrop = willDrop;
	}

	public double getWillDropForMobHealth(double hp)
	{
		return this.willDrop * hp / 20D;
	}

	public void setWillType(EnumDemonWillType type)
	{
		this.willType = type;
	}

	@OnlyIn(Dist.CLIENT)
	private IParticleData makeParticle()
	{
		ItemStack itemstack = this.func_213882_k();
		return (IParticleData) (itemstack.isEmpty() ? ParticleTypes.LAVA
				: new ItemParticleData(ParticleTypes.ITEM, itemstack));
	}

	/**
	 * Handler for {@link World#setEntityState}
	 */
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 3)
		{
			IParticleData iparticledata = this.makeParticle();

			for (int i = 0; i < 8; ++i)
			{
				this.world.addParticle(iparticledata, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
}