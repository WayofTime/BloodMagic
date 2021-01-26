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
		super(BloodMagicEntityTypes.THROWING_DAGGER.get(), stack, worldIn, throwerIn);

	}

	public EntityThrowingDaggerSyringe(ItemStack stack, World worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER.get(), stack, worldIn, x, y, z);
	}

	@Override
	protected void onEntityHit(EntityRayTraceResult p_213868_1_)
	{
		super.onEntityHit(p_213868_1_);
		Entity entity = p_213868_1_.getEntity();

		if (entity instanceof LivingEntity)
		{
			double maxHealth = ((LivingEntity) entity).getMaxHealth();
			if (!entity.isAlive())
			{
				int count = (int) (maxHealth / 20D) + (world.rand.nextDouble() < ((maxHealth % 20D) / 20D) ? 1 : 0);
				if (count > 0)
					InventoryHelper.spawnItemStack(world, this.getPosX(), this.getPosY(), this.getPosZ(), new ItemStack(BloodMagicItems.SLATE_AMPOULE.get(), count));
			}
		}
	}
}
