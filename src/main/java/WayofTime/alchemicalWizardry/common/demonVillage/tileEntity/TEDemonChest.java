package WayofTime.alchemicalWizardry.common.demonVillage.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import WayofTime.alchemicalWizardry.common.Int3;

public class TEDemonChest extends TileEntityChest implements ITilePortalNode
{
	public Int3 portalLocation = new Int3(0,0,0);
	
	@Override
	public String getInventoryName()
    {
        return "Demon's Chest";
    }
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		NBTTagCompound portalTag = tag.getCompoundTag("portalLocation");
		portalLocation = Int3.readFromNBT(portalTag);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		NBTTagCompound portalTag = portalLocation.writeToNBT(new NBTTagCompound());
		tag.setTag("portalLocation", portalTag);
	}
	
	@Override
	public void checkForAdjacentChests()
	{
		return;
	}

	@Override
	public void setPortalLocation(TEDemonPortal teDemonPortal) 
	{
		if(teDemonPortal != null)
		{
			portalLocation = new Int3(teDemonPortal.xCoord, teDemonPortal.yCoord, teDemonPortal.zCoord);
		}
	}
	
	public TEDemonPortal getDemonPortal()
	{
		TileEntity tile = worldObj.getTileEntity(portalLocation.xCoord, portalLocation.yCoord, portalLocation.zCoord);
		if(tile instanceof TEDemonPortal)
		{
			return (TEDemonPortal)tile;
		}
		return null;
	}
	
	public void notifyPortalOfInteraction()
	{
		TEDemonPortal portal = this.getDemonPortal();
		if(portal == null)
		{
			return;
		}
		
		portal.notifyDemons(xCoord, yCoord, zCoord, 25);
	}
}
