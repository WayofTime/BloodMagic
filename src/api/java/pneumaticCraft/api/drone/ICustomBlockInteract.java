package pneumaticCraft.api.drone;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkPosition;

/**
 * Implement this and register it to PneumaticRegistry.registerCustomBlockInteractor().
 * This will add a puzzle piece that has only a Area white- and blacklist parameter (similar to a GoTo piece).
 * It will do the specified behaviour. This can be used to create energy import/export widgets.
 */
public interface ICustomBlockInteract{

    /**
     * Should return a unique Id, used in NBT saving and localization.
     */
    public String getName();

    /**
     * Should return the puzzle piece texture. Should be a multiple of 80x64 (width x height). I'd recommend starting out with copying the Go To widget texture.
     * @return
     */
    public ResourceLocation getTexture();

    /**
     * The actual interaction.
     * 
     * For every position in the selected area the drone will visit every block (ordered from closest to furthest). It will call this method with 'simulate = true'. If this method returns true, the drone will navigate to this location, and call this method again with 'simulate = false' It will keep doing this until this method returns false.
     * 
     * When interactHandler.useCount() returns true:
     * In the interface of the puzzle piece users can specify a 'use count' and fill in the maximum count they want to use. When not simulating, you should only import/export up to interactHandler.getRemainingCount(), and you should notify the removed/added count by doing interactHandler.decreaseCount(int count).
     * 
     * @param pos   current visited location
     * @param drone
     * @param interactHandler   object you can use to use to get accessible sides and give feedback about counts.
     * @param simulate  will be true when trying to figure out whether or not the drone should navigate to this block, false when next to this block.
     * @return
     */
    public boolean doInteract(ChunkPosition pos, IDrone drone, IBlockInteractHandler interactHandler, boolean simulate);

    /**
     * Used for crafting, categorizes the puzzle piece.
     * @return
     */
    public int getCraftingColorIndex();
}
