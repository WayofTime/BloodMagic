package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class TEBelljar extends TEReagentConduit
{
    public TEBelljar()
    {
        super(1, 16000);
        this.maxConnextions = 1;
        this.affectedByRedstone = false;
    }

    public int getRSPowerOutput()
    {
        ReagentContainer thisTank = this.tanks[0];
        if (thisTank != null)
        {
            ReagentStack stack = thisTank.getReagent();
            if (stack != null)
            {
                return (15 * stack.amount / thisTank.getCapacity());
            }
        }
        return 0;
    }

    public static ReagentContainerInfo[] getContainerInfoFromItem(ItemStack stack)
    {
        if (stack != null && stack.getItem() instanceof ItemBlock && ModBlocks.blockCrystalBelljar == ((ItemBlock) stack.getItem()).field_150939_a)
        {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null)
            {
                NBTTagList tagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);

                int size = tagList.tagCount();
                ReagentContainer[] tanks = new ReagentContainer[size];

                ReagentContainerInfo[] infos = new ReagentContainerInfo[size];

                for (int i = 0; i < size; i++)
                {
                    NBTTagCompound savedTag = tagList.getCompoundTagAt(i);
                    tanks[i] = ReagentContainer.readFromNBT(savedTag);

                    if (tanks[i] != null)
                    {
                        infos[i] = tanks[i].getInfo();
                    }
                }

                return infos;
            }
        }

        return new ReagentContainerInfo[0];
    }

    public void readTankNBTOnPlace(NBTTagCompound tag)
    {
        NBTTagList tagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);

        int size = tagList.tagCount();
        this.tanks = new ReagentContainer[size];

        for (int i = 0; i < size; i++)
        {
            NBTTagCompound savedTag = tagList.getCompoundTagAt(i);
            this.tanks[i] = ReagentContainer.readFromNBT(savedTag);
        }
    }

    public void writeTankNBT(NBTTagCompound tag)
    {
        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < this.tanks.length; i++)
        {
            NBTTagCompound savedTag = new NBTTagCompound();
            if (this.tanks[i] != null)
            {
                this.tanks[i].writeToNBT(savedTag);
            }
            tagList.appendTag(savedTag);
        }

        tag.setTag("reagentTanks", tagList);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (hasChanged == 1)
        {
            Block block = worldObj.getBlock(xCoord + 1, yCoord, zCoord);
            block.onNeighborBlockChange(worldObj, xCoord + 1, yCoord, zCoord, block);
            block = worldObj.getBlock(xCoord - 1, yCoord, zCoord);
            block.onNeighborBlockChange(worldObj, xCoord - 1, yCoord, zCoord, block);
            block = worldObj.getBlock(xCoord, yCoord + 1, zCoord);
            block.onNeighborBlockChange(worldObj, xCoord, yCoord + 1, zCoord, block);
            block = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
            block.onNeighborBlockChange(worldObj, xCoord, yCoord - 1, zCoord, block);
            block = worldObj.getBlock(xCoord, yCoord, zCoord + 1);
            block.onNeighborBlockChange(worldObj, xCoord, yCoord, zCoord + 1, block);
            block = worldObj.getBlock(xCoord, yCoord, zCoord - 1);
            block.onNeighborBlockChange(worldObj, xCoord, yCoord, zCoord - 1, block);
        }
    }
}
