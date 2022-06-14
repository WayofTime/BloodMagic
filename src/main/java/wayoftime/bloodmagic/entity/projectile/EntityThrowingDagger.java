package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;

public class EntityThrowingDagger extends AbstractEntityThrowingDagger
{
	public EntityThrowingDagger(EntityType<EntityThrowingDagger> type, Level world)
	{
		super(type, world);
	}

	public EntityThrowingDagger(ItemStack stack, Level worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER.get(), stack, worldIn, throwerIn);

	}

	public EntityThrowingDagger(ItemStack stack, Level worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER.get(), stack, worldIn, x, y, z);
	}
}
