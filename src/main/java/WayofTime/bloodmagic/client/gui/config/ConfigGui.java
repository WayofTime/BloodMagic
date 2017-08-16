package WayofTime.bloodmagic.client.gui.config;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class ConfigGui extends GuiConfig {

    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), BloodMagic.MODID, false, false, "BloodMagic Configuration");
    }

    @SuppressWarnings("rawtypes")
    private static List<IConfigElement> getConfigElements(GuiScreen parent) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        // adds sections declared in ConfigHandler. toLowerCase() is used
        // because the configuration class automatically does this, so must we.
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Potions".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Client".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Compatibility".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Teleposer Blacklist".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Well of Suffering Blacklist".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Item/Block Blacklisting".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("General".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Rituals".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Blood Altar Sacrificial Values".toLowerCase())));

        return list;
    }
}
