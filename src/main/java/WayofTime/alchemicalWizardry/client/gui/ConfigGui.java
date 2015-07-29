package WayofTime.alchemicalWizardry.client.gui;


import static WayofTime.alchemicalWizardry.BloodMagicConfiguration.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigGui extends GuiConfig {

	public ConfigGui(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), "AWWayofTime", false, false, "Blood Magic Configuration");
	}

	@SuppressWarnings("rawtypes")
	private static List<IConfigElement> getConfigElements(GuiScreen parent) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		// adds sections declared in ConfigHandler. toLowerCase() is used because the configuration class automatically does this, so must we.
		list.add(new ConfigElement(config.getCategory("clientsettings".toLowerCase())));
		list.add(new ConfigElement(config.getCategory("dungeon loot chances".toLowerCase())));
		list.add(new ConfigElement(config.getCategory("meteor".toLowerCase())));
		list.add(new ConfigElement(config.getCategory("orecrushing".toLowerCase())));
		list.add(new ConfigElement(config.getCategory("potion id".toLowerCase())));
		list.add(new ConfigElement(config.getCategory("wellofsufferingblacklist".toLowerCase())));
		list.add(new ConfigElement(config.getCategory("wimpysettings".toLowerCase())));
		list.add(new ConfigElement(config.getCategory("ritual blacklist".toLowerCase())));
		list.add(new ConfigElement(config.getCategory("teleposer blacklist".toLowerCase())));
		list.add(new ConfigElement(config.getCategory("demon configs".toLowerCase())));

		return list;
	}
}
