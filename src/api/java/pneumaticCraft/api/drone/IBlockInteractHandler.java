package pneumaticCraft.api.drone;

/**
 * DON'T IMPLEMENT, just use
 */
public interface IBlockInteractHandler{

    /**
     * Returns a boolean[6] of all sides. when true, this side is accessible
     * @return
     */
    public boolean[] getSides();

    public boolean useCount();

    public void decreaseCount(int count);

    public int getRemainingCount();

    /**
     * When invoked, the drone will abort searching the area. Could be used to abort early when full of RF energy for example, when importing RF.
     * (It's useless to search any further)
     */
    public void abort();

}
