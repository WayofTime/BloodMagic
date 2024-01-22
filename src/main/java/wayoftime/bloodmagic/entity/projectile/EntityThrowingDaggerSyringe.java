package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;

public class EntityThrowingDaggerSyringe extends AbstractEntityThrowingDagger
{
	public EntityThrowingDaggerSyringe(EntityType<EntityThrowingDaggerSyringe> type, Level world)
	{
		super(type, world);
	}

	public EntityThrowingDaggerSyringe(ItemStack stack, Level worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER_SYRINGE.get(), stack, worldIn, throwerIn);

	}

	public EntityThrowingDaggerSyringe(ItemStack stack, Level worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER_SYRINGE.get(), stack, worldIn, x, y, z);
	}

	@Override
	protected void onHitEntity(EntityHitResult p_213868_1_)
	{
		super.onHitEntity(p_213868_1_);
		Entity entity = p_213868_1_.getEntity();

		if (entity instanceof LivingEntity)
		{
			double maxHealth = ((LivingEntity) entity).getMaxHealth();
			if (!entity.isAlive())
			{
				int count = (int) (maxHealth / 20D) + (level().random.nextDouble() < ((maxHealth % 20D) / 20D) ? 1 : 0);
				if (count > 0)
					Containers.dropItemStack(level(), this.getX(), this.getY(), this.getZ(), new ItemStack(BloodMagicItems.SLATE_AMPOULE.get(), count));
			}
		}
	}
}
