package WayofTime.alchemicalWizardry.common.items.routing;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import WayofTime.alchemicalWizardry.api.RoutingFocusPosAndFacing;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RoutingFocus extends Item
{
	public RoutingFocus()
	{
		super();
        this.maxStackSize = 1;
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}
	
	public RoutingFocusPosAndFacing getPosAndFacing(ItemStack itemStack)
	{
		return new RoutingFocusPosAndFacing(new Int3(this.xCoord(itemStack), this.yCoord(itemStack), this.zCoord(itemStack)), this.getSetDirection(itemStack));
	}
	
//	@Override
//    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
//    {
//		this.cycleDirection(itemStack);
//		return itemStack;
//    }
//	
//	public void cycleDirection(ItemStack itemStack)
//	{
//		EnumFacing dir = this.getSetDirection(itemStack);
//		int direction = dir.ordinal();
//		direction++;
//		if(direction >= EnumFacing.VALID_DIRECTIONS.length)
//		{
//			direction = 0;
//		}
//		
//		this.setSetDirection(itemStack, EnumFacing.getOrientation(direction));
//	}
	
	public EnumFacing getSetDirection(ItemStack itemStack)
	{
		if(!itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound tag = itemStack.getTagCompound();
		
		return EnumFacing.getFront(tag.getInteger("direction"));
	}
	
	public void setSetDirection(ItemStack itemStack, EnumFacing dir)
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
        par3List.add(StatCollector.translateToLocal(this.getFocusDescription()));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            NBTTagCompound itemTag = par1ItemStack.getTagCompound();

            par3List.add(StatCollector.translateToLocal("tooltip.alchemy.coords") + " " + itemTag.getInteger("xCoord") + ", " + itemTag.getInteger("yCoord") + ", " + itemTag.getInteger("zCoord"));
            par3List.add(StatCollector.translateToLocal("tooltip.alchemy.direction") + " " + this.getSetDirection(par1ItemStack));
        }
    }
	
	public String getFocusDescription()
	{
		return "tooltip.routingFocus.desc";
	}
    
	@Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
		{
			return false;
		}
		
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IInventory)
		{
			if(player.isSneaking())
			{
				if(this instanceof ILimitedRoutingFocus)
				{
					int pastAmount = ((ILimitedRoutingFocus)this).getRoutingFocusLimit(stack);
					int amount = SpellHelper.getNumberOfItemsInInventory((IInventory)tile, side);
					if(amount != pastAmount)
					{
						((ILimitedRoutingFocus)this).setRoutingFocusLimit(stack, amount);
						player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("message.routerfocus.limit") + amount));
					}					
				}
			}
			
			this.setCoordinates(stack, pos);
			this.setSetDirection(stack, side);
			
			return true;
		}

		return true;
	}
	
	public void setCoordinates(ItemStack itemStack, BlockPos pos)
	{
		if(!itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound tag = itemStack.getTagCompound();
		
		tag.setInteger("xCoord", pos.getX());
		tag.setInteger("yCoord", pos.getY());
		tag.setInteger("zCoord", pos.getZ());
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
    
    public RoutingFocusLogic getLogic(ItemStack itemStack)
	{
		return new RoutingFocusLogic();
	}
}
