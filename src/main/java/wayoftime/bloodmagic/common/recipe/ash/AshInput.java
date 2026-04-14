package wayoftime.bloodmagic.common.recipe.ash;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class AshInput extends RecipeWrapper {
    public AshInput(IItemHandler inv) {
        super(inv);
    }

    @Override
    public int size() {
        return 2;
    }
}
