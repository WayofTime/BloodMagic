package WayofTime.bloodmagic.api.event;

import WayofTime.bloodmagic.api.impl.recipe.RecipeBloodAltar;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BloodMagicCraftedEvent extends Event {

    private final boolean modifiable;
    private ItemStack output;

    public BloodMagicCraftedEvent(ItemStack output, boolean modifiable) {
        this.modifiable = modifiable;
        this.output = output;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ItemStack output) {
        if (isModifiable())
            this.output = output;
    }

    /**
     * Fired whenever a craft is completed in a BloodAltar.
     * <p>
     * It is not cancelable, however you can modify the output stack.
     */
    public static class Altar extends BloodMagicCraftedEvent {

        private final RecipeBloodAltar recipe;

        public Altar(RecipeBloodAltar recipe, ItemStack output) {
            super(output, true);

            this.recipe = recipe;
        }

        public RecipeBloodAltar getRecipe() {
            return recipe;
        }
    }
}
