package wayoftime.bloodmagic.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.ItemLivingArmor;
import wayoftime.bloodmagic.common.item.ItemLivingTome;
import wayoftime.bloodmagic.common.item.ItemLivingTomeScrap;
import wayoftime.bloodmagic.common.item.soul.ItemMonsterSoul;
import wayoftime.bloodmagic.common.item.soul.ItemSoulGem;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.LivingStats;

import java.util.function.Consumer;

public class BloodMagicCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodMagic.MODID);
    public static final RegistryObject<CreativeModeTab> BLOODMAGIC = CREATIVE_TABS.register("bloodmagic", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bloodmagic.creativeTab"))
            .icon(() -> BloodMagicBlocks.BLOOD_ALTAR.get().asItem().getDefaultInstance())
            .displayItems((params, output) -> {
                for (RegistryObject<Item> entry : BloodMagicItems.ITEMS.getEntries()) {
                    if (entry.getId().getNamespace().equals(BloodMagic.MODID)) {
                        variantBuilder(entry.get(), output::accept);
                    }
                }
                for (RegistryObject<Item> entry : BloodMagicItems.BASICITEMS.getEntries()) {
                    if (entry.getId().getNamespace().equals(BloodMagic.MODID)) {
                        variantBuilder(entry.get(), output::accept);
                    }
                }
            })
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .build());

    public static final RegistryObject<CreativeModeTab> BLOODMAGIC_UPGRADES = CREATIVE_TABS.register("bloodmagic_upgrades", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bloodmagic.upgradeTab"))
            .icon(() -> BloodMagicItems.LIVING_TOME.get().getDefaultInstance())
            .displayItems((params, output) -> {
                LivingArmorRegistrar.UPGRADE_MAP.forEach((resourceLocation, livingUpgrade) -> {
                    int exp = 0;
                    ItemLivingTome item = (ItemLivingTome) BloodMagicItems.LIVING_TOME.get();
                    while ((exp = livingUpgrade.getNextRequirement(exp)) != 0) {
                        ItemStack tome = new ItemStack(item);
                        item.updateLivingStats(tome, new LivingStats().setMaxPoints(livingUpgrade.getLevelCost(exp)).addExperience(resourceLocation, exp));
                        output.accept(tome);
                    }
                });
            })
            .withTabsBefore(BLOODMAGIC.getKey())
            .build());

    public static void variantBuilder(Item item, Consumer<ItemStack> consumer) {
        if (item instanceof ItemLivingArmor armor) {
            if (armor.getType() == ArmorItem.Type.CHESTPLATE) {
                ItemStack stack = armor.getDefaultInstance();
                armor.updateLivingStats(stack, new LivingStats());
                consumer.accept(stack);
                return;
            }
        } else if (item instanceof ItemLivingTomeScrap scrap) {
            ItemStack stack = scrap.getDefaultInstance();
            scrap.setTotalUpgradePoints(stack, 256);
            consumer.accept(stack);
            return;
        } else if (item instanceof ItemMonsterSoul itemMonsterSoul) {
            ItemStack stack = itemMonsterSoul.getDefaultInstance();
            EnumDemonWillType type = itemMonsterSoul.getType(stack);
            itemMonsterSoul.setWill(type, stack, 5);
            consumer.accept(stack);
            return;
        } else if (item instanceof ItemSoulGem soulGem) {
            for (EnumDemonWillType type : EnumDemonWillType.values()) {
                ItemStack stack = new ItemStack(soulGem);
                soulGem.setCurrentType(type, stack);
                soulGem.setWill(type, stack, soulGem.getMaxWill(type, stack));
                consumer.accept(stack);
            }
            return;
        }
        consumer.accept(item.getDefaultInstance());
    }
}