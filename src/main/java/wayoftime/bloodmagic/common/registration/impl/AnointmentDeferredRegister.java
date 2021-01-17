package wayoftime.bloodmagic.common.registration.impl;

import java.util.function.Supplier;

import wayoftime.bloodmagic.anointment.Anointment;
import wayoftime.bloodmagic.common.registration.WrappedDeferredRegister;

public class AnointmentDeferredRegister extends WrappedDeferredRegister<Anointment>
{
	public AnointmentDeferredRegister(String modid)
	{
		super(modid, Anointment.class);
	}

	public <AN extends Anointment> AnointmentRegistryObject<AN> register(String name, Supplier<? extends AN> sup)
	{
		return register(name, sup, AnointmentRegistryObject::new);
	}
}