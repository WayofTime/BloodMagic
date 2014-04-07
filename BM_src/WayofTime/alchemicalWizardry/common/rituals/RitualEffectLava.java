package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.common.network.PacketDispatcher;

public class RitualEffectLava extends RitualEffect
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
        World world = ritualStone.getWorldObj();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (world.isAirBlock(x, y + 1, z))
        {
            if (currentEssence < this.getCostPerRefresh())
            {
                EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

                if (entityOwner == null)
                {
                    return;
                }

                entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
            } else
            {
                for (int i = 0; i < 10; i++)
                {
                    PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(x, y, z, (short) 3));
                }

                world.setBlock(x, y + 1, z, Block.lavaMoving.blockID, 0, 3);
                data.currentEssence = currentEssence - this.getCostPerRefresh();
                data.markDirty();
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 500;
    }

	@Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> lavaRitual = new ArrayList();
        lavaRitual.add(new RitualComponent(1, 0, 0, 2));
        lavaRitual.add(new RitualComponent(-1, 0, 0, 2));
        lavaRitual.add(new RitualComponent(0, 0, 1, 2));
        lavaRitual.add(new RitualComponent(0, 0, -1, 2));
        return lavaRitual;
	}
}
