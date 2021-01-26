package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;

public class EntityThrowingDagger extends AbstractEntityThrowingDagger
{
	public EntityThrowingDagger(EntityType<EntityThrowingDagger> type, World world)
	{
		super(type, world);
	}

	public EntityThrowingDagger(ItemStack stack, World worldIn, LivingEntity throwerIn)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER.get(), stack, worldIn, throwerIn);

	}

	public EntityThrowingDagger(ItemStack stack, World worldIn, double x, double y, double z)
	{
		super(BloodMagicEntityTypes.THROWING_DAGGER.get(), stack, worldIn, x, y, z);
	}
}
