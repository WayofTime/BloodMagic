package WayofTime.alchemicalWizardry.common.demonVillage.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;

public class TEDemonChest extends TileEntityChest implements ITilePortalNode
{
	public BlockPos portalLocation = BlockPos.ORIGIN;
	
	@Override
	public String getName()
    {
        return "Demon's Chest";
    }
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		NBTTagCompound portalTag = tag.getCompoundTag("portalLocation");
		portalLocation = new BlockPos(portalTag.getInteger("xCoord"), portalTag.getInteger("yCoord"), portalTag.getInteger("zCoord"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		NBTTagCompound portalTag = new NBTTagCompound();
		portalTag.setInteger("xCoord", portalLocation.getX());
		portalTag.setInteger("yCoord", portalLocation.getY());
		portalTag.setInteger("zCoord", portalLocation.getZ());
		tag.setTag("portalLocation", portalTag);
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
		super.openInventory(player);
		this.notifyPortalOfInteraction();
	}
	
	@Override
	public void checkForAdjacentChests() {}

	@Override
	public void setPortalLocation(TEDemonPortal teDemonPortal) 
	{
		if(teDemonPortal != null)
		{
			portalLocation = teDemonPortal.getPos();
		}
	}
	
	public TEDemonPortal getDemonPortal()
	{
		TileEntity tile = worldObj.getTileEntity(portalLocation);
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
		
		portal.notifyDemons(pos.getX(), pos.getY(), pos.getZ(), 50);
	}
}
