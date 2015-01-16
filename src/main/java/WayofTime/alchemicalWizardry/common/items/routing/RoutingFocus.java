package WayofTime.alchemicalWizardry.common.items.routing;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;

public class RoutingFocus extends Item
{
	public RoutingFocus()
	{
		super();
        this.maxStackSize = 1;
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
		this.cycleDirection(itemStack);
		return itemStack;
    }
	
	public void cycleDirection(ItemStack itemStack)
	{
		ForgeDirection dir = this.getSetDirection(itemStack);
		int direction = dir.ordinal();
		direction++;
		if(direction >= ForgeDirection.VALID_DIRECTIONS.length)
		{
			direction = 0;
		}
		
		this.setSetDirection(itemStack, ForgeDirection.getOrientation(direction));
	}
	
	public ForgeDirection getSetDirection(ItemStack itemStack)
	{
		if(!itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound tag = itemStack.getTagCompound();
		
		return ForgeDirection.getOrientation(tag.getInteger("direction"));
	}
	
	public void setSetDirection(ItemStack itemStack, ForgeDirection dir)
	{
		if(!itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound tag = itemStack.getTagCompound();
		
		tag.setInteger("direction", dir.ordinal());
	}
	
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("An Enderpearl imbued with blood");

        if (!(par1ItemStack.getTagCompound() == null))
        {
            NBTTagCompound itemTag = par1ItemStack.getTagCompound();

            par3List.add("Coords: " + itemTag.getInteger("xCoord") + ", " + itemTag.getInteger("yCoord") + ", " + itemTag.getInteger("zCoord"));
            par3List.add("Direction: " + this.getSetDirection(par1ItemStack));
        }
    }
    
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof IInventory)
		{
			this.setCoordinates(stack, x, y, z);
			
			return true;
		}
		
		return false;
	}
	
	public void setCoordinates(ItemStack itemStack, int x, int y, int z)
	{
		if(!itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound tag = itemStack.getTagCompound();
		
		tag.setInteger("xCoord", x);
		tag.setInteger("yCoord", y);
		tag.setInteger("zCoord", z);
	}

    public int xCoord(ItemStack itemStack)
    {
        if (!(itemStack.getTagCompound() == null))
        {
            return itemStack.getTagCompound().getInteger("xCoord");
        } else
        {
            return 0;
        }
    }

    public int yCoord(ItemStack itemStack)
    {
        if (!(itemStack.getTagCompound() == null))
        {
            return itemStack.getTagCompound().getInteger("yCoord");
        } else
        {
            return 0;
        }
    }

    public int zCoord(ItemStack itemStack)
    {
        if (!(itemStack.getTagCompound() == null))
        {
            return itemStack.getTagCompound().getInteger("zCoord");
        } else
        {
            return 0;
        }
    }
}
