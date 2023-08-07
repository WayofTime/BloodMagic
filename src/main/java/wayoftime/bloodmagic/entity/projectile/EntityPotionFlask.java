package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class EntityPotionFlask extends ThrowableItemProjectile implements ItemSupplier
{
	public boolean isLingering = false;
	public static final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isSensitiveToWater;

	public EntityPotionFlask(EntityType<? extends EntityPotionFlask> typeIn, Level worldIn)
	{
		super(typeIn, worldIn);
	}

	public EntityPotionFlask(Level worldIn, LivingEntity livingEntityIn)
	{
		super(EntityType.POTION, livingEntityIn, worldIn);
	}

	public EntityPotionFlask(Level worldIn, double x, double y, double z)
	{
		super(EntityType.POTION, x, y, z, worldIn);
	}

	protected Item getDefaultItem()
	{
		return Items.SPLASH_POTION;
	}

	public void setIsLingering(boolean isLingering)
	{
		this.isLingering = isLingering;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravity()
	{
		return 0.05F;
	}

	protected void onHitBlock(BlockHitResult p_230299_1_)
	{
		super.onHitBlock(p_230299_1_);
		if (!this.level().isClientSide)
		{
			ItemStack itemstack = this.getItem();
			Potion potion = PotionUtils.getPotion(itemstack);
			List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
			boolean flag = potion == Potions.WATER && list.isEmpty();
			Direction direction = p_230299_1_.getDirection();
			BlockPos blockpos = p_230299_1_.getBlockPos();
			BlockPos blockpos1 = blockpos.relative(direction);
			if (flag)
			{
				this.extinguishFires(blockpos1, direction);
				this.extinguishFires(blockpos1.relative(direction.getOpposite()), direction);

				for (Direction direction1 : Direction.Plane.HORIZONTAL)
				{
					this.extinguishFires(blockpos1.relative(direction1), direction1);
				}
			}

		}
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected void onHit(HitResult result)
	{
		super.onHit(result);
		if (!this.level().isClientSide)
		{
			ItemStack itemstack = this.getItem();
			Potion potion = PotionUtils.getPotion(itemstack);
			List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
			boolean flag = potion == Potions.WATER && list.isEmpty();
			if (flag)
			{
				this.applyWater();
			} else if (!list.isEmpty())
			{
				if (this.isLingering())
				{
					this.makeAreaOfEffectCloud(itemstack, potion);
				} else
				{
					this.applySplash(list, result.getType() == HitResult.Type.ENTITY
							? ((EntityHitResult) result).getEntity()
							: null);
				}
			}

			int i = potion.hasInstantEffects() ? 2007 : 2002;
			this.level().levelEvent(i, this.blockPosition(), PotionUtils.getColor(itemstack));
			this.remove(RemovalReason.KILLED);
		}
	}

	private void applyWater()
	{
		AABB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb, WATER_SENSITIVE);
		if (!list.isEmpty())
		{
			for (LivingEntity livingentity : list)
			{
				double d0 = this.distanceToSqr(livingentity);
				if (d0 < 16.0D && livingentity.isSensitiveToWater())
				{
					livingentity.hurt(livingentity.damageSources().indirectMagic(livingentity, this.getOwner()), 1.0F);
				}
			}
		}

	}

	private void applySplash(List<MobEffectInstance> p_213888_1_, @Nullable Entity p_213888_2_)
	{
		AABB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
		if (!list.isEmpty())
		{
			for (LivingEntity livingentity : list)
			{
				if (livingentity.isAffectedByPotions())
				{
					double d0 = this.distanceToSqr(livingentity);
					if (d0 < 16.0D)
					{
						double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
						if (livingentity == p_213888_2_)
						{
							d1 = 1.0D;
						}

						for (MobEffectInstance effectinstance : p_213888_1_)
						{
							MobEffect effect = effectinstance.getEffect();
							if (effect.isInstantenous())
							{
								effect.applyInstantenousEffect(this, this.getOwner(), livingentity, effectinstance.getAmplifier(), d1);
							} else
							{
								int i = (int) (d1 * (double) effectinstance.getDuration() + 0.5D);
								if (i > 20)
								{
									livingentity.addEffect(new MobEffectInstance(effect, i, effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.isVisible()));
								}
							}
						}
					}
				}
			}
		}

	}

	public void makeAreaOfEffectCloud(ItemStack p_190542_1_, Potion p_190542_2_)
	{
		AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
		Entity entity = this.getOwner();
		if (entity instanceof LivingEntity)
		{
			areaeffectcloudentity.setOwner((LivingEntity) entity);
		}

		areaeffectcloudentity.setRadius(3.0F);
		areaeffectcloudentity.setRadiusOnUse(-0.5F);
		areaeffectcloudentity.setWaitTime(10);
		areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
		areaeffectcloudentity.setPotion(p_190542_2_);

		for (MobEffectInstance effectinstance : PotionUtils.getCustomEffects(p_190542_1_))
		{
			areaeffectcloudentity.addEffect(new MobEffectInstance(effectinstance));
		}

		CompoundTag compoundnbt = p_190542_1_.getTag();
		if (compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99))
		{
			areaeffectcloudentity.setFixedColor(compoundnbt.getInt("CustomPotionColor"));
		}

		this.level().addFreshEntity(areaeffectcloudentity);
	}

	public boolean isLingering()
	{
		return isLingering;
	}

	public void extinguishFires(BlockPos pos, Direction p_184542_2_)
	{
		BlockState blockstate = this.level().getBlockState(pos);
		if (blockstate.is(BlockTags.FIRE))
		{
			this.level().removeBlock(pos, false);
		} else if (CampfireBlock.isLitCampfire(blockstate))
		{
			this.level().levelEvent((Player) null, 1009, pos, 0);
			CampfireBlock.dowse(null, this.level(), pos, blockstate);
			this.level().setBlockAndUpdate(pos, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
		}

	}
}