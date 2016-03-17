package WayofTime.bloodmagic.client.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.Constants;

public class ConfigGui extends GuiConfig
{

    public ConfigGui(GuiScreen parentScreen)
    {
        super(parentScreen, getConfigElements(parentScreen), Constants.Mod.MODID, false, false, "BloodMagic Configuration");
    }

    @SuppressWarnings("rawtypes")
    private static List<IConfigElement> getConfigElements(GuiScreen parent)
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        // adds sections declared in ConfigHandler. toLowerCase() is used
        // because the configuration class automatically does this, so must we.
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("Potions".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("Client".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("Compatibility".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("Teleposer Blacklist".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("Well of Suffering Blacklist".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("Item/Block Blacklisting".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("General".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("Rituals".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("Blood Altar Sacrificial Values".toLowerCase())));
        list.add(new ConfigElement(ConfigHandler.getConfig().getCategory("IDontLikeFun".toLowerCase())));

        return list;
    }
}
