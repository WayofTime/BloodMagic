package WayofTime.alchemicalWizardry.common.rituals;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralContainer;

public class RitualEffectSupression extends RitualEffect
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
            int radius = 10;

            for (int i = -radius; i <= radius; i++)
            {
                for (int j = -radius; j <= radius; j++)
                {
                    for(int k = -radius; k <= radius; k++)
                    {
                    	if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                        {
                            continue;
                        }
                    	
                    	Block block = Block.blocksList[world.getBlockId(x+i, y+j, z+k)];
                    	
                    	
                    	if(SpellHelper.isBlockFluid(block))
                    	{
                    		TESpectralContainer.createSpectralBlockAtLocation(world, x+i, y+j, z+k, 3);
                    	}
                    	else
                    	{
                    		TileEntity tile = world.getBlockTileEntity(x+i, y+j, z+k);
                    		if(tile instanceof TESpectralContainer)
                    		{
                    			((TESpectralContainer) tile).resetDuration(3);
                    		}
                    	}		
                    }
                }
            }

            
            data.currentEssence = currentEssence - this.getCostPerRefresh();
            data.markDirty();
            
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 1;
    }
}
