package wayoftime.bloodmagic.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.item.ItemLivingArmor;
import wayoftime.bloodmagic.common.item.ItemLivingTomeScrap;
import wayoftime.bloodmagic.common.item.soul.ItemMonsterSoul;
import wayoftime.bloodmagic.common.item.soul.ItemSoulGem;
import wayoftime.bloodmagic.core.living.LivingStats;

import java.util.Map;
import java.util.function.Consumer;

public class BloodMagicCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodMagic.MODID);
    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("bloodmagictab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bloodmagictab"))
            .icon(() -> BloodMagicBlocks.BLOOD_ALTAR.get().asItem().getDefaultInstance())
            .displayItems((params, output) -> {
                for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
                    if (entry.getKey().location().getNamespace().equals(BloodMagic.MODID)) {
                        itemBuilder(entry.getValue(), output::accept);
                    }
                }
            })
            .build());
    private BloodMagicCreativeTabs() {

    }

    public static void itemBuilder(Item item, Consumer<ItemStack> consumer) {
        if (item instanceof ItemLivingArmor armor) {
            if (armor.getType() == ArmorItem.Type.CHESTPLATE) {
                ItemStack stack = armor.getDefaultInstance();
                armor.updateLivingStats(stack, new LivingStats());
                consumer.accept(stack);
            }
        } else if (item instanceof ItemLivingTomeScrap scrap) {
            ItemStack stack = scrap.getDefaultInstance();
            scrap.setTotalUpgradePoints(stack, 256);
            consumer.accept(stack);
        } else if (item instanceof ItemMonsterSoul itemMonsterSoul) {
            ItemStack stack = itemMonsterSoul.getDefaultInstance();
            EnumDemonWillType type = itemMonsterSoul.getType(stack);
            itemMonsterSoul.setWill(type, stack, 5);
            consumer.accept(stack);
        } else if (item instanceof ItemSoulGem soulGem) {
            for (EnumDemonWillType type : EnumDemonWillType.values()) {
                ItemStack stack = new ItemStack(soulGem);
                soulGem.setCurrentType(type, stack);
                soulGem.setWill(type, stack, soulGem.getMaxWill(type, stack));
                consumer.accept(stack);
            }
        }
        consumer.accept(item.getDefaultInstance());
    }
}