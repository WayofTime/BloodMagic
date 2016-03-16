package WayofTime.bloodmagic.api.event;

import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;

/**
 * Fired whenever a craft is completed in a BloodAltar.
 * 
 * It is not cancelable, however you can modify the output stack.
 */
@Getter
public class AltarCraftedEvent extends Event
{

    private final AltarRecipeRegistry.AltarRecipe altarRecipe;
    private final ItemStack output;

    /**
     * @param altarRecipe
     *        - The recipe that was crafted.
     * @param output
     *        - The item obtained from the recipe
     */
    public AltarCraftedEvent(AltarRecipeRegistry.AltarRecipe altarRecipe, ItemStack output)
    {
        this.altarRecipe = altarRecipe;
        this.output = output;
    }
}
