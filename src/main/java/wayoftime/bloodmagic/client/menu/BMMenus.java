package wayoftime.bloodmagic.client.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.client.screen.ARCScreen;

public class BMMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, BloodMagic.MODID);
    public static final DeferredHolder<MenuType<?>, MenuType<ARCMenu>> ARC = MENUS.register("arc_menu", () -> IMenuTypeExtension.create(ARCMenu::new));

    public static void register(IEventBus modbus) {
        MENUS.register(modbus);
        modbus.addListener(BMMenus::registerScreens);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ARC.get(), ARCScreen::new);
    }
}
