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
        ItemStack secondary = inputs.get(1);
        if (!primary.is(secondary.getItem())) { // not the same kind
            return false;
        }
        if (!(primary.is(BloodMagicItems.ITEM_ROUTER_FILTER.get())
                || primary.is(BloodMagicItems.ITEM_TAG_FILTER.get())
                || primary.is(BloodMagicItems.ITEM_MOD_FILTER.get())
                || primary.is(BloodMagicItems.ITEM_ENCHANT_FILTER.get())
                || primary.is(BloodMagicItems.ITEM_COMPOSITE_FILTER.get())))
        { // not a filter
            return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer grid, RegistryAccess registries) {
        return grid.getItems().get(0).copyWithCount(2);
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
