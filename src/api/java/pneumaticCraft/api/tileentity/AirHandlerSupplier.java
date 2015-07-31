package pneumaticCraft.api.tileentity;

import java.lang.reflect.Constructor;

public class AirHandlerSupplier{
    private static Constructor airHandlerConstructor;

    public static IAirHandler getTierOneAirHandler(int volume){
        return getAirHandler(5F, 7F, volume);
    }

    public static IAirHandler getTierTwoAirHandler(int volume){
        return getAirHandler(20F, 25F, volume);
    }

    /**
     * Returns a new instance of an IAirHandler. This handler handles everything pressurized air related: Air dispersion,
     * blowing up when the pressure gets too high, providing a method for releasing air into the atmosphere...
     * PROVIDED THAT THE FOLLOWING METHODS ARE FORWARDED TO THIS INSTANCE:
     * {@link net.minecraft.tileentity.TileEntity#updateEntity()}, 
     * {@link net.minecraft.tileentity.TileEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)}
     * {@link net.minecraft.tileentity.TileEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)}
     * {@link net.minecraft.tileentity.TileEntity#validate()}
     * @param dangerPressure minimal pressure on which this machine can explode (the yellow to red transition)
     * @param criticalPressure the absolute maximum pressure the machine can take 7 bar in tier 1 machines.
     * @param maxFlow maximum mL/tick that this machine can disperse. Tier one machines do 50mL/tick while Tier two have 200mL/tick.
     * @param volume Volume of the machine's internal storage. These vary from 1000mL for small machines to 10,000mL for the big ones.
     * The higher the volume the slower the machine will charge/discharge.
     * @return
     */
    public static IAirHandler getAirHandler(float dangerPressure, float criticalPressure, int volume){
        IAirHandler airHandler = null;
        try {
            if(airHandlerConstructor == null) airHandlerConstructor = Class.forName("pneumaticCraft.common.tileentity.TileEntityPneumaticBase").getConstructor(float.class, float.class, int.class);
            airHandler = (IAirHandler)airHandlerConstructor.newInstance(dangerPressure, criticalPressure, volume);
        } catch(Exception e) {
            System.err.println("[PneumaticCraft API] An error has occured whilst trying to get an AirHandler. Here's a stacktrace:");
            e.printStackTrace();
        }
        return airHandler;
    }

    /**
     * Use the version with integer parameters
     */
    @Deprecated
    public static IAirHandler getAirHandler(float dangerPressure, float criticalPressure, float maxFlow, float volume){
        return getAirHandler(dangerPressure, criticalPressure, (int)volume);
    }

}
