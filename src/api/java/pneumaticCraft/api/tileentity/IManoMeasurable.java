package pneumaticCraft.api.tileentity;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public interface IManoMeasurable{
    /**
     * This method is invoked by the Manometer when a player right-clicks a TE or Entity with this interface implemented.
     * @param player that rightclicks the measurable TE, and therefore needs to get the message
     * @param curInfo list you can append info to. If you don't append any info no air will be used.
     */
    public void printManometerMessage(EntityPlayer player, List<String> curInfo);
}
