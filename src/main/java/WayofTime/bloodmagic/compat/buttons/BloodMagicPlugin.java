package WayofTime.bloodmagic.compat.buttons;

import WayofTime.bloodmagic.compat.buttons.button.ButtonFillNetwork;
import net.minecraft.util.ResourceLocation;
import tehnut.buttons.api.ButtonsPlugin;
import tehnut.buttons.api.IWidgetPlugin;
import tehnut.buttons.api.IWidgetRegistry;
import tehnut.buttons.api.WidgetTexture;

@ButtonsPlugin
public class BloodMagicPlugin extends IWidgetPlugin.Base {

    public static final WidgetTexture FILL_BUTTON = new WidgetTexture(
            new ResourceLocation("bloodmagic", "textures/gui/buttons_compat.png"),
            0,
            0,
            20,
            20
    );

    @Override
    public void register(IWidgetRegistry widgetRegistry) {
        widgetRegistry.addUtilityButton(new ButtonFillNetwork());
    }
}
