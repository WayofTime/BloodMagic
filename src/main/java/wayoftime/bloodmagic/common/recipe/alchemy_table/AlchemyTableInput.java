package wayoftime.bloodmagic.common.recipe.alchemy_table;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import static wayoftime.bloodmagic.common.blockentity.AlchemyTableTile.ORB_SLOT;

public record AlchemyTableInput(NonNullList<ItemStack> inputs) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return inputs.get(index);
    }

    @Override
    public int size() {
        return inputs().size();
    }
}
