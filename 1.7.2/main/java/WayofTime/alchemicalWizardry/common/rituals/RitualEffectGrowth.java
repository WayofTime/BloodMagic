package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectGrowth extends RitualEffect
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
            EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            if (world.getWorldTime() % 20 != 0)
            {
                return;
            }

            boolean flag = false;

            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    Block block = world.getBlock(x + i, y + 2, z + j);

                    if (block instanceof IPlantable)
                    {
                        {
                            SpellHelper.sendIndexedParticleToAllAround(world, x, y, z, 20, world.provider.dimensionId, 3, x, y, z);
                            block.updateTick(world, x + i, y + 2, z + j, world.rand);
                            flag = true;
                        }
                    }
                }
            }

            if (flag)
            {
                data.currentEssence = currentEssence - this.getCostPerRefresh();
                data.markDirty();
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 100;
    }

    @Override
	public List<RitualComponent> getRitualComponentList()
	{
		ArrayList<RitualComponent> growthRitual = new ArrayList();
        growthRitual.add(new RitualComponent(1, 0, 0, 1));
        growthRitual.add(new RitualComponent(-1, 0, 0, 1));
        growthRitual.add(new RitualComponent(0, 0, 1, 1));
        growthRitual.add(new RitualComponent(0, 0, -1, 1));
        growthRitual.add(new RitualComponent(-1, 0, 1, 3));
        growthRitual.add(new RitualComponent(1, 0, 1, 3));
        growthRitual.add(new RitualComponent(-1, 0, -1, 3));
        growthRitual.add(new RitualComponent(1, 0, -1, 3));
        return growthRitual;
	}
}
