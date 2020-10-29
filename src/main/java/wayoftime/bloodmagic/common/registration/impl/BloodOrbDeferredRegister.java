package wayoftime.bloodmagic.common.registration.impl;

import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.common.registration.WrappedDeferredRegister;
import wayoftime.bloodmagic.orb.BloodOrb;

public class BloodOrbDeferredRegister extends WrappedDeferredRegister<BloodOrb>
{
	public BloodOrbDeferredRegister(String modid)
	{
		super(modid, BloodOrb.class);
	}

	public BloodOrbRegistryObject<BloodOrb> register(String name, ResourceLocation rl, int tier, int capacity,
			int fillRate)
	{
		return register(name, () -> new BloodOrb(rl, tier, capacity, fillRate));
	}

	public <ORB extends BloodOrb> BloodOrbRegistryObject<ORB> register(String name, Supplier<? extends ORB> sup)
	{
		return register(name, sup, BloodOrbRegistryObject::new);
	}
}
