package wayoftime.bloodmagic.common.registration.impl;

import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.registration.WrappedRegistryObject;

public class BloodOrbRegistryObject<ORB extends BloodOrb> extends WrappedRegistryObject<ORB>
{
	public BloodOrbRegistryObject(RegistryObject<ORB> registryObject)
	{
		super(registryObject);
	}
}
