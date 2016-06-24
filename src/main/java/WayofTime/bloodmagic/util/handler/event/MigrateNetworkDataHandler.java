package WayofTime.bloodmagic.util.handler.event;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.annot.Handler;
import WayofTime.bloodmagic.api.saving.BMWorldSavedData;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import com.google.common.base.Stopwatch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

// Migrates from the old data storage system to the cleaner new one
@Handler
public class MigrateNetworkDataHandler
{

    @SubscribeEvent
    public void playerJoin(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            UUID playerId = PlayerHelper.getUUIDFromPlayer(player);
            Stopwatch stopwatch = Stopwatch.createStarted();

            if (event.getWorld().getMapStorage() == null)
                return;

            BMWorldSavedData saveData = (BMWorldSavedData) event.getWorld().getMapStorage().getOrLoadData(BMWorldSavedData.class, BMWorldSavedData.ID);
            WayofTime.bloodmagic.api.network.SoulNetwork oldData = (WayofTime.bloodmagic.api.network.SoulNetwork) event.getWorld().getMapStorage().getOrLoadData(WayofTime.bloodmagic.api.network.SoulNetwork.class, playerId.toString());

            if (saveData == null)
            {
                saveData = new BMWorldSavedData();
                event.getWorld().getMapStorage().setData(BMWorldSavedData.ID, saveData);
            }

            if (oldData == null)
                return;

            SoulNetwork network = saveData.getNetwork(playerId);
            if (oldData.getOrbTier() > network.getOrbTier())
                network.setOrbTier(oldData.getOrbTier());
            if (oldData.getCurrentEssence() > network.getCurrentEssence())
                network.setCurrentEssence(oldData.getCurrentEssence());

            File oldDataFile = event.getWorld().getSaveHandler().getMapFileFromName(playerId.toString());
            try
            {
                FileUtils.forceDelete(oldDataFile);
            } catch (IOException e)
            {
                BloodMagic.instance.getLogger().error("Error deleting data file {}.", oldDataFile);
                BloodMagic.instance.getLogger().error(e.getLocalizedMessage());
            }
            stopwatch.stop();
            BloodMagic.instance.getLogger().info("Migration completed for {} ({}) in {}.", player.getDisplayNameString(), playerId, stopwatch);
        }
    }
}
