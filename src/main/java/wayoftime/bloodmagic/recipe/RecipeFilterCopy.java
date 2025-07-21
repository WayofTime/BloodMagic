package wayoftime.bloodmagic.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;

import java.util.List;

public class RecipeFilterCopy extends CustomRecipe {

    public RecipeFilterCopy(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer grid, Level level) {
        List<ItemStack> inputs = grid.getItems();
        ItemStack primary = inputs.get(0);
        if (!(primary.is(BloodMagicItems.ITEM_ROUTER_FILTER.get())
                || primary.is(BloodMagicItems.ITEM_TAG_FILTER.get())
                || primary.is(BloodMagicItems.ITEM_MOD_FILTER.get())
                || primary.is(BloodMagicItems.ITEM_ENCHANT_FILTER.get())
                || primary.is(BloodMagicItems.ITEM_COMPOSITE_FILTER.get())))
        { // not a filter
            return false;
        }
        boolean found = false;
        for (int i = 1; i < inputs.size(); i++) {
            if (!inputs.get(i).isEmpty()) {
                if (!inputs.get(i).is(primary.getItem())) {
                    return false;
                } else {
                    found = true;
                }
            }
        }

        return found; // if only primary is present its not actually copying anything
    }

    @Override
    public ItemStack assemble(CraftingContainer grid, RegistryAccess registries) {
        // if assemble is called we know #matches returns true, so all the items are correct, just need to count them
        int count = 0;
        for (ItemStack stack : grid.getItems()) {
            if (!stack.isEmpty()) {
                count++;
            }
        }
        return grid.getItems().get(0).copyWithCount(count);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BloodMagicRecipeSerializers.FILTER_COPY.getRecipeSerializer();
    }
}
