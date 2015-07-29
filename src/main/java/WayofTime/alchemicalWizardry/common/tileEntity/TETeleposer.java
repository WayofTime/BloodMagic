package WayofTime.alchemicalWizardry.common.tileEntity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.items.TelepositionFocus;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class TETeleposer extends TEInventory implements IUpdatePlayerListBox
{
	public static final int sizeInv = 1;
	
    private int resultID;
    private int resultDamage;
    private int previousInput;

    private boolean isActive;

    public TETeleposer()
    {
        super(sizeInv);
        resultID = 0;
        resultDamage = 0;
        isActive = false;
        previousInput = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        resultID = par1NBTTagCompound.getInteger("resultID");
        resultDamage = par1NBTTagCompound.getInteger("resultDamage");
        isActive = par1NBTTagCompound.getBoolean("isActive");
        previousInput = par1NBTTagCompound.getInteger("previousInput");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("resultID", resultID);
        par1NBTTagCompound.setInteger("resultDamage", resultDamage);
        par1NBTTagCompound.setBoolean("isActive", isActive);
        par1NBTTagCompound.setInteger("previousInput", previousInput);
    }

    @Override
    public String getName()
    {
        return "TETeleposer";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    //Logic for the actual block is under here
    @Override
    public void update()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        int currentInput = worldObj.getStrongPower(pos);

        if (previousInput == 0 && currentInput != 0)
        {
            ItemStack focus = this.getStackInSlot(0);

            if (focus != null && focus.getItem() instanceof TelepositionFocus)
            {
                TelepositionFocus focusItem = (TelepositionFocus) (focus.getItem());
                int xf = focusItem.xCoord(focus);
                int yf = focusItem.yCoord(focus);
                int zf = focusItem.zCoord(focus);
                BlockPos posF = focusItem.getBlockPos(focus);
                
                World worldF = focusItem.getWorld(focus);
                int damage = (int) (0.5f * Math.sqrt(pos.distanceSq(posF)));
                int focusLevel = focusItem.getFocusLevel();
                int transportCount = 0;
                int entityCount = 0;

                if (worldF != null && worldF.getTileEntity(posF) instanceof TETeleposer && !worldF.getTileEntity(posF).equals(this))
                {
                    //Prime the teleportation
                    int d0 = focusLevel - 1;
                    AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(pos.add(0, 1, 0), pos.add(1, 2, 1)).expand(d0, d0, d0);
//                    axisalignedbb1.maxY = Math.min((double) this.worldObj.getHeight(), pos.getY() + 2 + d0 + d0);
                    List list1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb1);
                    Iterator iterator1 = list1.iterator();
                    EntityLivingBase entityplayer1;

                    while (iterator1.hasNext())
                    {
                        entityplayer1 = (EntityLivingBase) iterator1.next();
                        entityCount++;
                    }

                    AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(xf-0.5, yf + d0 + 0.5, zf-0.5, xf + 0.5, yf + 1.5 + d0, zf+0.5).expand(d0, d0, d0);
                    List list2 = worldF.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb2);
                    Iterator iterator2 = list2.iterator();
                    EntityLivingBase entityplayer2;

                    while (iterator2.hasNext())
                    {
                        entityplayer2 = (EntityLivingBase) iterator2.next();
                        entityCount++;
                    }

                    if (EnergyItems.canSyphonInContainer(focus, damage * (focusLevel * 2 - 1) * (focusLevel * 2 - 1) * (focusLevel * 2 - 1) + damage * entityCount))
                    {
                        for (int k = 0; k <= (focusLevel * 2 - 2); k++)
                            for (int i = -(focusLevel - 1); i <= (focusLevel - 1); i++)
                            {
                                for (int j = -(focusLevel - 1); j <= (focusLevel - 1); j++)
                                {
                                    {
                                        if (BlockTeleposer.swapBlocks(this, worldObj, worldF, pos.add(i, 1 + k, j), posF.add(i, 1 + k, j)))
                                        {
                                            transportCount++;
                                        }
                                    }
                                }
                            }

                        if (!worldF.equals(worldObj))
                        {
                            entityCount = 0;
                        }

                        EnergyItems.syphonWhileInContainer(focus, damage * transportCount + damage * entityCount);
                        //Teleport

                        if (worldF.equals(worldObj))
                        {
                            iterator1 = list1.iterator();
                            iterator2 = list2.iterator();

                            while (iterator1.hasNext())
                            {
                                entityplayer1 = (EntityLivingBase) iterator1.next();
                                entityplayer1.worldObj = worldF;
                                entityplayer1.setPositionAndUpdate(entityplayer1.posX - pos.getX() + xf, entityplayer1.posY - pos.getY() + yf, entityplayer1.posZ - pos.getZ() + zf);
                            }

                            while (iterator2.hasNext())
                            {
                                entityplayer2 = (EntityLivingBase) iterator2.next();
                                entityplayer2.worldObj = worldF;
                                entityplayer2.setPositionAndUpdate(entityplayer2.posX + pos.getX() - xf, entityplayer2.posY + pos.getY() - yf, entityplayer2.posZ + pos.getZ() - zf);
                            }
                        }else
                        {
                        	iterator1 = list1.iterator();
                            iterator2 = list2.iterator();

                            while (iterator1.hasNext())
                            {
                                entityplayer1 = (EntityLivingBase) iterator1.next();
                            	SpellHelper.teleportEntityToDim(worldObj, worldF.provider.getDimensionId(), entityplayer1.posX - pos.getX() + xf, entityplayer1.posY - pos.getY() + yf, entityplayer1.posZ - pos.getZ() + zf, entityplayer1);
//                                entityplayer1.worldObj = worldF;
//                                entityplayer1.setPositionAndUpdate(entityplayer1.posX - pos.getX() + xf, entityplayer1.posY - pos.getY() + yf, entityplayer1.posZ - pos.getZ() + zf);
                            }

                            while (iterator2.hasNext())
                            {
                                entityplayer2 = (EntityLivingBase) iterator2.next();
                                SpellHelper.teleportEntityToDim(worldF, worldObj.provider.getDimensionId(), entityplayer2.posX + pos.getX() - xf, entityplayer2.posY + pos.getY() - yf, entityplayer2.posZ + pos.getZ() - zf, entityplayer2);
//                                entityplayer2.worldObj = worldF;
//                                entityplayer2.setPositionAndUpdate(entityplayer2.posX + pos.getX() - xf, entityplayer2.posY + pos.getY() - yf, entityplayer2.posZ + pos.getZ() - zf);
                            }
                        }
                    }
                }
            }
        }

        previousInput = currentInput;
    }

    public void setActive()
    {
        isActive = false;
    }

    public boolean isActive()
    {
        return isActive;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return NewPacketHandler.getPacket(this);
    }

    public void handlePacketData(int[] intData)
    {
        if (intData == null)
        {
            return;
        }

        if (intData.length == 3)
        {
            for (int i = 0; i < 1; i++)
            {
                if (intData[i * 3 + 2] != 0)
                {
                    ItemStack is = new ItemStack(Item.getItemById(intData[i * 3]), intData[i * 3 + 2], intData[i * 3 + 1]);
                    inv[i] = is;
                } else
                {
                    inv[i] = null;
                }
            }
        }
    }

    public int[] buildIntDataList()
    {
        int[] sortList = new int[3]; //1 * 3
        int pos = 0;

        for (ItemStack is : inv)
        {
            if (is != null)
            {
                sortList[pos++] = Item.getIdFromItem(is.getItem());
                sortList[pos++] = is.getItemDamage();
                sortList[pos++] = is.stackSize;
            } else
            {
                sortList[pos++] = 0;
                sortList[pos++] = 0;
                sortList[pos++] = 0;
            }
        }

        return sortList;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return itemstack.getItem() instanceof TelepositionFocus;
    }
}
