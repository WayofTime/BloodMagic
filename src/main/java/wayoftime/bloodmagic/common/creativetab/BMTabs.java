package wayoftime.bloodmagic.common.creativetab;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.fluid.BMFluids;
import wayoftime.bloodmagic.common.item.BMItems;

import java.util.function.Consumer;

public class BMTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodMagic.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register(
            "main",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BMBlocks.BLOOD_ALTAR))
                    .title(Component.translatable("item_group.bloodmagic.main"))
                    .displayItems((parameters, output) -> {
                        addAll(BMBlocks.BLOCK_ITEMS, output::accept);
                        addAll(BMItems.BASIC_ITEMS, output::accept);
                        addAll(BMItems.ITEMS, output::accept);
                        addAll(BMFluids.BUCKETS, output::accept);
                        BMItems.WILL_ITEMS.getEntries().forEach(holder -> {
                            for (EnumWillType type : EnumWillType.values()) {
                                ItemStack stack = new ItemStack(holder.get());
                                stack.set(BMDataComponents.DEMON_WILL_TYPE, type);
                                output.accept(stack);
                            }
                        });
                        addAll(BMBlocks.BASIC_BLOCK_ITEMS, output::accept);
                    })
                    .build()
    );

    private static void addAll(DeferredRegister<Item> register, Consumer<ItemStack> tab) {
        register.getEntries().forEach(holder -> {
            tab.accept(new ItemStack(holder.getDelegate()));
        });
    }

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
    }
}
