package wayoftime.bloodmagic.core;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import net.minecraft.resources.ResourceLocation;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.anointment.Anointment;
import wayoftime.bloodmagic.common.registration.impl.AnointmentDeferredRegister;
import wayoftime.bloodmagic.common.registration.impl.AnointmentRegistryObject;
import wayoftime.bloodmagic.gson.Serializers;

public class AnointmentRegistrar
{
	public static final AnointmentDeferredRegister ANOINTMENTS = new AnointmentDeferredRegister(BloodMagic.MODID);

	public static final Map<ResourceLocation, Anointment> ANOINTMENT_MAP = new HashMap<>();

	private static final Map<String, ResourceLocation> DEFINITIONS = ((Supplier<Map<String, ResourceLocation>>) () -> {
		Map<String, ResourceLocation> def = new HashMap<>();
		def.put("melee_damage", BloodMagic.rl("melee_damage"));
		def.put("holy_water", BloodMagic.rl("holy_water"));
		def.put("hidden_knowledge", BloodMagic.rl("hidden_knowledge"));
		def.put("quick_draw", BloodMagic.rl("quick_draw"));
		def.put("bow_power", BloodMagic.rl("bow_power"));
		def.put("bow_velocity", BloodMagic.rl("bow_velocity"));
		def.put("repairing", BloodMagic.rl("repairing"));
//		def.put("arrow_shot", BloodMagic.rl("arrow_shot"));
//		def.put("critical_strike", BloodMagic.rl("critical_strike"));
//		def.put("digging", BloodMagic.rl("digging"));
//		def.put("experienced", BloodMagic.rl("experienced"));
//		def.put("fall_protect", BloodMagic.rl("fall_protect"));
//		def.put("fire_resist", BloodMagic.rl("fire_resist"));
//		def.put("grave_digger", BloodMagic.rl("grave_digger"));
//		def.put("health", BloodMagic.rl("health"));
//		def.put("jump", BloodMagic.rl("jump"));
//		def.put("knockback_resist", BloodMagic.rl("knockback_resist"));
//		def.put("melee_damage", BloodMagic.rl("melee_damage"));
//		def.put("physical_protect", BloodMagic.rl("physical_protect"));
//		def.put("poison_resist", BloodMagic.rl("poison_resist"));
//		def.put("sprint_attack", BloodMagic.rl("sprint_attack"));
//		def.put("speed", BloodMagic.rl("speed"));
//		def.put("self_sacrifice", BloodMagic.rl("self_sacrifice"));
		return def;
	}).get();

//	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_MELEE_DAMAGE = ANOINTMENTS.register("melee_damage", () -> parseDefinition("melee_damage").withAttributeProvider((stats, attributeMap, uuid, upgrade, level) -> {
//		attributeMap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Weapon modifier", upgrade.getBonusValue("damage", level).intValue(), AttributeModifier.Operation.ADDITION));
//	}).setConsumeOnAttack());

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_MELEE_DAMAGE = ANOINTMENTS.register("melee_damage", () -> parseDefinition("melee_damage").withDamageProvider((player, weapon, damage, holder, attacked, anoint, level) -> {
		return anoint.getBonusValue("damage", level).doubleValue();
	}).setConsumeOnAttack().addIncompatibility(BloodMagic.rl("holy_water")));

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_SILK_TOUCH = ANOINTMENTS.register("silk_touch", () -> new Anointment(BloodMagic.rl("silk_touch")).setConsumeOnHarvest().addIncompatibility(BloodMagic.rl("fortune")));

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_FORTUNE = ANOINTMENTS.register("fortune", () -> new Anointment(BloodMagic.rl("fortune")).setConsumeOnHarvest().addIncompatibility(BloodMagic.rl("silk_touch")));

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_HOLY_WATER = ANOINTMENTS.register("holy_water", () -> parseDefinition("holy_water").withDamageProvider((player, weapon, damage, holder, attacked, anoint, level) -> {
		if (attacked.isInvertedHealAndHarm())
			return anoint.getBonusValue("damage", level).doubleValue();
		else
			return 0;
	}).setConsumeOnAttack().addIncompatibility(BloodMagic.rl("melee_damage")));

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_HIDDEN_KNOWLEDGE = ANOINTMENTS.register("hidden_knowledge", () -> parseDefinition("hidden_knowledge"));

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_QUICK_DRAW = ANOINTMENTS.register("quick_draw", () -> parseDefinition("quick_draw").setConsumeOnUseFinish());

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_LOOTING = ANOINTMENTS.register("looting", () -> new Anointment(BloodMagic.rl("looting")).setConsumeOnAttack());

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_BOW_POWER = ANOINTMENTS.register("bow_power", () -> parseDefinition("bow_power").setConsumeOnUseFinish());

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_WILL_POWER = ANOINTMENTS.register("will_power", () -> new Anointment(BloodMagic.rl("will_power")).setConsumeOnAttack());

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_SMELTING = ANOINTMENTS.register("smelting", () -> new Anointment(BloodMagic.rl("smelting")).setConsumeOnHarvest());

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_VOIDING = ANOINTMENTS.register("voiding", () -> new Anointment(BloodMagic.rl("voiding")).setConsumeOnHarvest());

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_BOW_VELOCITY = ANOINTMENTS.register("bow_velocity", () -> parseDefinition("bow_velocity").setConsumeOnUseFinish());

