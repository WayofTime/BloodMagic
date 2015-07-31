package pneumaticCraft.api.tileentity;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

/**
 * A way for you to access about everything you need from a pneumatic machine.
 * DO NOT IMPLEMENT THIS YOURSELF! Use AirHandlerSupplier to get an instance for your TileEntity, and implement IPneumaticMachine instead.
 */

public interface IAirHandler extends IManoMeasurable{

    /**
     * -----------Needs to be forwarded by the implementing TileEntity's updateEntity() method.
     * Updates the pneumatic machine's logic like air dispersion and checking if it needs to explode.
     */
    public void updateEntityI();

    /**
     * -----------Needs to be forwarded by the implementing TileEntity.
     * @param nbt
     */
    public void readFromNBTI(NBTTagCompound nbt);

    /**
     * -----------Needs to be forwarded by the implementing TileEntity.
     * @param nbt
     */
    public void writeToNBTI(NBTTagCompound nbt);

    /**
     * -----------Needs to be forwarded by the implementing TileEntity with itself as parameter.
     * @param parent TileEntity that is referencing this air handler.
     */
    public void validateI(TileEntity parent);

    /**
     * Method to release air in the air. It takes air from a specific side, plays a sound effect, and spawns smoke particles.
     * It automatically detects if it needs to release air (when under pressure), suck air (when in vacuum) or do nothing.
     * @param side
     */
    public void airLeak(ForgeDirection side);

    /**
     * Returns a list of all the connecting pneumatics. It takes sides in account.
     */
    public List<Pair<ForgeDirection, IPneumaticMachine>> getConnectedPneumatics();

    /**
     * Adds air to the tank of the given side of this TE. It also updates clients where needed (when they have a GUI opened).
     * Deprecated: use the version with the integer parameter now.
     * @param amount
     * @param side
     */
    @Deprecated
    public void addAir(float amount, ForgeDirection side);

    public void addAir(int amount, ForgeDirection side);

    /**
     * Sets the volume of this TE's air tank. When the volume decreases the pressure will remain the same, meaning air will
     * be lost. When the volume increases, the air remains the same, meaning the pressure will drop.
     * Used in the Volume Upgrade calculations.
     * Deprecated: use the version with the integer parameter now.
     * @param newVolume
     */
    @Deprecated
    public void setVolume(float newVolume);

    public void setVolume(int newVolume);

    public int getVolume();

    /**
     * Returns the pressure at which this TE will explode.
     * @return
     */
    public float getMaxPressure();

    public float getPressure(ForgeDirection sideRequested);

    /**
     * Returns the amount of air (that has a relation to the pressure: air = pressure * volume)
     * @param sideRequested
     * @return
     */
    public int getCurrentAir(ForgeDirection sideRequested);

    /**
     * When your TileEntity is implementing IInventory and has slots that accept PneumaticCraft upgrades, register these slots
     * to the air handler by calling this method once on initialization of the TileEntity. Then they'll automatically be used to get Volume/Security upgrades.
     * @param upgradeSlots all upgrade slots stored in an array.
     */
    public void setUpgradeSlots(int... upgradeSlots);

    public int[] getUpgradeSlots();

    public int getXCoord();

    public int getYCoord();

    public int getZCoord();

    /**
     * Needs to be forwarded from the implementing _Block_! Forward the Block's "onNeighborChange" method to this handler.
     */
    public void onNeighborChange();

}
