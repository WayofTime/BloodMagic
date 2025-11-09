package wayoftime.bloodmagic.common.recipe.arc;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public class ARCRecipeInput implements RecipeInput {

    private final ItemStack toolStack;
    private final ItemStack inputStack;
    private final FluidStack inputFluid;
    public ARCRecipeInput(ItemStack toolStack, ItemStack inputStack, FluidStack inputFluid) {
        this.toolStack = toolStack;
        this.inputStack = inputStack;
        this.inputFluid = inputFluid;
    }

    @Override
    public ItemStack getItem(int index) {
        return switch(index) {
            case 0 -> toolStack;
            case 1 -> inputStack;
            default -> ItemStack.EMPTY;
        };
    }

    public FluidStack getFluid() {
        return inputFluid;
    }

    @Override
    public int size() {
        return 2;
    }
}
