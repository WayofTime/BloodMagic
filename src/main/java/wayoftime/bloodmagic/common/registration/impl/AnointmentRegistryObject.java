package wayoftime.bloodmagic.common.registration.impl;

import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.anointment.Anointment;
import wayoftime.bloodmagic.common.registration.WrappedRegistryObject;

public class AnointmentRegistryObject<AN extends Anointment> extends WrappedRegistryObject<AN>
{
	public AnointmentRegistryObject(RegistryObject<AN> registryObject)
	{
		super(registryObject);
	}
}
