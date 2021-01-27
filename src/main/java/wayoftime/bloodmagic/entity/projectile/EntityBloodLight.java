package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;

public class EntityBloodLight extends ProjectileItemEntity
{
	public EntityBloodLight(EntityType<EntityBloodLight> p_i50159_1_, World p_i50159_2_)
	{
		super(p_i50159_1_, p_i50159_2_);
	}

	public EntityBloodLight(World worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.BLOOD_LIGHT.getEntityType(), throwerIn, worldIn);
	}

	public EntityBloodLight(World worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.BLOOD_LIGHT.getEntityType(), x, y, z, worldIn);
	}

	protected Item getDefaultItem()
	{
		return BloodMagicItems.REAGENT_BLOOD_LIGHT.get();
	}

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick()
	{
		super.tick();
		RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
//		boolean flag = false;
		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK)
		{
			BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getPos().offset(((BlockRayTraceResult) raytraceresult).getFace());
			BlockState blockstate = this.world.getBlockState(blockpos);
			Material material = blockstate.getMaterial();
			if (blockstate.isAir() || blockstate.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable())
			{
				this.getEntityWorld().setBlockState(blockpos, BloodMagicBlocks.BLOOD_LIGHT.get().getDefaultState());
				this.setDead();
			}
		}
	}

	protected float getGravityVelocity()
	{
		return 0;
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