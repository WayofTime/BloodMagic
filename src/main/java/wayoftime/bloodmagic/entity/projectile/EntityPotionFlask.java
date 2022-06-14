package wayoftime.bloodmagic.entity.projectile;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityPotionFlask extends ProjectileItemEntity implements IRendersAsItem
{
	public boolean isLingering = false;
	public static final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isSensitiveToWater;

	public EntityPotionFlask(EntityType<? extends EntityPotionFlask> typeIn, World worldIn)
	{
		super(typeIn, worldIn);
	}

	public EntityPotionFlask(World worldIn, LivingEntity livingEntityIn)
	{
		super(EntityType.POTION, livingEntityIn, worldIn);
	}

	public EntityPotionFlask(World worldIn, double x, double y, double z)
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
	public IPacket<?> getAddEntityPacket()
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

	protected void onHitBlock(BlockRayTraceResult p_230299_1_)
	{
		super.onHitBlock(p_230299_1_);
		if (!this.level.isClientSide)
		{
			ItemStack itemstack = this.getItem();
			Potion potion = PotionUtils.getPotion(itemstack);
			List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
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
	protected void onHit(RayTraceResult result)
	{
		super.onHit(result);
		if (!this.level.isClientSide)
		{
			ItemStack itemstack = this.getItem();
			Potion potion = PotionUtils.getPotion(itemstack);
			List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
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
					this.applySplash(list, result.getType() == RayTraceResult.Type.ENTITY
							? ((EntityRayTraceResult) result).getEntity()
							: null);
				}
			}

			int i = potion.hasInstantEffects() ? 2007 : 2002;
			this.level.levelEvent(i, this.blockPosition(), PotionUtils.getColor(itemstack));
			this.remove();
		}
	}

	private void applyWater()
	{
		AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb, WATER_SENSITIVE);
		if (!list.isEmpty())
		{
			for (LivingEntity livingentity : list)
			{
				double d0 = this.distanceToSqr(livingentity);
				if (d0 < 16.0D && livingentity.isSensitiveToWater())
				{
					livingentity.hurt(DamageSource.indirectMagic(livingentity, this.getOwner()), 1.0F);
				}
			}
		}

	}

	private void applySplash(List<EffectInstance> p_213888_1_, @Nullable Entity p_213888_2_)
	{
		AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, axisalignedbb);
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

						for (EffectInstance effectinstance : p_213888_1_)
						{
							Effect effect = effectinstance.getEffect();
							if (effect.isInstantenous())
							{
								effect.applyInstantenousEffect(this, this.getOwner(), livingentity, effectinstance.getAmplifier(), d1);
							} else
							{
								int i = (int) (d1 * (double) effectinstance.getDuration() + 0.5D);
								if (i > 20)
								{
									livingentity.addEffect(new EffectInstance(effect, i, effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.isVisible()));
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
		AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
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

		for (EffectInstance effectinstance : PotionUtils.getCustomEffects(p_190542_1_))
		{
			areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
		}

		CompoundNBT compoundnbt = p_190542_1_.getTag();
		if (compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99))
		{
			areaeffectcloudentity.setFixedColor(compoundnbt.getInt("CustomPotionColor"));
		}

		this.level.addFreshEntity(areaeffectcloudentity);
	}

	public boolean isLingering()
	{
		return isLingering;
	}

	public void extinguishFires(BlockPos pos, Direction p_184542_2_)
	{
		BlockState blockstate = this.level.getBlockState(pos);
		if (blockstate.is(BlockTags.FIRE))
		{
			this.level.removeBlock(pos, false);
		} else if (CampfireBlock.isLitCampfire(blockstate))
		{
			this.level.levelEvent((PlayerEntity) null, 1009, pos, 0);
			CampfireBlock.dowse(this.level, pos, blockstate);
			this.level.setBlockAndUpdate(pos, blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false)));
		}

	}
}