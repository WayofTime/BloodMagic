package wayoftime.bloodmagic.common.registration.impl;

import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.common.registration.WrappedRegistryObject;
import wayoftime.bloodmagic.core.living.LivingUpgrade;

public class LivingUpgradeRegistryObject<UP extends LivingUpgrade> extends WrappedRegistryObject<UP>
{
	public LivingUpgradeRegistryObject(RegistryObject<UP> registryObject)
	{
		super(registryObject);
	}
}
