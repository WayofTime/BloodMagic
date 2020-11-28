package wayoftime.bloodmagic.core;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.registration.impl.LivingUpgradeDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.LivingUpgradeRegistryObject;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.gson.Serializers;

public class LivingArmorRegistrar
{

	public static final LivingUpgradeDeferredRegister UPGRADES = new LivingUpgradeDeferredRegister(BloodMagic.MODID);

	public static final Map<ResourceLocation, LivingUpgrade> UPGRADE_MAP = new HashMap<>();

//	public static final DefaultedRegistry<LivingUpgrade> UPGRADES = (DefaultedRegistry<LivingUpgrade>) createRegistry("livingarmor:upgrades", LivingUpgrade.DUMMY.getKey().toString(), () -> LivingUpgrade.DUMMY);
//	private static final Map<String, ResourceLocation> DEFINITIONS = ((Supplier<Map<String, ResourceLocation>>) () -> {
//		Map<String, ResourceLocation> def = new HashMap<>();
//		def.put("arrow_protect", Paths.get(MinecraftForge.getInstance().getConfigDirectory().getAbsolutePath(), "livingarmor", "arrow_protect.json"));
//		def.put("arrow_shot", Paths.get(FabricLoader.getInstance().getConfigDirectory().getAbsolutePath(), "livingarmor", "arrow_shot.json"));
//		def.put("critical_strike", Paths.get(FabricLoader.getInstance().getConfigDirectory().getAbsolutePath(), "livingarmor", "critical_strike.json"));
//		def.put("jump", Paths.get(FabricLoader.getInstance().getConfigDirectory().getAbsolutePath(), "livingarmor", "jump.json"));
//		return def;
//	}).get();
	private static final Map<String, ResourceLocation> DEFINITIONS = ((Supplier<Map<String, ResourceLocation>>) () -> {
		Map<String, ResourceLocation> def = new HashMap<>();
		def.put("arrow_protect", BloodMagic.rl("arrow_protect"));
		def.put("arrow_shot", BloodMagic.rl("arrow_shot"));
		def.put("critical_strike", BloodMagic.rl("critical_strike"));
		def.put("jump", BloodMagic.rl("jump"));
		def.put("health", BloodMagic.rl("health"));
		def.put("experience", BloodMagic.rl("experienced"));
		def.put("sprint_attack", BloodMagic.rl("sprint_attack"));
		def.put("self_sacrifice", BloodMagic.rl("self_sacrifice"));
		def.put("speed", BloodMagic.rl("speed"));
		return def;
	}).get();
	// private static final Map<String, Path> DEFINITIONS =
	// ResourceUtil.gatherResources("/data", "living_armor", p ->
	// FilenameUtils.getExtension(p.toFile().getName()).equals("json"))
//            .stream()
//            .collect(Collectors.toMap(key -> FilenameUtils.getBaseName(key.toFile().getName()), value -> value));
	private static final Gson GSON = new GsonBuilder().serializeNulls().create();

//	public static final ItemLivingArmor LIVING_HELMET = new ItemLivingArmor(EquipmentSlotType.HEAD);
//	public static final ItemLivingArmor LIVING_CHESTPLATE = new ItemLivingArmor(EquipmentSlotType.CHEST);
//	public static final ItemLivingArmor LIVING_LEGGINGS = new ItemLivingArmor(EquipmentSlotType.LEGS);
//	public static final ItemLivingArmor LIVING_BOOTS = new ItemLivingArmor(EquipmentSlotType.FEET);
//	public static final ItemLivingTrainer TRAINER = new ItemLivingTrainer();
//	public static final ItemLivingTome TOME = new ItemLivingTome();

	public static final LivingUpgradeRegistryObject<LivingUpgrade> UPGRADE_ARROW_PROTECT = UPGRADES.register("arrow_protect", () -> parseDefinition("arrow_protect").withArmorProvider((player, stats, source, upgrade, level) -> {
		if (source.isProjectile())
		{
			return upgrade.getBonusValue("protection", level).doubleValue();
		}
		return 0;
	}));

	public static final LivingUpgradeRegistryObject<LivingUpgrade> UPGRADE_HEALTH = UPGRADES.register("health", () -> parseDefinition("health").withAttributeProvider((stats, attributeMap, uuid, upgrade, level) -> {
		attributeMap.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Health Modifier", upgrade.getBonusValue("hp", level).intValue(), AttributeModifier.Operation.ADDITION));
	}));

