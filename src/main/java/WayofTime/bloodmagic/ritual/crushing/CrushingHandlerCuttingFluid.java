package WayofTime.bloodmagic.ritual.crushing;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CrushingHandlerCuttingFluid implements ICrushingHandler {

    private int lpDrain;

    private double willDrain;

    private ItemStack cuttingStack;

    public CrushingHandlerCuttingFluid(ItemStack cuttingStack, int lpDrain, double willDrain) {
        this.lpDrain = lpDrain;
        this.willDrain = willDrain;
        this.cuttingStack = cuttingStack;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput(ItemStack inputStack, World world, BlockPos pos) {
        List<ItemStack> inputList = new ArrayList<>();
        inputList.add(cuttingStack);
        inputList.add(inputStack.copy());
        RecipeAlchemyTable recipeAlchemyTable = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyTable(inputList);

        if (recipeAlchemyTable != null) {
            return recipeAlchemyTable.getOutput().copy();
        }

        return ItemStack.EMPTY;
    }

    public double getWillDrain() {
        return willDrain;
    }

    public int getLpDrain() {
        return lpDrain;
    }
}
