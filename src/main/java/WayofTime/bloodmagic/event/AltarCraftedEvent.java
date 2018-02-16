package WayofTime.bloodmagic.event;

import WayofTime.bloodmagic.core.registry.AltarRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired whenever a craft is completed in a BloodAltar.
 * <p>
 * It is not cancelable, however you can modify the output stack.
 */
public class AltarCraftedEvent extends Event {

    private final AltarRecipeRegistry.AltarRecipe altarRecipe;
    private final ItemStack output;

    /**
     * @param altarRecipe - The recipe that was crafted.
     * @param output      - The item obtained from the recipe
     */
    public AltarCraftedEvent(AltarRecipeRegistry.AltarRecipe altarRecipe, ItemStack output) {
        this.altarRecipe = altarRecipe;
        this.output = output;
    }

    public AltarRecipeRegistry.AltarRecipe getAltarRecipe() {
        return altarRecipe;
    }

    public ItemStack getOutput() {
        return output;
    }
}
