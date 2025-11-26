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

    public static final DeferredHolder<MenuType<?>, MenuType<TrainerMenu>> TRAINER = MENUS.register("trainer", () -> new MenuType<>(TrainerMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>, MenuType<NodeMasterMenu>> MASTER_NODE = MENUS.register("node_master", () -> new MenuType<>(NodeMasterMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<NodeFilterMenu>> FILTERED_NODE = MENUS.register("node_filtered", () -> IMenuTypeExtension.create(NodeFilterMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<FilterMenu>> ITEM_FILTER = MENUS.register("item_filter", () -> IMenuTypeExtension.create(FilterMenu::new));

    public static void register(IEventBus modbus) {
        MENUS.register(modbus);
    }
}
