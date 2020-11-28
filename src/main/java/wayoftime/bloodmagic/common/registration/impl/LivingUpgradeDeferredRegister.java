package wayoftime.bloodmagic.common.registration.impl;

import java.util.function.Supplier;

import wayoftime.bloodmagic.common.registration.WrappedDeferredRegister;
import wayoftime.bloodmagic.core.living.LivingUpgrade;

public class LivingUpgradeDeferredRegister extends WrappedDeferredRegister<LivingUpgrade>
{
	public LivingUpgradeDeferredRegister(String modid)
	{
		super(modid, LivingUpgrade.class);
	}

//	public BloodOrbRegistryObject<BloodOrb> register(String name, ResourceLocation rl, int tier, int capacity,
//			int fillRate)
//	{
//		return register(name, () -> new BloodOrb(rl, tier, capacity, fillRate));
//	}

	public <ORB extends LivingUpgrade> LivingUpgradeRegistryObject<ORB> register(String name, Supplier<? extends ORB> sup)
	{
		return register(name, sup, LivingUpgradeRegistryObject::new);
	}
}
