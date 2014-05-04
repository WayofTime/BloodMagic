package WayofTime.alchemicalWizardry.common.rituals;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public class RitualEffectSummonPlayer extends RitualEffect //Summons a player via the bound item
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

        if (ritualStone.getCooldown() > 0)
        {
            ritualStone.setCooldown(0);
        }

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
            List<EntityItem> entities = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1));

            if (entities == null)
            {
                return;
            }

            for (EntityItem entityItem : entities)
            {
                if (entityItem != null && entityItem.getEntityItem().getItem() instanceof IBindable)
                {
                    String str = EnergyItems.getOwnerName(entityItem.getEntityItem());
                    
                    EntityPlayer entityPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(str);
                    if(entityPlayer!=null)
                    {
                    	entityPlayer.posX = x;
                    	entityPlayer.posY = y+1;
                    	entityPlayer.posZ = z;
                    	
                    	if(entityPlayer instanceof EntityPlayerMP)
                    	{
                    		((EntityPlayerMP)entityPlayer).mcServer.getConfigurationManager().transferPlayerToDimension(((EntityPlayerMP)entityPlayer), 0);
                    	}
                    	
                    	entityItem.setDead();
                    	
                    	data.currentEssence = currentEssence - this.getCostPerRefresh();
                        data.markDirty();
                    }
                    break;
                }
            }

//        	EnergyBlastProjectile proj = new EnergyBlastProjectile(world, x, y+20, z);
//        	proj.motionX = 0.0d;
//        	proj.motionZ = 0.0d;
//        	proj.motionY = -1.0d;
//        	world.spawnEntityInWorld(proj);
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

	@Override
	public List<RitualComponent> getRitualComponentList() {
		// TODO Auto-generated method stub
		return null;
	}
}
