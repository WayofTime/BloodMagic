package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import thaumcraft.api.ItemApi;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.common.network.PacketDispatcher;

public class RitualEffectSoulBound extends RitualEffect
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
            if (ritualStone.getVar1() == 0)
            {
                int d0 = 0;
                AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) x, (double) y + 1, (double) z, (double) (x + 1), (double) (y + 2), (double) (z + 1)).expand(d0, d0, d0);
                List list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
                Iterator iterator = list.iterator();
                EntityItem item;

                while (iterator.hasNext())
                {
                    item = (EntityItem) iterator.next();

                    ItemStack itemStack = item.getEntityItem();

                    if (itemStack == null)
                    {
                        continue;
                    }

                    if(BindingRegistry.isRequiredItemValid(itemStack))
                    {
                    	ritualStone.setVar1(BindingRegistry.getIndexForItem(itemStack)+1);
                    	world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                        ritualStone.setCooldown(ritualStone.getCooldown() - 1);
                    	item.setDead();
                    	break;
                    }

                    if (world.rand.nextInt(10) == 0)
                    {
                        PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(item.posX, item.posY, item.posZ, (short) 1));
                    }
                }

                data.currentEssence = currentEssence - this.getCostPerRefresh();
                data.markDirty();
            } else
            {
                ritualStone.setCooldown(ritualStone.getCooldown() - 1);

                if (world.rand.nextInt(20) == 0)
                {
                    int lightningPoint = world.rand.nextInt(8);

                    switch (lightningPoint)
                    {
                        case 0:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 3, z + 0));
                            break;

                        case 1:
                            world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 3, z + 0));
                            break;

                        case 2:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 0, y + 3, z + 4));
                            break;

                        case 3:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 0, y + 3, z - 4));
                            break;

                        case 4:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 3, y + 3, z + 3));
                            break;

                        case 5:
                            world.addWeatherEffect(new EntityLightningBolt(world, x - 3, y + 3, z + 3));
                            break;

                        case 6:
                            world.addWeatherEffect(new EntityLightningBolt(world, x + 3, y + 3, z - 3));
                            break;

                        case 7:
                            world.addWeatherEffect(new EntityLightningBolt(world, x - 3, y + 3, z - 3));
                            break;
                    }
                }

                if (ritualStone.getCooldown() <= 0)
                {
                	
                    ItemStack spawnedItem = BindingRegistry.getOutputForIndex(ritualStone.getVar1()-1);

                    if (spawnedItem != null)
                    {
                        EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, spawnedItem);
                        world.spawnEntityInWorld(newItem);
                    }

                    ritualStone.setActive(false);
                }
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 0;
    }

    @Override
    public int getInitialCooldown()
    {
        return 200;
    }

	@Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> boundSoulRitual = new ArrayList();
        boundSoulRitual.add(new RitualComponent(3, 0, 0, 2));
        boundSoulRitual.add(new RitualComponent(-3, 0, 0, 2));
        boundSoulRitual.add(new RitualComponent(0, 0, 3, 2));
        boundSoulRitual.add(new RitualComponent(0, 0, -3, 2));
        boundSoulRitual.add(new RitualComponent(2, 0, 2, 4));
        boundSoulRitual.add(new RitualComponent(-2, 0, 2, 4));
        boundSoulRitual.add(new RitualComponent(2, 0, -2, 4));
        boundSoulRitual.add(new RitualComponent(-2, 0, -2, 4));
        boundSoulRitual.add(new RitualComponent(4, 2, 0, 1));
        boundSoulRitual.add(new RitualComponent(-4, 2, 0, 1));
        boundSoulRitual.add(new RitualComponent(0, 2, 4, 1));
        boundSoulRitual.add(new RitualComponent(0, 2, -4, 1));
        boundSoulRitual.add(new RitualComponent(3, 2, 3, 3));
        boundSoulRitual.add(new RitualComponent(3, 2, -3, 3));
        boundSoulRitual.add(new RitualComponent(-3, 2, 3, 3));
        boundSoulRitual.add(new RitualComponent(-3, 2, -3, 3));
        boundSoulRitual.add(new RitualComponent(4, 1, 0, 0));
        boundSoulRitual.add(new RitualComponent(-4, 1, 0, 0));
        boundSoulRitual.add(new RitualComponent(0, 1, 4, 0));
        boundSoulRitual.add(new RitualComponent(0, 1, -4, 0));
        boundSoulRitual.add(new RitualComponent(3, 1, 3, 0));
        boundSoulRitual.add(new RitualComponent(3, 1, -3, 0));
        boundSoulRitual.add(new RitualComponent(-3, 1, 3, 0));
        boundSoulRitual.add(new RitualComponent(-3, 1, -3, 0));
        return boundSoulRitual;
	}
}
