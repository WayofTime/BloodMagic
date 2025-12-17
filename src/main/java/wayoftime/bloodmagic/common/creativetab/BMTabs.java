package wayoftime.bloodmagic.common.creativetab;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.BMIdentifiers.Sigils;
import wayoftime.bloodmagic.api.sigil.SigilEffect;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.datacomponent.UpgradeTome;
import wayoftime.bloodmagic.common.fluid.BMFluids;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.living.LivingHelper;
import wayoftime.bloodmagic.common.living.LivingUpgrade;
import wayoftime.bloodmagic.common.registry.BMRegistries;
import wayoftime.bloodmagic.common.tag.BMTags;

import java.util.List;
import java.util.function.Consumer;

public class BMTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodMagic.MODID);

    public static final Holder<CreativeModeTab> MAIN = TABS.register(
            "main",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BMBlocks.BLOOD_ALTAR))
                    .title(Component.translatable("item_group.bloodmagic.main"))
                    .displayItems((parameters, output) -> {
                        addAll(BMBlocks.BLOCK_ITEMS, output::accept);

                        ItemStack living_plate = new ItemStack(BMItems.LIVING_PLATE);
                        LivingHelper.setDefaultLiving(living_plate, parameters.holders());
                        output.accept(living_plate);

                        addSigils(parameters.holders(), output::accept);
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

    private static void addSigils(HolderLookup.Provider registries, Consumer<ItemStack> tab) {
        // TODO cant seem to get all entries for a given registry from here, either make a tag with all sigils to add here or do it manually
        ItemStack sigilStack = new ItemStack(BMItems.SIGIL);
        // for now, this is the order of them appearing in the tab
        List<ResourceKey<SigilEffect>> displaySigils = List.of(Sigils.DIVINATION, Sigils.SEER, Sigils.LAVA, Sigils.WATER, Sigils.VOID, Sigils.MINER);
        displaySigils.forEach(key -> {
            ItemStack tmp = sigilStack.copy();
            tmp.set(BMDataComponents.SIGIL_EFFECT, key);
            tab.accept(tmp);
        });
    }

    public static final Holder<CreativeModeTab> TOMES = TABS.register(
            "tomes",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BMItems.UPGRADE_TOME))
                    .title(Component.translatable("item_group.bloodmagic.tomes"))
                    .displayItems((params, output) -> {
                        // TODO maybe have actual tags for up/downgrade to use? idk, this is probably fine
                        addAll(params.holders().lookupOrThrow(BMRegistries.Keys.LIVING_UPGRADES).get(BMTags.Living.TOOLTIP_ORDER).orElseThrow(), output::accept);
                    })
                    .build()
    );

    public static final Holder<CreativeModeTab> TRAINERS = TABS.register(
            "trainers",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BMItems.UPGRADE_TOME))
                    .title(Component.translatable("item_group.bloodmagic.trainers"))
                    .displayItems((params, output) -> {
                        addAll(params.holders().lookupOrThrow(BMRegistries.Keys.LIVING_UPGRADES).get(BMTags.Living.TRAINERS).orElseThrow(), output::accept);
                    })
                    .build()
    );


    private static void addAll(HolderSet<LivingUpgrade> set, Consumer<ItemStack> tab) {
        ItemStack tome = new ItemStack(BMItems.UPGRADE_TOME);
        set.forEach(upgrade -> {
            upgrade.value().levels().expToLevel().forEach((exp, cost) -> {
                tome.set(BMDataComponents.UPGRADE_TOME_DATA, new UpgradeTome(upgrade, exp));
                tab.accept(tome.copy());
            });
        });
    }

    private static void addAll(DeferredRegister<Item> register, Consumer<ItemStack> tab) {
        register.getEntries().forEach(holder -> {
            tab.accept(new ItemStack(holder.getDelegate()));
        });
    }

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
    }
}
