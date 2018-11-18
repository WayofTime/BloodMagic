package WayofTime.bloodmagic.ritual.crushing;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyTable;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
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

    @Override
    public boolean hasResources(double will, int lp) {
        return will >= this.getWillDrain() && lp >= this.getLpDrain();
    }

    @Override
    public int consumeResources(IMasterRitualStone mrs, EnumDemonWillType will, World world, BlockPos pos) {
        WorldDemonWillHandler.drainWill(world, pos, will, willDrain, true);
        mrs.getOwnerNetwork().syphon(mrs.ticket(lpDrain));
        return mrs.getOwnerNetwork().getCurrentEssence();
    }

    public double getWillDrain() {
        return willDrain;
    }

    public int getLpDrain() {
        return lpDrain;
    }
}
