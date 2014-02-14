package WayofTime.alchemicalWizardry.common.rituals;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RitualEffectCrushing extends RitualEffect
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

        if (world.getWorldTime() % 40 != 0)
        {
            return;
        }

        int x = ritualStone.xCoord;
        int y = ritualStone.yCoord;
        int z = ritualStone.zCoord;
        TileEntity tile = world.getTileEntity(x, y + 1, z);
        IInventory tileEntity;

        if (tile instanceof IInventory)
        {
            tileEntity = (IInventory) tile;
        } else
        {
            return;
        }

        if (tileEntity.getSizeInventory() <= 0)
        {
            return;
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
            //boolean flag = false;
            for (int j = -3; j < 0; j++)
            {
                for (int i = -1; i <= 1; i++)
                {
                    for (int k = -1; k <= 1; k++)
                    {
                        Block block = world.getBlock(x + i, y + j, z + k);
                        int meta = world.getBlockMetadata(x + i, y + j, z + k);

                        if (block != null)
                        {
                            if ((block.equals(ModBlocks.ritualStone) || block.equals(ModBlocks.blockMasterStone)))
                            {
                                continue;
                            }

                            ArrayList<ItemStack> itemDropList = block.getDrops(world, x + i, y + j, z + k, meta, 0);

                            if (itemDropList != null)
                            {
                                int invSize = tileEntity.getSizeInventory();

                                for (ItemStack item : itemDropList)
                                {
                                    ItemStack copyStack = item.copyItemStack(item);

                                    for (int n = 0; n < invSize; n++)
                                    {
                                        if (tileEntity.isItemValidForSlot(n, copyStack) && copyStack.stackSize != 0)
                                        {
                                            ItemStack itemStack = tileEntity.getStackInSlot(n);

                                            if (itemStack == null)
                                            {
                                                tileEntity.setInventorySlotContents(n, copyStack);
                                                copyStack.stackSize = 0;
                                            } else
                                            {
                                                if (itemStack.getItem().equals(copyStack.getItem()))
                                                {
                                                    int itemSize = itemStack.stackSize;
                                                    int copySize = copyStack.stackSize;
                                                    int maxSize = itemStack.getMaxStackSize();

                                                    if (copySize + itemSize < maxSize)
                                                    {
                                                        copyStack.stackSize = 0;
                                                        itemStack.stackSize = itemSize + copySize;
                                                        tileEntity.setInventorySlotContents(n, itemStack);
                                                    } else
                                                    {
                                                        copyStack.stackSize = itemSize + copySize - maxSize;
                                                        itemStack.stackSize = maxSize;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (copyStack.stackSize > 0)
                                    {
                                        world.spawnEntityInWorld(new EntityItem(world, x + 0.4, y + 2, z + 0.5, copyStack));
                                        //flag=true;
                                    }
                                }
                            }

                            //if(flag)
                            world.setBlockToAir(x + i, y + j, z + k);
                            world.playSoundEffect(x + i, y + j, z + k, "mob.endermen.portal", 1.0F, 1.0F);
                            data.currentEssence = currentEssence - this.getCostPerRefresh();
                            data.markDirty();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 7;
    }
}
