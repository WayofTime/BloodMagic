package wayoftime.bloodmagic.common.registration.impl;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import wayoftime.bloodmagic.util.providers.IEntityTypeProvider;
import wayoftime.bloodmagic.common.registration.WrappedRegistryObject;

public class EntityTypeRegistryObject<ENTITY extends Entity> extends WrappedRegistryObject<EntityType<ENTITY>>
		implements IEntityTypeProvider
{

	public EntityTypeRegistryObject(RegistryObject<EntityType<ENTITY>> registryObject)
	{
		super(registryObject);
	}

	@Nonnull
	@Override
	public EntityType<ENTITY> getEntityType()
	{
		return get();
	}
}
