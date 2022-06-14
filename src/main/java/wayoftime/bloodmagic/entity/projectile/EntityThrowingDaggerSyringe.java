package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;

public class EntityThrowingDaggerSyringe extends AbstractEntityThrowingDagger
{
	public EntityThrowingDaggerSyringe(EntityType<EntityThrowingDaggerSyringe> type, World world)
	{
		super(type, world);
	}

	public EntityThrowingDaggerSyringe(ItemStack stack, World worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER_SYRINGE.get(), stack, worldIn, throwerIn);

	}

	public EntityThrowingDaggerSyringe(ItemStack stack, World worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER_SYRINGE.get(), stack, worldIn, x, y, z);
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult p_213868_1_)
	{
		super.onHitEntity(p_213868_1_);
		Entity entity = p_213868_1_.getEntity();

		if (entity instanceof LivingEntity)
		{
			double maxHealth = ((LivingEntity) entity).getMaxHealth();
			if (!entity.isAlive())
			{
				int count = (int) (maxHealth / 20D) + (level.random.nextDouble() < ((maxHealth % 20D) / 20D) ? 1 : 0);
				if (count > 0)
					InventoryHelper.dropItemStack(level, this.getX(), this.getY(), this.getZ(), new ItemStack(BloodMagicItems.SLATE_AMPOULE.get(), count));
			}
		}
	}
}
