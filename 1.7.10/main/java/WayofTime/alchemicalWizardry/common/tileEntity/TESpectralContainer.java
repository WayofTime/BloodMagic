package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.IFluidBlock;
import WayofTime.alchemicalWizardry.ModBlocks;

public class TESpectralContainer extends TileEntity
{
    private ItemStack[] inv;
    
    private int ticksRemaining;

    public TESpectralContainer()
    {
        this.inv = new ItemStack[1];

        ticksRemaining = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList tagList = par1NBTTagCompound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
            int slot = tag.getByte("Slot");

            if (slot >= 0 && slot < inv.length)
            {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        ticksRemaining = par1NBTTagCompound.getInteger("ticksRemaining");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inv.length; i++)
        {
            ItemStack stack = inv[i];

            if (inv[i] != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                inv[i].writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        par1NBTTagCompound.setTag("Inventory", itemList);
        par1NBTTagCompound.setInteger("ticksRemaining", ticksRemaining); 
    }
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        this.ticksRemaining--;
        
        if(this.ticksRemaining<=0)
        {
        	this.returnContainedBlock();
        }
    }   
    
    public static boolean createSpectralBlockAtLocation(World world, int x, int y, int z, int duration)
    {
    	Block block = world.getBlock(x, y, z);
    	
    	if(block==null)
		{
			return false;
		}
    	
    	if(world.getTileEntity(x, y, z)==null || block instanceof IFluidBlock)
    	{
    		int meta = world.getBlockMetadata(x, y, z);
    		ItemStack item = new ItemStack(block, 1, meta);
    		
    		world.setBlock(x, y, z, ModBlocks.blockSpectralContainer);
    		TileEntity tile = world.getTileEntity(x, y, z);
    		if(tile instanceof TESpectralContainer)
    		{
    			((TESpectralContainer) tile).setContainedItem(item);
    			((TESpectralContainer) tile).setDuration(duration);
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public void setDuration(int dur)
    {
    	this.ticksRemaining = dur;
    }
    
    public void resetDuration(int dur)
    {
    	if(this.ticksRemaining<dur)
    	{
    		this.ticksRemaining = dur;
    	}
    }
    
    public void setContainedItem(ItemStack item)
    {
    	this.inv[0] = item;
    }
    
    public void returnContainedBlock()
    {
    	ItemStack item = this.inv[0];
    	if(item!=null)
    	{
    		if(item.getItem() instanceof ItemBlock)
    		{
    			Block block = ((ItemBlock)item.getItem()).field_150939_a;
    			int meta = item.getItemDamage();
            	
            	if(block != null)
            	{
            		this.worldObj.setBlock(xCoord, yCoord, zCoord, block, meta, 6);
            	}
    		}
 	
    	}else
    	{
    		this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
    	}
    }
}
