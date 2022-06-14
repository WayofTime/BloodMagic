package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.potion.BloodMagicPotions;

public class EntitySoulSnare extends ProjectileItemEntity
{
	public EntitySoulSnare(EntityType<EntitySoulSnare> p_i50159_1_, World p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntitySoulSnare(World worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.SNARE.getEntityType(), throwerIn, worldIn);
	}

	public EntitySoulSnare(World worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.SNARE.getEntityType(), x, y, z, worldIn);
	}

	protected Item getDefaultItem()
	{
		return BloodMagicItems.SOUL_SNARE.get();
	}

	@Override
	public IPacket<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	/**
	 * Called when the arrow hits an entity
	 */
	protected void onHitEntity(EntityRayTraceResult result)
	{
		if (result.getEntity() == this.getOwner() || this.tickCount < 2 || getCommandSenderWorld().isClientSide)
			return;

		if (result.getEntity() instanceof LivingEntity)
		{
			((LivingEntity) result.getEntity()).addEffect(new EffectInstance(BloodMagicPotions.SOUL_SNARE, 300, 0));
			result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), (float) 0);
		}

		this.removeAfterChangingDimensions();
	}

	@OnlyIn(Dist.CLIENT)
	private IParticleData makeParticle()
	{
		ItemStack itemstack = this.getItemRaw();
		return (IParticleData) (itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL
				: new ItemParticleData(ParticleTypes.ITEM, itemstack));
	}

	/**
	 * Handler for {@link World#setEntityState}
	 */
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id)
	{
		if (id == 3)
		{
			IParticleData iparticledata = this.makeParticle();

			for (int i = 0; i < 8; ++i)
			{
				this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
