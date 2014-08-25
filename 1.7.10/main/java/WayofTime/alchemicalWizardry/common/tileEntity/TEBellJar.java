package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;

public class TEBellJar extends TEReagentConduit 
{	
	public TEBellJar()
	{
		super(1, 16000);
		this.maxConnextions = 1;
		this.affectedByRedstone = false;
	}
	
	public int getRSPowerOutput()
	{
		ReagentContainer thisTank = this.tanks[0];
		if(thisTank != null)
		{
			ReagentStack stack = thisTank.getReagent();
			if(stack != null)
			{
				return (15*stack.amount/thisTank.getCapacity());
			}
		}
		return 0;
	}
	
//	@Override
//	public void readClientNBT(NBTTagCompound tag)
//	{
//		super.readClientNBT(tag);
//		
//		NBTTagList tagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);
//        
//        int size = tagList.tagCount();
//        this.tanks = new ReagentContainer[size];
//        
//        for(int i=0; i<size; i++)
//        {
//        	NBTTagCompound savedTag = tagList.getCompoundTagAt(i);
//        	this.tanks[i] = ReagentContainer.readFromNBT(savedTag);
//        }
//	}
//	
//	@Override
//	public void writeClientNBT(NBTTagCompound tag)
//	{
//		super.writeClientNBT(tag);
//		
//		NBTTagList tagList = new NBTTagList();
//        
//        for(int i=0; i<this.tanks.length; i++)
//        {
//        	NBTTagCompound savedTag = new NBTTagCompound();
//        	if(this.tanks[i] != null)
//        	{
//        		this.tanks[i].writeToNBT(savedTag);
//        	}
//        	tagList.appendTag(savedTag);
//        }
//        
//        tag.setTag("reagentTanks", tagList);
//	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if(hasChanged == 1)
		{
			Block block = worldObj.getBlock(xCoord+1, yCoord, zCoord);
			block.onNeighborBlockChange(worldObj, xCoord+1, yCoord, zCoord, block);
			block = worldObj.getBlock(xCoord-1, yCoord, zCoord);
			block.onNeighborBlockChange(worldObj, xCoord-1, yCoord, zCoord, block);
			block = worldObj.getBlock(xCoord, yCoord+1, zCoord);
			block.onNeighborBlockChange(worldObj, xCoord, yCoord+1, zCoord, block);
			block = worldObj.getBlock(xCoord, yCoord-1, zCoord);
			block.onNeighborBlockChange(worldObj, xCoord, yCoord-1, zCoord, block);
			block = worldObj.getBlock(xCoord, yCoord, zCoord+1);
			block.onNeighborBlockChange(worldObj, xCoord, yCoord, zCoord+1, block);
			block = worldObj.getBlock(xCoord, yCoord, zCoord-1);
			block.onNeighborBlockChange(worldObj, xCoord, yCoord, zCoord-1, block);
		}
	}
}
