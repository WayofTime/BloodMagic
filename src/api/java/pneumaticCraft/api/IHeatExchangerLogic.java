package pneumaticCraft.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * DO NOT IMPLEMENT THIS CLASS YOURSELF! Use PneumaticRegistry.getInstance().getHeatExchangerLogic() !
 * @author MineMaarten
 * www.minemaarten.com
 */
public interface IHeatExchangerLogic{

    /**
     * Call this to tick this logic, and make the heat disperse itself.
     */
    public void update();

    /**
     * When called (preferably on tile entity load and neighbor block/tile entity change) this will add all IHeatExchanger neighbor TileEntities as connected heat exchangers.
     * It will also take care of blocks like Lava.
     * 
     * You don't _have_ to call this method, if this heat exchanger is not connected to the outside world (for example the heat of the liquid
     * plastic in the Plastic Mixer).
     * @param world
     * @param x
     * @param y
     * @param z
     * @param validSides Can be left out as vararg, meaning every side can be connected. When one or more sides are specified this will constrain
     * this heat exchanger to only connect to other heat exchangers on these sides.
     */
    public void initializeAsHull(World world, int x, int y, int z, ForgeDirection... validSides);

    /**
     * When called, this will connect these two heat exchangers. You should only call this on one of the two heat exchangers. 
     * @param exchanger
     */
    public void addConnectedExchanger(IHeatExchangerLogic exchanger);

    public void removeConnectedExchanger(IHeatExchangerLogic exchanger);

    /**
     * A heat exchanger starts with 295 degrees Kelvin (20 degrees Celcius) by default.
     * @param temperature in degrees Kelvin
     */
    public void setTemperature(double temperature);

    public double getTemperature();

    /**
     * The higher the thermal resistance, the slower the heat disperses.
     * @param thermalResistance By default it's 1.
     */
    public void setThermalResistance(double thermalResistance);

    public double getThermalResistance();

    /**
     * The higher the capacity, the more heat can be 'stored'. This means that an object with a high capacity can heat up an object with a lower
     * capacity without losing any significant amount of temperature.
     * @param capacity
     */
    public void setThermalCapacity(double capacity);

    public double getThermalCapacity();

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    /**
     * Adds heat (= deltaT * Thermal Capacity) to this exchanger. negative values will remove heat.
     * @param amount
     */
    public void addHeat(double amount);

}
