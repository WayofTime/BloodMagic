package wayoftime.bloodmagic;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigManager
{
	private static final Logger LOGGER = LogManager.getLogger();

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
		public final ForgeConfigSpec.DoubleValue demonWillGaugeX;
		public final ForgeConfigSpec.DoubleValue demonWillGaugeY;

		ClientConfig(ForgeConfigSpec.Builder builder)
		{
			builder.comment("Settings for the position of the Demon Will Gauge HUD element.").push("hud");
			demonWillGaugeX = builder.defineInRange("DemonWillGaugePosX", 0.01, 0, 1);
			demonWillGaugeY = builder.defineInRange("DemonWillGaugePosY", 0.01, 0, 1);
			builder.pop();
		}
	}
}
