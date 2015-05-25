package pneumaticCraft.api.recipe;

import net.minecraft.item.ItemStack;

public interface IPressureChamberRecipe{

    /**
     * Returns the threshold which is minimal to craft the recipe. Negative pressures also work.
     * @return threshold pressure
     */
    public float getCraftingPressure();

    /**
     * This method should return the used items in the recipe when the right items are provided to craft this recipe.
     * @param inputStacks
     * @return usedStacks, return null when the inputStacks aren't valid for this recipe.
     */
    public ItemStack[] isValidRecipe(ItemStack[] inputStacks);

    /**
     * When returned true, only the exact same references of the stacks returned by isValidRecipe() will be removed. This is useful
     * to remove stacks with a certain NBT value (like Enchanted Books). Return false for normal behaviour.
     * @return true if exact stacks should be removed only.
     */
    public boolean shouldRemoveExactStacks();

    /**
     * This method will be called when the recipe should output its items. the stacks the recipe output, may be dependent on the input stacks.
     * @param removedStacks same reference to the stacks returned by isValidRecipe.
     * @param inputStacks. These stacks can be modified (like adding/removing NBT data eg.)
     * @return outputStacks. Stacks that will pop 'out of the chamber'
     */
    public ItemStack[] craftRecipe(ItemStack[] inputStacks, ItemStack[] removedStacks);
}
