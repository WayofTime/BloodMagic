package wayoftime.bloodmagic.common.registries;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.registration.impl.EntityTypeDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.EntityTypeRegistryObject;
import wayoftime.bloodmagic.entity.projectile.EntityBloodLight;
import wayoftime.bloodmagic.entity.projectile.EntityMeteor;
import wayoftime.bloodmagic.entity.projectile.EntityPotionFlask;
import wayoftime.bloodmagic.entity.projectile.EntityShapedCharge;
import wayoftime.bloodmagic.entity.projectile.EntitySoulSnare;
import wayoftime.bloodmagic.entity.projectile.EntityThrowingDagger;
import wayoftime.bloodmagic.entity.projectile.EntityThrowingDaggerSyringe;

public class BloodMagicEntityTypes
{
	private BloodMagicEntityTypes()
	{

	}

	public static final EntityTypeDeferredRegister ENTITY_TYPES = new EntityTypeDeferredRegister(BloodMagic.MODID);

	public static final EntityTypeRegistryObject<EntitySoulSnare> SNARE = ENTITY_TYPES.register("soulsnare", EntityType.Builder.<EntitySoulSnare>of(EntitySoulSnare::new, MobCategory.MISC).setTrackingRange(64).setUpdateInterval(1).sized(0.25f, 0.25f));
	public static final EntityTypeRegistryObject<EntityThrowingDagger> THROWING_DAGGER = ENTITY_TYPES.register("throwing_dagger", EntityType.Builder.<EntityThrowingDagger>of(EntityThrowingDagger::new, MobCategory.MISC).setTrackingRange(64).setUpdateInterval(1).sized(0.25f, 0.25f));
	public static final EntityTypeRegistryObject<EntityThrowingDaggerSyringe> THROWING_DAGGER_SYRINGE = ENTITY_TYPES.register("throwing_dagger_syringe", EntityType.Builder.<EntityThrowingDaggerSyringe>of(EntityThrowingDaggerSyringe::new, MobCategory.MISC).setTrackingRange(64).setUpdateInterval(1).sized(0.25f, 0.25f));
	public static final EntityTypeRegistryObject<EntityBloodLight> BLOOD_LIGHT = ENTITY_TYPES.register("bloodlight", EntityType.Builder.<EntityBloodLight>of(EntityBloodLight::new, MobCategory.MISC).setTrackingRange(64).setUpdateInterval(1).sized(0.25f, 0.25f));
	public static final EntityTypeRegistryObject<EntityShapedCharge> SHAPED_CHARGE = ENTITY_TYPES.register("shapedcharge", EntityType.Builder.<EntityShapedCharge>of(EntityShapedCharge::new, MobCategory.MISC).setTrackingRange(64).setUpdateInterval(1).sized(0.4f, 0.4f));
	public static final EntityTypeRegistryObject<EntityMeteor> METEOR = ENTITY_TYPES.register("meteor", EntityType.Builder.<EntityMeteor>of(EntityMeteor::new, MobCategory.MISC).setTrackingRange(64).setUpdateInterval(1).sized(1, 1));

	public static final EntityTypeRegistryObject<EntityPotionFlask> FLASK = ENTITY_TYPES.register("potionflask", EntityType.Builder.<EntityPotionFlask>of(EntityPotionFlask::new, MobCategory.MISC).setTrackingRange(64).setUpdateInterval(1).sized(1, 1));

}
