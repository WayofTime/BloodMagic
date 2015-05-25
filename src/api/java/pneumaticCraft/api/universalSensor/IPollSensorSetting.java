package pneumaticCraft.api.universalSensor;

import net.minecraft.world.World;

public interface IPollSensorSetting extends ISensorSetting{

    /**
     * The value returned here is the interval between every check in ticks (the interval of calling getRedstoneValue()).
     * Consider increasing the interval when your sensor method is resource intensive.
     * @return
     */
    public int getPollFrequency();

    /**
     * The base method. This method should return the outputted redstone value 0-15 of this sensor. When this sensor is
     * digital, just return 0 or 15.
     * @param world
     * @param x
     * @param y
     * @param z
     * @param sensorRange Range of the sensor, based on the amount of Range Upgrades inserted in the Universal Sensor.
     * @param textBoxText The text typed in the textbox of the Universal Sensor.
     * @return
     */
    public int getRedstoneValue(World world, int x, int y, int z, int sensorRange, String textBoxText);

}
