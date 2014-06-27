package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.items.BoundArmour;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectUnbinding extends RitualEffect
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
        } else
        {
            int d0 = 0;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double) x, (double) y + 1, (double) z, (double) (x + 1), (double) (y + 2), (double) (z + 1)).expand(d0, d0, d0);
            List list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
            Iterator iterator = list.iterator();
            EntityItem item;

            while (iterator.hasNext())
            {
                item = (EntityItem) iterator.next();
//                double xDif = item.posX - (xCoord+0.5);
//                double yDif = item.posY - (yCoord+1);
//                double zDif = item.posZ - (zCoord+0.5);
                ItemStack itemStack = item.getEntityItem();

                if (itemStack == null)
                {
                    continue;
                }

                if (itemStack.getItem() == ModItems.boundHelmet)
                {
                    ritualStone.setVar1(5);
                } else if (itemStack.getItem() == ModItems.boundPlate)
                {
                    ritualStone.setVar1(8);
                } else if (itemStack.getItem() == ModItems.boundLeggings)
                {
                    ritualStone.setVar1(7);
                } else if (itemStack.getItem() == ModItems.boundBoots)
                {
                    ritualStone.setVar1(4);
                } else if (itemStack.getItem() == ModItems.sigilOfHolding)
                {
                    ritualStone.setVar1(-1);
                }

                if (ritualStone.getVar1() > 0)
                {
                    item.setDead();
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z - 5));
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z + 5));
                    world.addWeatherEffect(new EntityLightningBolt(world, x - 5, y + 1, z));
                    world.addWeatherEffect(new EntityLightningBolt(world, x + 5, y + 1, z));
                    NBTTagCompound itemTag = itemStack.stackTagCompound;
                    ItemStack[] inv = ((BoundArmour) itemStack.getItem()).getInternalInventory(itemStack);

                    if (inv != null)
                    {
                        for (ItemStack internalItem : inv)
                        {
                            if (internalItem != null)
                            {
                                EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, internalItem.copy());
                                world.spawnEntityInWorld(newItem);
                            }
                        }
                    }

                    EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, new ItemStack(ModBlocks.bloodSocket, ritualStone.getVar1()));
                    world.spawnEntityInWorld(newItem);
                    ritualStone.setActive(false);
                    break;
                } else if (ritualStone.getVar1() == -1)
                {
                    item.setDead();
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z - 5));
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z + 5));
                    world.addWeatherEffect(new EntityLightningBolt(world, x - 5, y + 1, z));
                    world.addWeatherEffect(new EntityLightningBolt(world, x + 5, y + 1, z));
                    NBTTagCompound itemTag = itemStack.stackTagCompound;
                    ItemStack[] inv = ((SigilOfHolding) itemStack.getItem()).getInternalInventory(itemStack);

                    if (inv != null)
                    {
                        for (ItemStack internalItem : inv)
                        {
                            if (internalItem != null)
                            {
                                EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, internalItem.copy());
                                world.spawnEntityInWorld(newItem);
                            }
                        }
                    }

                    EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, new ItemStack(ModItems.sigilOfHolding, 1, 0));
                    world.spawnEntityInWorld(newItem);
                    ritualStone.setActive(false);
                    break;
                }

                if (world.rand.nextInt(10) == 0)
                {
                    SpellHelper.sendIndexedParticleToAllAround(world, x, y, z, 20, world.provider.dimensionId, 1, x, y, z);
                }
            }

            data.currentEssence = currentEssence - this.getCostPerRefresh();
            data.markDirty();
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> unbindingRitual = new ArrayList();
        unbindingRitual.add(new RitualComponent(-2, 0, 0, 4));
        unbindingRitual.add(new RitualComponent(2, 0, 0, 4));
        unbindingRitual.add(new RitualComponent(0, 0, 2, 4));
        unbindingRitual.add(new RitualComponent(0, 0, -2, 4));
        unbindingRitual.add(new RitualComponent(-2, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(-2, 0, -3, 3));
        unbindingRitual.add(new RitualComponent(-3, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, -3, 3));
        unbindingRitual.add(new RitualComponent(3, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(-2, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(-2, 0, 3, 3));
        unbindingRitual.add(new RitualComponent(-3, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, 3, 3));
        unbindingRitual.add(new RitualComponent(3, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(3, 1, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 1, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 1, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 1, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 2, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 2, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 2, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 2, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 3, 3, 2));
        unbindingRitual.add(new RitualComponent(3, 3, -3, 2));
        unbindingRitual.add(new RitualComponent(-3, 3, -3, 2));
        unbindingRitual.add(new RitualComponent(-3, 3, 3, 2));
        unbindingRitual.add(new RitualComponent(-5, 0, 0, 2));
        unbindingRitual.add(new RitualComponent(5, 0, 0, 2));
        unbindingRitual.add(new RitualComponent(0, 0, 5, 2));
        unbindingRitual.add(new RitualComponent(0, 0, -5, 2));
        return unbindingRitual;
	}
}
