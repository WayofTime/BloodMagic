package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

public class EntitySoulSnare extends ThrowableItemProjectile
{
	public EntitySoulSnare(EntityType<EntitySoulSnare> p_i50159_1_, Level p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntitySoulSnare(Level worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.SNARE.getEntityType(), throwerIn, worldIn);
	}

	public EntitySoulSnare(Level worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.SNARE.getEntityType(), x, y, z, worldIn);
	}

	protected Item getDefaultItem()
	{
		return BloodMagicItems.SOUL_SNARE.get();
	}

	@Override
	public Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	/**
	 * Called when the arrow hits an entity
	 */
	protected void onHitEntity(EntityHitResult result)
	{
		if (result.getEntity() == this.getOwner() || this.tickCount < 2 || getCommandSenderWorld().isClientSide)
			return;

		if (result.getEntity() instanceof LivingEntity)
		{
			((LivingEntity) result.getEntity()).addEffect(new MobEffectInstance(BloodMagicPotions.SOUL_SNARE, 300, 0));
			result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), (float) 0);
		}

		this.removeAfterChangingDimensions();
	}

	@OnlyIn(Dist.CLIENT)
	private ParticleOptions makeParticle()
	{
		ItemStack itemstack = this.getItemRaw();
		return (ParticleOptions) (itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL
				: new ItemParticleOption(ParticleTypes.ITEM, itemstack));
	}

	/**
	 * Handler for {@link World#setEntityState}
	 */
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id)
	{
		if (id == 3)
		{
			ParticleOptions iparticledata = this.makeParticle();

			for (int i = 0; i < 8; ++i)
			{
				this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
