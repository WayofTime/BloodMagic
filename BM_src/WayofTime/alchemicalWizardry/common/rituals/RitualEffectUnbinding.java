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
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.items.BoundArmour;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.common.network.PacketDispatcher;

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

                if (itemStack.itemID == ModItems.boundHelmet.itemID)
                {
                    ritualStone.setVar1(5);
                } else if (itemStack.itemID == ModItems.boundPlate.itemID)
                {
                    ritualStone.setVar1(8);
                } else if (itemStack.itemID == ModItems.boundLeggings.itemID)
                {
                    ritualStone.setVar1(7);
                } else if (itemStack.itemID == ModItems.boundBoots.itemID)
                {
                    ritualStone.setVar1(4);
                } else if (itemStack.itemID == ModItems.sigilOfHolding.itemID)
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

                    EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, new ItemStack(Block.blocksList[ModBlocks.bloodSocket.blockID], ritualStone.getVar1()));
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

                    EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, new ItemStack(ModItems.sigilOfHolding.itemID, 1, 0));
                    world.spawnEntityInWorld(newItem);
                    ritualStone.setActive(false);
                    break;
                }

                if (world.rand.nextInt(10) == 0)
                {
                    PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(item.posX, item.posY, item.posZ, (short) 1));
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
