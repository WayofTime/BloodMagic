package wayoftime.bloodmagic.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.common.item.ItemLivingTome;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeTomeCombine extends CustomRecipe {

    public RecipeTomeCombine(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer grid, Level level) {
        List<ItemStack> inputs = grid.getItems();
        Map<LivingUpgrade, Double> allUpgrades = new HashMap<>();
        int count = 0;
        for (int i = 0; i < inputs.size(); i++) {
            ItemStack inputStack = inputs.get(i);
            if (inputStack.isEmpty()) { // on empty move on
                continue;
            }
            if (inputStack.getItem() instanceof ItemLivingTome tome) {
                count++;
                LivingStats stats = tome.getLivingStats(inputStack);
                stats.getUpgrades().forEach((key, amount) -> allUpgrades.compute(key, (k, v) -> v == null ? amount : v + amount));
            } else { // dont have a tome and not empty, not valid item
                return false;
            }
        }
        return allUpgrades.size() == 1 && count > 1; // if theres one or less tomes its not valid, and if there are not exactly 1 upgrade kinds its not valid
    }

    @Override
    public ItemStack assemble(CraftingContainer grid, RegistryAccess registries) {
        List<ItemStack> inputs = grid.getItems();
        Map<LivingUpgrade, Double> allUpgrades = new HashMap<>();
        for (int i = 0; i < inputs.size(); i++) {
            ItemStack inputStack = inputs.get(i);
            if (inputStack.getItem() instanceof ItemLivingTome tome) {
                LivingStats stats = tome.getLivingStats(inputStack);
                stats.getUpgrades().forEach((key, amount) -> allUpgrades.compute(key, (k, v) -> v == null ? amount : v + amount));
            }
        }

        if (allUpgrades.size() > 1) {
            return ItemStack.EMPTY;
        }

        for (Map.Entry<LivingUpgrade, Double> entry : allUpgrades.entrySet()) {
            int exp = entry.getValue().intValue();
            LivingUpgrade upgrade = entry.getKey();
            ItemStack upgradeStack = new ItemStack(BloodMagicItems.LIVING_TOME.get());
            ((ILivingContainer) BloodMagicItems.LIVING_TOME.get()).updateLivingStats(upgradeStack, new LivingStats().setMaxPoints(upgrade.getLevelCost(exp)).addExperience(upgrade.getKey(), exp));
            return upgradeStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BloodMagicRecipeSerializers.TOME_COMBINE.getRecipeSerializer();
    }
}
