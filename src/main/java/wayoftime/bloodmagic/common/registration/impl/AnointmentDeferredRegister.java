package wayoftime.bloodmagic.common.registration.impl;

import java.util.function.Supplier;

import wayoftime.bloodmagic.anointment.Anointment;
import wayoftime.bloodmagic.common.registration.WrappedForgeDeferredRegister;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

public class AnointmentDeferredRegister extends WrappedForgeDeferredRegister<Anointment>
{
	public AnointmentDeferredRegister(String modid)
	{
		super(modid, BloodMagicAPI.anointmentRegistryName());
	}

	public <AN extends Anointment> AnointmentRegistryObject<AN> register(String name, Supplier<? extends AN> sup)
	{
		return register(name, sup, AnointmentRegistryObject::new);
	}
}