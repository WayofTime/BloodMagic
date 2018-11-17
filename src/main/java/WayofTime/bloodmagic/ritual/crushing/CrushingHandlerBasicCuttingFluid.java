package WayofTime.bloodmagic.ritual.crushing;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.item.alchemy.ItemCuttingFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CrushingHandlerBasicCuttingFluid implements ICrushingHandler {

    private static final Integer LP_DRAIN = 250;

    private static final Double WILL_DRAIN = 0.5;

    private static final ItemStack CUTTING_STACK = ItemCuttingFluid.FluidType.BASIC.getStack();

    @Override
    public ItemStack getRecipeOutput(ItemStack inputStack, World world, BlockPos pos) {
        List<ItemStack> inputList = new ArrayList<>();
        inputList.add(CUTTING_STACK);
        inputList.add(inputStack.copy());
        RecipeAlchemyTable recipeAlchemyTable = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyTable(inputList);

        if (recipeAlchemyTable != null) {
            return recipeAlchemyTable.getOutput();
        }

        return null;
    }

    public Double getWillDrain() {
        return WILL_DRAIN;
    }

    public Integer getLpDrain() {
        return LP_DRAIN;
    }
}
