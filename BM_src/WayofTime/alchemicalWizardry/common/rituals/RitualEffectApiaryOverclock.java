package WayofTime.alchemicalWizardry.common.rituals;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeekeepingLogic;

public class RitualEffectApiaryOverclock extends RitualEffect
{
    @Override
    public void performEffect(TEMasterStone ritualStone)
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
        World world = ritualStone.worldObj;
        int x = ritualStone.xCoord;
        int y = ritualStone.yCoord;
        int z = ritualStone.zCoord;

        
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
            TileEntity tile = world.getBlockTileEntity(x, y+1, z);
            
            try{
            	if(tile instanceof IBeeHousing && tile.getClass().getName().contains("Apiary"))
                {
                	for (int i = 0; i < 10; i++)
                    {
                        PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(x, y+1, z, (short) 3));
                    }
                	
                	for(int i=0; i<9; i++)
                	{
                		tile.updateEntity();
                	}
                	
                	data.currentEssence = currentEssence - this.getCostPerRefresh();
                    data.markDirty();
                }
            }catch (Exception e)
            {
            	
            }
        
            
        }
        
    }

    @Override
    public int getCostPerRefresh()
    {
        // TODO Auto-generated method stub
        return 10;
    }
}
