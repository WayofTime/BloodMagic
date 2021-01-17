package wayoftime.bloodmagic.common.registries;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.registration.impl.EntityTypeDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.EntityTypeRegistryObject;
import wayoftime.bloodmagic.entity.projectile.EntityBloodLight;
import wayoftime.bloodmagic.entity.projectile.EntityShapedCharge;
import wayoftime.bloodmagic.entity.projectile.EntitySoulSnare;

public class BloodMagicEntityTypes
{
	private BloodMagicEntityTypes()
	{

	}

	public static final EntityTypeDeferredRegister ENTITY_TYPES = new EntityTypeDeferredRegister(BloodMagic.MODID);

	public static final EntityTypeRegistryObject<EntitySoulSnare> SNARE = ENTITY_TYPES.register("soulsnare", EntityType.Builder.<EntitySoulSnare>create(EntitySoulSnare::new, EntityClassification.MISC).setTrackingRange(64).setUpdateInterval(1).size(0.25f, 0.25f));
	public static final EntityTypeRegistryObject<EntityBloodLight> BLOOD_LIGHT = ENTITY_TYPES.register("bloodlight", EntityType.Builder.<EntityBloodLight>create(EntityBloodLight::new, EntityClassification.MISC).setTrackingRange(64).setUpdateInterval(1).size(0.25f, 0.25f));
	public static final EntityTypeRegistryObject<EntityShapedCharge> SHAPED_CHARGE = ENTITY_TYPES.register("shapedcharge", EntityType.Builder.<EntityShapedCharge>create(EntityShapedCharge::new, EntityClassification.MISC).setTrackingRange(64).setUpdateInterval(1).size(0.4f, 0.4f));

}
