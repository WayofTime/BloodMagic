package WayofTime.bloodmagic.api.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BloodMagicCraftedEvent extends Event {

    private final boolean modifiable;
    private final ItemStack[] inputs;
    private ItemStack output;

    public BloodMagicCraftedEvent(ItemStack output, ItemStack[] inputs, boolean modifiable) {
        this.modifiable = modifiable;
        this.inputs = inputs;
        this.output = output;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public ItemStack[] getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ItemStack output) {
        if (isModifiable())
            this.output = output;
    }

    /**
     * Fired whenever a craft is completed in a Blood Altar.
     *
     * It is not cancelable, however you can modify the output stack.
     */
    public static class Altar extends BloodMagicCraftedEvent {

        public Altar(ItemStack output, ItemStack input) {
            super(output, new ItemStack[] { input }, true);
        }
    }

    /**
     * Fired whenever a craft is completed in a Soul Forge.
     *
     * It is not cancelable, however you can modify the output stack.
     */
    public static class SoulForge extends BloodMagicCraftedEvent {

        public SoulForge(ItemStack output, ItemStack[] inputs) {
            super(output, inputs, true);
        }
    }

    /**
     * Fired whenever a craft is completed in an Alchemy Table.
     *
     * It is not cancelable, however you can modify the output stack.
     */
    public static class AlchemyTable extends BloodMagicCraftedEvent {

        public AlchemyTable(ItemStack output, ItemStack[] inputs) {
            super(output, inputs, true);
        }
    }

}
