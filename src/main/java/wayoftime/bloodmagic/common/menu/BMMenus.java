package wayoftime.bloodmagic.common.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.LivingStationBlock;

public class BMMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, BloodMagic.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ARCMenu>> ARC = MENUS.register("arc_menu", () -> IMenuTypeExtension.create(ARCMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<LivingStationMenu>> LIVING_STATION = MENUS.register("living_station", () -> IMenuTypeExtension.create(LivingStationMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<TrainerMenu>> TRAINER = MENUS.register("trainer", () -> IMenuTypeExtension.create(TrainerMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<TeleposerMenu>> TELEPOSER = MENUS.register("teleposer", () -> IMenuTypeExtension.create(TeleposerMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<AlchemyTableMenu>> ALCHEMY_TABLE = MENUS.register("alchemy_table", () -> IMenuTypeExtension.create(AlchemyTableMenu::new));

    public static void register(IEventBus modbus) {
        MENUS.register(modbus);
    }
}