	public static final AnointmentRegistryObject<Anointment> ANOINTMENT_WEAPON_REPAIR = ANOINTMENTS.register("repairing", () -> parseDefinition("repairing").setConsumeOnUseFinish());

	public static void register()
	{
		registerAnointment(ANOINTMENT_MELEE_DAMAGE.get());
		registerAnointment(ANOINTMENT_SILK_TOUCH.get());
		registerAnointment(ANOINTMENT_FORTUNE.get());
		registerAnointment(ANOINTMENT_HOLY_WATER.get());
		registerAnointment(ANOINTMENT_HIDDEN_KNOWLEDGE.get());
		registerAnointment(ANOINTMENT_QUICK_DRAW.get());
		registerAnointment(ANOINTMENT_LOOTING.get());
		registerAnointment(ANOINTMENT_BOW_POWER.get());
		registerAnointment(ANOINTMENT_WILL_POWER.get());
		registerAnointment(ANOINTMENT_SMELTING.get());
		registerAnointment(ANOINTMENT_VOIDING.get());
		registerAnointment(ANOINTMENT_BOW_VELOCITY.get());
		registerAnointment(ANOINTMENT_WEAPON_REPAIR.get());

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

	public static void registerAnointment(Anointment anoint)
	{
		ANOINTMENT_MAP.put(anoint.getKey(), anoint);
	}

	public static Anointment parseDefinition(String fileName)
	{
//		System.out.println("Attempting to parse Anointment: " + fileName);
		ResourceLocation path = DEFINITIONS.get(fileName);
		if (path == null)
			return Anointment.DUMMY;

		try
		{
			URL schematicURL = Anointment.class.getResource(resLocToResourcePath(path));
			System.out.println("Attempting to load Anointment: " + schematicURL + ", path: " + resLocToResourcePath(path));
			return Serializers.GSON.fromJson(Resources.toString(schematicURL, Charsets.UTF_8), Anointment.class);
//			return GSON.fromJson(IOUtils.toString(path.toUri(), StandardCharsets.UTF_8), LivingUpgrade.class);
		} catch (Exception e)
		{
			e.printStackTrace();
			return Anointment.DUMMY;
		}
	}

	public static String resLocToResourcePath(ResourceLocation resourceLocation)
	{
		return "/data/" + resourceLocation.getNamespace() + "/anointment/" + resourceLocation.getPath() + ".json";
	}
}
