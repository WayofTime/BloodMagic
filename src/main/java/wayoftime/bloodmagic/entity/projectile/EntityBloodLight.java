package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;

public class EntityBloodLight extends ThrowableItemProjectile
{
	public EntityBloodLight(EntityType<EntityBloodLight> p_i50159_1_, Level p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntityBloodLight(Level worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.BLOOD_LIGHT.getEntityType(), throwerIn, worldIn);
	}

	public EntityBloodLight(Level worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.BLOOD_LIGHT.getEntityType(), x, y, z, worldIn);
	}

	protected Item getDefaultItem()
	{
		return BloodMagicItems.REAGENT_BLOOD_LIGHT.get();
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick()
	{
		super.tick();
		HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
//		boolean flag = false;
		if (raytraceresult.getType() == HitResult.Type.BLOCK)
		{
			BlockPos blockpos = ((BlockHitResult) raytraceresult).getBlockPos().relative(((BlockHitResult) raytraceresult).getDirection());
			BlockState blockstate = this.level().getBlockState(blockpos);
			if (blockstate.isAir() || blockstate.is(BlockTags.FIRE) || blockstate.canBeReplaced() || blockstate.liquid())
			{
				this.getCommandSenderWorld().setBlockAndUpdate(blockpos, BloodMagicBlocks.BLOOD_LIGHT.get().defaultBlockState());
				this.removeAfterChangingDimensions();
			}
		}
	}

	protected float getGravity()
	{
		return 0;
	}

	@OnlyIn(Dist.CLIENT)
	private ParticleOptions makeParticle()
	{
		ItemStack itemstack = this.getItemRaw();
		return (ParticleOptions) (itemstack.isEmpty() ? ParticleTypes.LAVA
				: new ItemParticleOption(ParticleTypes.ITEM, itemstack));
	}


	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte id)
	{
		if (id == 3)
		{
			ParticleOptions iparticledata = this.makeParticle();

			for (int i = 0; i < 8; ++i)
			{
				this.level().addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
}