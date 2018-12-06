package WayofTime.bloodmagic.client.hud;

import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigEntryEditHUD extends GuiConfigEntries.CategoryEntry {

    public ConfigEntryEditHUD(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement element) {
        super(owningScreen, owningEntryList, element);

        this.childScreen = new GuiEditHUD(owningScreen);
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public void setToDefault() {
        ElementRegistry.resetPos();
    }

    @Override
    public boolean isChanged() {
        return ((GuiEditHUD) childScreen).changes;
    }

    @Override
    public void undoChanges() {

    }

    @Override
    public boolean saveConfigElement() {
        return false;
    }
}
