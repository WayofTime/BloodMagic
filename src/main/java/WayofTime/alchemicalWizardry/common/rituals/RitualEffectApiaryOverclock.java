package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualEffectApiaryOverclock extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();


        if (currentEssence < this.getCostPerRefresh())
        {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 10;
    }

    @Override
    public List<RitualComponent> getRitualComponentList()
    {
        ArrayList<RitualComponent> apiaryRitual = new ArrayList();
        apiaryRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(1, 0, 1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(1, 0, -1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
        apiaryRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        return apiaryRitual;
    }
}
