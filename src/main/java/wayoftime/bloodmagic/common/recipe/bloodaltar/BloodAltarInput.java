package wayoftime.bloodmagic.common.recipe.bloodaltar;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class BloodAltarInput implements RecipeInput {

    private final ItemStack inputStack;
    private final int altarTier;
    public BloodAltarInput(ItemStack inputStack, int altarTier) {
        this.inputStack = inputStack;
        this.altarTier = altarTier;
    }

    @Override
    public ItemStack getItem(int index) {
        return inputStack;
    }

    public int getAltarTier() {
        return altarTier;
    }

    @Override
    public int size() {
        return 1;
    }
}
