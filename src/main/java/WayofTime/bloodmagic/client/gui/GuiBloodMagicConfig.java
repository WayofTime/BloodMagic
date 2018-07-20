package WayofTime.bloodmagic.client.gui;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.client.hud.ConfigEntryEditHUD;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GuiBloodMagicConfig extends GuiConfig {

    public GuiBloodMagicConfig(GuiScreen parentScreen) {
        super(parentScreen, getElements(), BloodMagic.MODID, false, false, BloodMagic.NAME);
    }

    public static List<IConfigElement> getElements() {
        List<IConfigElement> elements = Lists.newArrayList();

        elements.addAll(ConfigElement.from(ConfigHandler.class).getChildElements());
        elements.add(new ConfigElement(BloodMagic.RITUAL_MANAGER.getConfig().getCategory("rituals")));
        if (Minecraft.getMinecraft().world != null)
            elements.add(new DummyElementEditHUD(BloodMagic.NAME, "config." + BloodMagic.MODID + ".edit_hud"));

        return elements;
    }

    public static class DummyElementEditHUD extends DummyConfigElement.DummyCategoryElement {

        public DummyElementEditHUD(String name, String langKey) {
            super(name, langKey, Collections.emptyList(), ConfigEntryEditHUD.class);
        }
    }

    public static class Factory implements IModGuiFactory {
        @Override
        public void initialize(Minecraft minecraftInstance) {

        }

        @Override
        public boolean hasConfigGui() {
            return true;
        }

        @Override
        public GuiScreen createConfigGui(GuiScreen parentScreen) {
            return new GuiBloodMagicConfig(parentScreen);
        }

        @Override
        public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
            return null;
        }
    }
}
