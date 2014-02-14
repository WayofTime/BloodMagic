package WayofTime.alchemicalWizardry.common.rituals;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
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
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.items.BoundArmour;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class RitualEffectUnbinding extends RitualEffect
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
        World world = ritualStone.getWorldObj();
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
            int d0 = 0;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) x, (double) y + 1, (double) z, (double) (x + 1), (double) (y + 2), (double) (z + 1)).expand(d0, d0, d0);
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
}
