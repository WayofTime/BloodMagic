package wayoftime.bloodmagic;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import wayoftime.bloodmagic.client.hud.ElementRegistry;
import wayoftime.bloodmagic.impl.BloodMagicAPI;

@EventBusSubscriber(modid = BloodMagic.MODID, bus = Bus.MOD)
public class ConfigManager
{
	private static final Logger LOGGER = LogManager.getLogger();

	public static final CommonConfig COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	static
	{
		final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static class CommonConfig
	{
		public final ConfigValue<List<? extends String>> wellOfSuffering;
		public final ForgeConfigSpec.IntValue sacrificialDaggerConversion;
		public final ConfigValue<List<? extends String>> sacrificialValues;
		public final ForgeConfigSpec.BooleanValue makeDungeonRitualCreativeOnly;

		CommonConfig(ForgeConfigSpec.Builder builder)
		{
			builder.comment("Stops the listed entities from being used in the Well of Suffering.", "Use the registry name of the entity. Vanilla entities do not require the modid.").push("Blacklist");
			wellOfSuffering = builder.defineList("wellOfSuffering", ImmutableList.of(), obj -> true);

			builder.pop();

			builder.comment("Amount of LP the Sacrificial Dagger should provide for each damage dealt.").push("Config Values");
			sacrificialDaggerConversion = builder.defineInRange("sacrificialDaggerConversion", 100, 0, 10000);
//			builder.pop();

			builder.comment("Declares the amount of LP gained per HP sacrificed for the given entity.", "Setting the value to 0 will blacklist it.", "Use the registry name of the entity followed by a ';' and then the value you want.", "Vanilla entities do not require the modid.");
			sacrificialValues = builder.defineList("sacrificialValues", ImmutableList.of("villager;100", "slime;15", "enderman;10", "cow;100", "chicken;100", "horse;100", "sheep;100", "wolf;100", "ocelot;100", "pig;100", "rabbit;100"), obj -> true);

			builder.comment("State that the dungeon spawning ritual can only be activated when using a Creative Activation Crystal.", "Used on servers for if you do not trust your players to not destroy other people's bases.");
			makeDungeonRitualCreativeOnly = builder.define("makeDungeonRitualCreativeOnly", false);

			builder.pop();
		}
	}

	@SubscribeEvent
	public static void onCommonReload(ModConfigEvent ev)
	{
		if (ev.getConfig().getSpec().equals(COMMON_SPEC))
		{
			BloodMagic.handleConfigValues(BloodMagicAPI.INSTANCE);
		}
	}

	public static final ClientConfig CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;

	static
	{
		final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static class ClientConfig
	{
//		public final ForgeConfigSpec.DoubleValue demonWillGaugeX;
//		public final ForgeConfigSpec.DoubleValue demonWillGaugeY;
//		public final ForgeConfigSpec.DoubleValue bloodAltarGaugeX;
//		public final ForgeConfigSpec.DoubleValue bloodAltarGaugeY;
//		public final ForgeConfigSpec.DoubleValue bloodAltarAdvGaugeX;
//		public final ForgeConfigSpec.DoubleValue bloodAltarAdvGaugeY;
//		public final ForgeConfigSpec.DoubleValue incenseGaugeX;
//		public final ForgeConfigSpec.DoubleValue incenseGaugeY;
//		public final ForgeConfigSpec.DoubleValue holdingX;
//		public final ForgeConfigSpec.DoubleValue holdingY;

		public final ForgeConfigSpec.BooleanValue alwaysRenderRoutingLines;
		public final ForgeConfigSpec.BooleanValue sigilHoldingSkipsEmptySlots;

		ClientConfig(ForgeConfigSpec.Builder builder)
		{
			builder.comment("Always render the beams between routing nodes.", "If disabled, the beams will only render while the Node Router is held.").push("client");
			alwaysRenderRoutingLines = builder.define("alwaysRenderRoutingLines", false);

			builder.comment("When cycling through slots, the Sigil of Holding will skip over empty slots and move to the next occupied one.", "If disabled, it will behave identically to the default hotbar.");
			sigilHoldingSkipsEmptySlots = builder.define("sigilHoldingSkipsEmptySlots", false);
			builder.pop();

//			builder.comment("Settings for the position of the Demon Will Gauge HUD element.").push("hud");
//			demonWillGaugeX = builder.defineInRange("DemonWillGaugePosX", 0.01, 0, 1);
//			demonWillGaugeY = builder.defineInRange("DemonWillGaugePosY", 0.01, 0, 1);
//
//			builder.comment("Settings for the position of the basic Blood Altar info HUD element.");
//			bloodAltarGaugeX = builder.defineInRange("bloodAltarGaugeX", 0.01, 0, 1);
//			bloodAltarGaugeY = builder.defineInRange("bloodAltarGaugeY", 0.01, 0, 1);
//
//			builder.comment("Settings for the position of the advanced Blood Altar info HUD element.");
//			bloodAltarAdvGaugeX = builder.defineInRange("bloodAltarAdvGaugeX", 0.01, 0, 1);
//			bloodAltarAdvGaugeY = builder.defineInRange("bloodAltarAdvGaugeY", 0.01, 0, 1);
//
//			builder.comment("Settings for the position of the Incense Altar info HUD element.");
//			incenseGaugeX = builder.defineInRange("incenseGaugeX", 0.01, 0, 1);
//			incenseGaugeY = builder.defineInRange("incenseGaugeY", 0.01, 0, 1);
//
//			builder.comment("Settings for the position of the Sigil of Holding info HUD element.");
//			holdingX = builder.defineInRange("holdingX", 0.72f, 0, 1);
//			holdingY = builder.defineInRange("holdingY", 1f, 0, 1);

//			builder.pop();
		}

	}

	@SubscribeEvent
	public static void onClientReload(ModConfigEvent ev)
	{
		if (ev.getConfig().getSpec().equals(CLIENT_SPEC))
		{
			System.out.println("Reloading...?");
			ElementRegistry.readConfig();
		}
	}
}
