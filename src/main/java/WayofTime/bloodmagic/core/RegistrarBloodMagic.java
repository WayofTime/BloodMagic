package wayoftime.bloodmagic.core;

import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class RegistrarBloodMagic
{
	private RegistrarBloodMagic()
	{

	}

//	public static final BloodOrbDeferredRegister BLOOD_ORBS = new BloodOrbDeferredRegister(BloodMagic.MODID);
//
//	public static final BloodOrbRegistryObject<BloodOrb> ORB_WEAK = BLOOD_ORBS.register("weakbloodorb", () -> new BloodOrb(new ResourceLocation(BloodMagic.MODID, "weakbloodorb"), 0, 5000, 10));

//	public static IForgeRegistry<BloodOrb> BLOOD_ORBS = new RegistryBuilder<BloodOrb>().setName(new ResourceLocation(BloodMagic.MODID, "blood_orb")).setIDRange(0, Short.MAX_VALUE).setType(BloodOrb.class).addCallback((IForgeRegistry.AddCallback<BloodOrb>) (
//			owner, stage, id, obj,
//			oldObj) -> OrbRegistry.tierMap.put(obj.getTier(), OrbRegistry.getOrbStack(obj))).create();

//	@SubscribeEvent
//	public static void onRegistryCreation(RegistryEvent.NewRegistry event)
//	{
//		System.out.println("Testification3");
//		BLOOD_ORBS = new RegistryBuilder<BloodOrb>().setName(new ResourceLocation(BloodMagic.MODID, "blood_orb")).setIDRange(0, Short.MAX_VALUE).setType(BloodOrb.class).addCallback((IForgeRegistry.AddCallback<BloodOrb>) (
//				owner, stage, id, obj,
//				oldObj) -> OrbRegistry.tierMap.put(obj.getTier(), OrbRegistry.getOrbStack(obj))).create();
//	}
}
