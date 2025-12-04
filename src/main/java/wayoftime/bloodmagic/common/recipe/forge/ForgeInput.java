package wayoftime.bloodmagic.common.recipe.forge;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public class ForgeInput implements RecipeInput {

    private final List<ItemStack> inputStacks;
    private final ItemStack gemStack;
    private final int gemIndex;

    public ForgeInput(List<ItemStack> items, ItemStack gemStack, int gemIndex) {
        this.inputStacks = items;
        this.gemStack = gemStack;
        this.gemIndex = gemIndex;
    }

    @Override
    public ItemStack getItem(int index) {
        return inputStacks.get(index);
    }

    public ItemStack getGem() {
        return this.gemStack;
    }

    public int getGemIndex() {
        return this.gemIndex;
    }

    public ItemStack[] asArray() {
        return new ItemStack[]{inputStacks.get(0), inputStacks.get(1), inputStacks.get(2), inputStacks.get(3)};
    }

    @Override
    public int size() {
        return (int) inputStacks.stream().filter(stack -> !stack.isEmpty()).count();
    }
}
