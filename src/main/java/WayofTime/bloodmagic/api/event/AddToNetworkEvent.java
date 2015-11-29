package WayofTime.bloodmagic.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class AddToNetworkEvent extends Event {

    public String ownerNetwork;
    public int addedAmount;
    public int maximum;

    /**
     * This event is called whenever the network is added to. If cancelled, no LP will be drained from the source. If result is set to Result.DENY,
     * the LP will still be drained but the soul network will not be added to.
     *
     * @param ownerNetwork Key used for the soul network
     * @param addedAmount  Amount added
     * @param maximum      Ceiling that the network can add to
     */
    public AddToNetworkEvent(String ownerNetwork, int addedAmount, int maximum) {
        this.ownerNetwork = ownerNetwork;
        this.addedAmount = addedAmount;
        this.maximum = maximum;
    }
}
