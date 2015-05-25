package pneumaticCraft.api.item;

import net.minecraft.item.ItemStack;

/**
 * Implement this for items that can get programmed in a Programmer.
 * For now the only thing you can do with this is make program storages, later more interaction with programming puzzles is planned.
 * Puzzle pieces will be written onto the implementer's itemstack NBT, using the "progWidget" tag.
 */
public interface IProgrammable{

    /**
     * When returned true, this stack is allowed to be programmed.
     * Used to allow certain damage values to be programmed while others can't.
     * @param stack
     * @return
     */
    public boolean canProgram(ItemStack stack);

    /**
     * When returned true, Programming Puzzles are needed to program this item. When returned false, it's free to program.
     * Drones and Network API's return true in PneumaticCraft, Network Storages return false.
     * @param stack
     * @return
     */
    public boolean usesPieces(ItemStack stack);

    /**
     * When returned true, the implementing item will get a tooltip added of the summary of puzzles used in the stored program.
     * @return
     */
    public boolean showProgramTooltip();

}
