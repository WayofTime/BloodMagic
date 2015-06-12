package pneumaticCraft.api.universalSensor;

import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.eventhandler.Event;

public interface IEventSensorSetting extends ISensorSetting{

    /**
     * This method is only invoked when a subscribed event is triggered.
     * @param event
     * @param sensor
     * @param range
     * @param textboxText
     * @return Redstone strength for the given event.
     */
    public int emitRedstoneOnEvent(Event event, TileEntity sensor, int range, String textboxText);

    /**
     * Should return how long a pulse should hold in ticks. By default this is 5 ticks (1/4 second).
     * @return
     */
    public int getRedstonePulseLength();
}
