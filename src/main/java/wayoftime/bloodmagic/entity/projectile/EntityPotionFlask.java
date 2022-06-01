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
	public static final Predicate<LivingEntity> WATER_SENSITIVE = LivingEntity::isWaterSensitive;

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
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	protected float getGravityVelocity()
	{
		return 0.05F;
	}

	protected void func_230299_a_(BlockRayTraceResult p_230299_1_)
	{
		super.func_230299_a_(p_230299_1_);
		if (!this.world.isRemote)
		{
			ItemStack itemstack = this.getItem();
			Potion potion = PotionUtils.getPotionFromItem(itemstack);
			List<EffectInstance> list = PotionUtils.getEffectsFromStack(itemstack);
			boolean flag = potion == Potions.WATER && list.isEmpty();
			Direction direction = p_230299_1_.getFace();
			BlockPos blockpos = p_230299_1_.getPos();
			BlockPos blockpos1 = blockpos.offset(direction);
			if (flag)
			{
				this.extinguishFires(blockpos1, direction);
				this.extinguishFires(blockpos1.offset(direction.getOpposite()), direction);

				for (Direction direction1 : Direction.Plane.HORIZONTAL)
				{
					this.extinguishFires(blockpos1.offset(direction1), direction1);
				}
			}

		}
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected void onImpact(RayTraceResult result)
	{
		super.onImpact(result);
		if (!this.world.isRemote)
		{
			ItemStack itemstack = this.getItem();
			Potion potion = PotionUtils.getPotionFromItem(itemstack);
			List<EffectInstance> list = PotionUtils.getEffectsFromStack(itemstack);
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
					this.func_213888_a(list, result.getType() == RayTraceResult.Type.ENTITY
							? ((EntityRayTraceResult) result).getEntity()
							: null);
				}
			}

			int i = potion.hasInstantEffect() ? 2007 : 2002;
			this.world.playEvent(i, this.getPosition(), PotionUtils.getColor(itemstack));
			this.remove();
		}
	}

	private void applyWater()
	{
		AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb, WATER_SENSITIVE);
		if (!list.isEmpty())
		{
			for (LivingEntity livingentity : list)
			{
				double d0 = this.getDistanceSq(livingentity);
				if (d0 < 16.0D && livingentity.isWaterSensitive())
				{
					livingentity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(livingentity, this.func_234616_v_()), 1.0F);
				}
			}
		}

	}

	private void func_213888_a(List<EffectInstance> p_213888_1_, @Nullable Entity p_213888_2_)
	{
		AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
		List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
		if (!list.isEmpty())
		{
			for (LivingEntity livingentity : list)
			{
				if (livingentity.canBeHitWithPotion())
				{
					double d0 = this.getDistanceSq(livingentity);
					if (d0 < 16.0D)
					{
						double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
						if (livingentity == p_213888_2_)
						{
							d1 = 1.0D;
						}

						for (EffectInstance effectinstance : p_213888_1_)
						{
							Effect effect = effectinstance.getPotion();
							if (effect.isInstant())
							{
								effect.affectEntity(this, this.func_234616_v_(), livingentity, effectinstance.getAmplifier(), d1);
							} else
							{
								int i = (int) (d1 * (double) effectinstance.getDuration() + 0.5D);
								if (i > 20)
								{
									livingentity.addPotionEffect(new EffectInstance(effect, i, effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.doesShowParticles()));
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
		AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ());
		Entity entity = this.func_234616_v_();
		if (entity instanceof LivingEntity)
		{
			areaeffectcloudentity.setOwner((LivingEntity) entity);
		}

		areaeffectcloudentity.setRadius(3.0F);
		areaeffectcloudentity.setRadiusOnUse(-0.5F);
		areaeffectcloudentity.setWaitTime(10);
		areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
		areaeffectcloudentity.setPotion(p_190542_2_);

		for (EffectInstance effectinstance : PotionUtils.getFullEffectsFromItem(p_190542_1_))
		{
			areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
		}

		CompoundNBT compoundnbt = p_190542_1_.getTag();
		if (compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99))
		{
			areaeffectcloudentity.setColor(compoundnbt.getInt("CustomPotionColor"));
		}

		this.world.addEntity(areaeffectcloudentity);
	}

	public boolean isLingering()
	{
		return isLingering;
	}

	public void extinguishFires(BlockPos pos, Direction p_184542_2_)
	{
		BlockState blockstate = this.world.getBlockState(pos);
		if (blockstate.isIn(BlockTags.FIRE))
		{
			this.world.removeBlock(pos, false);
		} else if (CampfireBlock.isLit(blockstate))
		{
			this.world.playEvent((PlayerEntity) null, 1009, pos, 0);
			CampfireBlock.extinguish(this.world, pos, blockstate);
			this.world.setBlockState(pos, blockstate.with(CampfireBlock.LIT, Boolean.valueOf(false)));
		}

	}
}