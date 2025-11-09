package wayoftime.bloodmagic.common.recipe.forge;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.blockentity.HellfireForgeTile;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.tag.BMTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForgeRecipe implements Recipe<ForgeInput> {

    public static final String RECIPE_TYPE_NAME = "soul_forge";
    public final double minWill;
    public final double usedWill;
    public final List<Ingredient> ingredients;
    public final ItemStack resultItem;
    public final Optional<EnumWillType> willType;
    public ForgeRecipe(double minWill, double usedWill, List<Ingredient> ingredients, ItemStack resultItem, Optional<EnumWillType> willType) {
        this.minWill = minWill;
        this.usedWill = usedWill;
        this.ingredients = ingredients;
        this.resultItem = resultItem;
        this.willType = willType;
    }

    @Override
    public boolean matches(ForgeInput input, Level level) {
        if (input.size() != ingredients.size()) {
            return false;
        }
        EnumWillType will = input.getGem().getOrDefault(BMDataComponents.DEMON_WILL_TYPE, EnumWillType.DEFAULT);
        if (willType.isPresent() && willType.get() != will) {
            return false;
        }

        List<Ingredient> ingredientList = new ArrayList<>(ingredients);
        for (int i = 0; i < input.size(); i++) {
            boolean matched = false;
            for (int j = 0; j < ingredientList.size(); j++) {
                if (ingredientList.get(j).test(input.getItem(i))) {
                    matched = true;
                    ingredientList.remove(j);
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(ForgeInput input, HolderLookup.Provider registries) {
        ItemStack gemStack = input.getGem();
        double will = gemStack.getOrDefault(BMDataComponents.DEMON_WILL_AMOUNT, 0D);
        if (will < minWill) {
            return ItemStack.EMPTY;
        }
        ItemStack outStack = resultItem.copy();
        if (outStack.is(BMTags.Items.SOUL_GEM) && input.getGemIndex() != HellfireForgeTile.GEM_SLOT) {
            outStack.set(BMDataComponents.DEMON_WILL_AMOUNT, will - usedWill);
            outStack.set(BMDataComponents.DEMON_WILL_TYPE, gemStack.get(BMDataComponents.DEMON_WILL_TYPE));
        }

        return outStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return resultItem.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BMRecipes.SOUL_FORGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BMRecipes.SOUL_FORGE_TYPE.get();
    }

    public Double getMinWill() {
        return minWill;
    }

    public Double getDrain() {
        return usedWill;
    }

    public List<Ingredient> getCraftingIngredients() {
        return ingredients;
    }

    public ItemStack getOutput() {
        return resultItem;
    }

    public Optional<EnumWillType> getWillType() {
        return willType;
    }
}