	public static final LivingUpgradeRegistryObject<LivingUpgrade> UPGRADE_EXPERIENCE = UPGRADES.register("experienced", () -> parseDefinition("experienced"));
	public static final LivingUpgradeRegistryObject<LivingUpgrade> UPGRADE_SPRINT_ATTACK = UPGRADES.register("sprint_attack", () -> parseDefinition("sprint_attack").withDamageProvider((player, weapon, damage, stats, attackedEntity, upgrade, level) -> {
		if (player.isSprinting())
		{
			return damage * upgrade.getBonusValue("damage_boost", level).doubleValue();
		}
		return 0;
	}));
	public static final LivingUpgradeRegistryObject<LivingUpgrade> UPGRADE_SELF_SACRIFICE = UPGRADES.register("self_sacrifice", () -> parseDefinition("self_sacrifice"));
	public static final LivingUpgradeRegistryObject<LivingUpgrade> UPGRADE_SPEED = UPGRADES.register("speed", () -> parseDefinition("speed"));

//	public static final LivingUpgrade UPGRADE_ARROW_PROTECT = parseDefinition("arrow_protect").withArmorProvider((player, stats, source, upgrade, level) -> {
//		if (source.isProjectile())
//		{
//			return upgrade.getBonusValue("protection", level).doubleValue();
//		}
//		return 0;
//	});
//	public static final LivingUpgrade UPGRADE_ARROW_SHOT = parseDefinition("arrow_shot");
//	public static final LivingUpgrade UPGRADE_CRITICAL_STRIKE = parseDefinition("critical_strike").withAttributeProvider((stats, attributeMap, uuid, upgrade, level) -> {
//		attributeMap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Weapon modifier", upgrade.getBonusValue("damage_boost", level).doubleValue(), AttributeModifier.Operation.ADDITION));
////        attributeMap.put(EntityAttributes.ATTACK_DAMAGE.getId(), AttributeModifiers.create(upgrade, "damage_boost", upgrade.getBonusValue("damage_boost", level).doubleValue(), EntityAttributeModifier.Operation.ADDITION));
////        attributeMap.put(EntityAttributes.ATTACK_DAMAGE.getId(), AttributeModifiers.create(upgrade, "damage_boost", level, EntityAttributeModifier.Operation.ADDITION));
//	});
//	public static final LivingUpgrade UPGRADE_JUMP = parseDefinition("jump");

	public static void register()
	{
		registerUpgrade(UPGRADE_ARROW_PROTECT.get());
		registerUpgrade(UPGRADE_HEALTH.get());
		registerUpgrade(UPGRADE_EXPERIENCE.get());
		registerUpgrade(UPGRADE_SPRINT_ATTACK.get());
		registerUpgrade(UPGRADE_SELF_SACRIFICE.get());
		registerUpgrade(UPGRADE_SPEED.get());
//		Registry.register(UPGRADES, UPGRADE_ARROW_PROTECT.getKey(), UPGRADE_ARROW_PROTECT);
//		Registry.register(UPGRADES, UPGRADE_ARROW_SHOT.getKey(), UPGRADE_ARROW_SHOT);
//		Registry.register(UPGRADES, UPGRADE_CRITICAL_STRIKE.getKey(), UPGRADE_CRITICAL_STRIKE);
//		Registry.register(UPGRADES, UPGRADE_JUMP.getKey(), UPGRADE_JUMP);

//        Registry.register(Registry.ITEM, new ResourceLocation("livingarmor", "living_helmet"), LIVING_HELMET);
//        Registry.register(Registry.ITEM, new Identifier("livingarmor", "living_chestplate"), LIVING_CHESTPLATE);
//        Registry.register(Registry.ITEM, new Identifier("livingarmor", "living_leggings"), LIVING_LEGGINGS);
//        Registry.register(Registry.ITEM, new Identifier("livingarmor", "living_boots"), LIVING_BOOTS);
//        Registry.register(Registry.ITEM, new Identifier("livingarmor", "trainer"), TRAINER);
//        Registry.register(Registry.ITEM, new Identifier("livingarmor", "tome"), TOME);
	}

	public static void registerUpgrade(LivingUpgrade upgrade)
	{
		UPGRADE_MAP.put(upgrade.getKey(), upgrade);
	}

	public static LivingUpgrade parseDefinition(String fileName)
	{
		ResourceLocation path = DEFINITIONS.get(fileName);
		if (path == null)
			return LivingUpgrade.DUMMY;

		try
		{
			URL schematicURL = LivingUpgrade.class.getResource(resLocToResourcePath(path));
			System.out.println("Attempting to load Living Armour Upgrade: " + schematicURL + ", path: " + resLocToResourcePath(path));
			return Serializers.GSON.fromJson(Resources.toString(schematicURL, Charsets.UTF_8), LivingUpgrade.class);
//			return GSON.fromJson(IOUtils.toString(path.toUri(), StandardCharsets.UTF_8), LivingUpgrade.class);
		} catch (Exception e)
		{
			e.printStackTrace();
			return LivingUpgrade.DUMMY;
		}
//		Path path = DEFINITIONS.get(fileName);
//		if (path == null)
//			return LivingUpgrade.DUMMY;
//
//		try
//		{
//			return GSON.fromJson(IOUtils.toString(path.toUri(), StandardCharsets.UTF_8), LivingUpgrade.class);
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//			return LivingUpgrade.DUMMY;
//		}
	}

	public static String resLocToResourcePath(ResourceLocation resourceLocation)
	{
		return "/data/" + resourceLocation.getNamespace() + "/living_armor/" + resourceLocation.getPath() + ".json";
	}

//	private static <T> Registry<T> createRegistry(String registryId, String defaultId, Supplier<T> defaultProvider)
//	{
//		try
//		{
//			Method _createRegistry = Registry.class.getDeclaredMethod("create", String.class, String.class, Supplier.class); // FIXME
//																																// yarn
//																																// name
//			_createRegistry.setAccessible(true);
//			return (Registry<T>) _createRegistry.invoke(null, registryId, defaultId, defaultProvider);
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//			MutableRegistry<T> registry = new DefaultedRegistry(defaultId, null, null);
//			registry.add(new ResourceLocation(defaultId), defaultProvider.get());
//			return registry;
//		}
//	}
}
