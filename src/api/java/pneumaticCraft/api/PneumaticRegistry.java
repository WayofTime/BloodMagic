package pneumaticCraft.api;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import pneumaticCraft.api.client.pneumaticHelmet.IBlockTrackEntry;
import pneumaticCraft.api.client.pneumaticHelmet.IEntityTrackEntry;
import pneumaticCraft.api.client.pneumaticHelmet.IHackableBlock;
import pneumaticCraft.api.client.pneumaticHelmet.IHackableEntity;
import pneumaticCraft.api.drone.ICustomBlockInteract;
import pneumaticCraft.api.drone.IPathfindHandler;
import pneumaticCraft.api.item.IInventoryItem;

/**
 * This class can be used to register and access various things to and from the mod.
 */
public class PneumaticRegistry{
    /**
     * This field, which is initialized in PneumaticCraft's preInit, will give you access to various registration and access options.
     * @deprecated This field isn't going to be removed, but it'll be marked private. use getInstance().
     */
    @Deprecated
    public static IPneumaticCraftInterface instance;

    public static IPneumaticCraftInterface getInstance(){
        return instance;
    }

    public static void init(IPneumaticCraftInterface inter){
        if(instance == null) instance = inter;//only allow initialization once; by PneumaticCraft
    }

    public static interface IPneumaticCraftInterface{

        /*
         * ------------- Pneumatic Helmet --------------
         */

        public void registerEntityTrackEntry(Class<? extends IEntityTrackEntry> entry);

        public void registerBlockTrackEntry(IBlockTrackEntry entry);

        public void addHackable(Class<? extends Entity> entityClazz, Class<? extends IHackableEntity> iHackable);

        public void addHackable(Block block, Class<? extends IHackableBlock> iHackable);

        /**
         * Returns a list of all current successful hacks of a given entity. This is used for example in Enderman hacking, so the user
         * can only hack an enderman once (more times wouldn't have any effect). This is mostly used for display purposes.
         * @param entity
         * @return empty list if no hacks.
         */
        public List<IHackableEntity> getCurrentEntityHacks(Entity entity);

        /*
         * ------------- Drones --------------
         */

        /**
         * Normally drones will pathfind through any block that doesn't have any collisions (Block#getBlocksMovement returns true).
         * With this method you can register custom blocks to allow the drone to pathfind through them. If the block requires any special
         * handling, like allow pathfinding on certain conditions, you can pass a IPathFindHandler with the registry.
         * @param block
         * @param handler can be null, to always allow pathfinding through this block.
         */
        public void addPathfindableBlock(Block block, IPathfindHandler handler);

        /**
         * This will add a puzzle piece that has only a Area white- and blacklist parameter (similar to a GoTo piece).
         * It will do the specified behaviour. This can be used to create energy import/export widgets.
         * @param interactor
         */
        public void registerCustomBlockInteractor(ICustomBlockInteract interactor);

        /**
         * Will spawn in a Drone a distance away from the given coordinate. The drone is programmed to travel to go to 5 blocks above the specified
         * y level, and drop the deliveredStacks. When there isn't a clear path for the items to fall these 5 blocks the Drone will deliver at a
         * y level above the specified y that _is_ clear. If no clear blocks can be found (when there are only solid blocks), the Drone will
         * drop the items very high up in the air instead, and drop them there.
         * 
         * When the Drone is tried to be catched by a player (by wrenching it), the drone will only the drop the items that it was delivering (or
         * none if it dropped those items already). The Drone itself never will be dropped.
         * 
         * @param x
         * @param y
         * @param z
         * @param deliveredStacks stacks that are delivered by the drone. When no stacks, or more than 65 stacks are given, this will generate a IllegalArgumentException.
         * @return the drone. You can use this to set a custom name for example (defaults to "Amazon Delivery Drone").
         */
        public EntityCreature deliverItemsAmazonStyle(World world, int x, int y, int z, ItemStack... deliveredStacks);

        /*
         * --------------- Items -------------------
         */
        /**
         * See {@link pneumaticCraft.api.item.IInventoryItem}
         * @param handler
         */
        public void registerInventoryItem(IInventoryItem handler);

        /*
         * ----------------- Heat System --------------
         */
        public IHeatExchangerLogic getHeatExchangerLogic();

        public void registerBlockExchanger(Block block, double temperature, double thermalResistance);

        /*
         * ---------------- Power Generation -----------
         */

        /**
         * Adds a burnable liquid to the Liquid Compressor's available burnable fuels.
         * @param fluid
         * @param mLPerBucket the amount of mL generated for 1000mB of the fuel. As comparison, one piece of coal generates 16000mL in an Air Compressor.
         */
        public void registerFuel(Fluid fluid, int mLPerBucket);

        /*
         * --------------- Misc -------------------
         */

        /**
         * Returns the amount of Security Stations that disallow interaction with the given coordinate for the given player.
         * Usually you'd disallow interaction when this returns > 0.
         * @param world
         * @param x
         * @param y
         * @param z
         * @param player
         * @param showRangeLines When true, any Security Station that prevents interaction will show the line grid (server --> client update is handled internally).
         * @return The amount of Security Stations that disallow interaction for the given player.
         * This method throws an IllegalArgumentException when tried to be called from the client side!
         */
        public int getProtectingSecurityStations(World world, int x, int y, int z, EntityPlayer player, boolean showRangeLines);

        /**
         * Use this to register ISimpleBlockRenderHandler render id's of full blocks, those of which should be able to be used for the Pneumatic Door Base camouflage.
         * @param id
         */
        public void registerConcealableRenderId(int id);

        /**
         * Used to register a liquid that represents liquid XP (like MFR mob essence, OpenBlocks liquid XP).
         * This is used in the Aerial Interface to pump XP in/out of the player.
         * @param fluid
         * @param liquidToPointRatio The amount of liquid (in mB) used to get one XP point. In OpenBlocks this is 20 (mB/point).
         */
        public void registerXPLiquid(Fluid fluid, int liquidToPointRatio);

    }
}
