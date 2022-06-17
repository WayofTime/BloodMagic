package wayoftime.bloodmagic.common.registration.impl;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.common.registration.WrappedDeferredRegister;

public class EntityTypeDeferredRegister extends WrappedDeferredRegister<EntityType<?>>
{
	public EntityTypeDeferredRegister(String modid)
	{
		super(modid, ForgeRegistries.Keys.ENTITY_TYPES);
	}

	public <ENTITY extends Entity> EntityTypeRegistryObject<ENTITY> register(String name, EntityType.Builder<ENTITY> builder)
	{
		return register(name, () -> builder.build(name), EntityTypeRegistryObject::new);
	}
}