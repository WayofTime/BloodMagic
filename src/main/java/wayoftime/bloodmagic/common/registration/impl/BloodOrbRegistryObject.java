package wayoftime.bloodmagic.common.registration.impl;

import net.minecraftforge.fml.RegistryObject;
import wayoftime.bloodmagic.common.registration.WrappedRegistryObject;
import wayoftime.bloodmagic.orb.BloodOrb;

public class BloodOrbRegistryObject<ORB extends BloodOrb> extends WrappedRegistryObject<ORB>
{
	public BloodOrbRegistryObject(RegistryObject<ORB> registryObject)
	{
		super(registryObject);
	}
}
