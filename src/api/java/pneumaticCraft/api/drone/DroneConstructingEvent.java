package pneumaticCraft.api.drone;

import cpw.mods.fml.common.eventhandler.Event;

public class DroneConstructingEvent extends Event{
    public IDrone drone;

    public DroneConstructingEvent(IDrone drone){
        this.drone = drone;
    }
}
