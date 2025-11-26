package wayoftime.bloodmagic.util.helper;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper {
    public static final int CONTAINER_DATA_SIZE = 2 + (3 * 9); // slot/bwlist + per slot tag/enchant/enchant lvl

    public static final int DATA_SLOT = 0;
    public static final int DATA_BWLIST = DATA_SLOT + 1;
    public static final int DATA_TAG = DATA_BWLIST + 1;
    public static final int DATA_ENCHANT = DATA_TAG + 9;
    public static final int DATA_ENCHANT_LVL = DATA_ENCHANT + 9;

    public static final int BUTTON_BWLIST = 0;
    public static final int BUTTON_TAG = 1;
    public static final int BUTTON_ENCHANT = 2;
    public static final int BUTTON_ENCHANT_LVL = 3;

    public static TagKey<Item> getTag(ItemStack target, int index) {
        return target.getTags().toList().get(index - 1);
    }

    public static MutableComponent translate(String name) {
        return Component.translatable("filter.bloodmagic." + name);
    }

    public static int cycleTag(ItemStack contentStack, int index) {
        index++;
        List<TagKey<Item>> tags = contentStack.getTags().toList();
        if (index > tags.size()) {
            index = 0;
        }
        
        return index;
    }

    public static ItemEnchantments getEnchantments(ItemStack contentStack) {
        ItemEnchantments enchantmentList;
        if (contentStack.has(DataComponents.ENCHANTMENTS)) {
            enchantmentList = contentStack.get(DataComponents.ENCHANTMENTS);
        } else if (contentStack.has(DataComponents.STORED_ENCHANTMENTS)) {
            enchantmentList = contentStack.get(DataComponents.STORED_ENCHANTMENTS);
        } else {
            // no enchantments, neither applied nor stored
            return ItemEnchantments.EMPTY;
        }

        return enchantmentList;
    }

    public static int cycleEnchant(ItemStack contentStack, int index) {
        index++;
        ItemEnchantments enchantmentList = getEnchantments(contentStack);
        if (index >= enchantmentList.size() + 2 || enchantmentList.isEmpty()) {
            index = 0;
        }
        if (enchantmentList.size() == 1 && index == 1) { // state would have been 0 before, so changing from 1 to 2 means skipping over "any" if there is only 1 enchant since it'd be the same as "every"
            index = 2;
        }

        return index;
    }

    public static List<Component> tagButtonText(ItemStack contentStack) {
        List<Component> component = new ArrayList<>();
        int index = contentStack.getOrDefault(BMDataComponents.FILTER_TAG_INDEX, 0);
        if (index == 0) {
            component.add(translate("any_tag"));
            contentStack.getTags()
                    .map(TagKey::location)
                    .map(ResourceLocation::toString)
                    .map(Component::literal)
                    .forEachOrdered(component::add);
            return component;
        }

        if (contentStack.has(BMDataComponents.FILTER_TAG)) {
            component.add(translate("specified_tag"));
            component.add(Component.literal(contentStack.get(BMDataComponents.FILTER_TAG).location().toString()));
            return component;
        }

        return List.of(translate("no_valid_tag"));
    }

    public static List<Component> enchantButtonText(ItemStack contentStack) {
        final List<Component> component = new ArrayList<>();
        int index = contentStack.getOrDefault(BMDataComponents.FILTER_ENCHANT_INDEX, 0);
        ItemEnchantments enchantments = getEnchantments(contentStack);
        if (enchantments.isEmpty()) {
            return List.of(translate("no_enchant"));
        }
        if (index < 2) {
            if (index == 0) {
                component.add(translate("every_enchant"));
            } else {
                component.add(translate("any_enchant"));
            }
            EnchantmentHelper.runIterationOnItem(contentStack, (enchantment, level) -> {
                component.add(enchantment.value().description());
            });
        } else {
            component.add(translate("specified_enchant"));
            component.add(enchantments.keySet().stream().toList().get(index - 2).value().description());
        }

        return component;
    }

    public static List<Component> enchantLevelButtonText(ItemStack contentStack) {
        int index = contentStack.getOrDefault(BMDataComponents.FILTER_ENCHANT_LEVEL, 0);
        return index == 0 ? List.of(translate("enchant_lvl_exact")) : List.of(translate("enchant_lvl_any"));
    }
}
