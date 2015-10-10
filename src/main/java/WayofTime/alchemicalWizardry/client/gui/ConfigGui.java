package WayofTime.alchemicalWizardry.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;

import java.util.ArrayList;
import java.util.List;

import static WayofTime.alchemicalWizardry.BloodMagicConfiguration.config;

public class ConfigGui extends GuiConfig {

	public ConfigGui(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), "AWWayofTime", false, false, "Blood Magic Configuration");
	}

	@SuppressWarnings("rawtypes")
	private static List<IConfigElement> getConfigElements(GuiScreen parent) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		// adds sections declared in ConfigHandler. toLowerCase() is used because the configuration class automatically does this, so must we.
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("clientsettings".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("dungeon loot chances".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("meteor".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("orecrushing".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("potion id".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("wellofsufferingblacklist".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("wimpysettings".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("ritual blacklist".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("teleposer blacklist".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("demon configs".toLowerCase())));
		list.add(new ConfigElement<ConfigCategory>(config.getCategory("lp costs".toLowerCase())));

		return list;
	}
}
