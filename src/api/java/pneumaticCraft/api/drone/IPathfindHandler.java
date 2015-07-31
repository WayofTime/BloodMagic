package pneumaticCraft.api.drone;

import net.minecraft.world.World;

public interface IPathfindHandler{
    /**
     * When returned true, the drone can pathfind through this block. When false it can't.
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean canPathfindThrough(World world, int x, int y, int z);

    /**
     * CURRENTLY NOT IMPLEMENTED!
     * Will be called every tick as long as the drone is < 1 block away from the given coordinate.
     * can be used to open a door for a drone for example.
     * @param world
     * @param x
     * @param y
     * @param z
     */
    public void onPathingThrough(World world, int x, int y, int z);
}
