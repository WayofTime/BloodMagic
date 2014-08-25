package WayofTime.alchemicalWizardry.common.items.energy;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.IReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ISegmentedReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;
import WayofTime.alchemicalWizardry.common.Int3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTankSegmenter extends Item implements IReagentManipulator
{
	public IIcon crystalBody;
	public IIcon crystalLabel;
	
	public ItemTankSegmenter()
	{
		super();
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		this.hasSubtypes = true;
		this.maxStackSize = 1;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		Reagent reagent = this.getReagent(stack);
		
		String name = super.getItemStackDisplayName(stack);
		
		if(reagent != null)
		{
			name = name + " (" + reagent.name + ")";
		}
		
		return name;
	}
	
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Used to designate which");
        par3List.add("reagents can go into a container");

        if (!(par1ItemStack.stackTagCompound == null))
        {
        	Reagent reagent = this.getReagent(par1ItemStack);
        	if(reagent != null)
        	{
        		par3List.add("Currently selected reagent: " + reagent.name);
        	}
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.crystalBody = iconRegister.registerIcon("AlchemicalWizardry:TankSegmenter1");
        this.crystalLabel = iconRegister.registerIcon("AlchemicalWizardry:TankSegmenter2");
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		switch(pass)
		{
		case 0:
			return 256*(256*255 + 255) + 255;
		case 1:
			Reagent reagent = this.getReagent(stack);
			if(reagent != null)
			{
				return (reagent.getColourRed()*256*256 + reagent.getColourGreen()*256 + reagent.getColourBlue());
			}
			break;
		}	
		
		return 256*(256*255 + 255) + 255;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int meta)
	{
		return 2;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass)
	{
		switch(pass)
		{
		case 0:
			return this.crystalBody;
		case 1:
			return this.crystalLabel;
		}
		return this.itemIcon;
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
		if(world.isRemote)
		{
			return itemStack;
		}
		
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (movingobjectposition == null)
        {
            return itemStack;
        } else
        {
        	if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;
                
                TileEntity tile = world.getTileEntity(x, y, z);
                
                if(!(tile instanceof ISegmentedReagentHandler))
                {
                	return itemStack;
                }
                
                ISegmentedReagentHandler reagentHandler = (ISegmentedReagentHandler)tile;
                
                if(player.isSneaking())
                {
                	ReagentContainerInfo[] infos = reagentHandler.getContainerInfo(ForgeDirection.UNKNOWN);
                	if(infos != null)
                	{
                    	List<Reagent> reagentList = new LinkedList();
                    	for(ReagentContainerInfo info : infos)
                    	{
                    		if(info != null)
                    		{
                    			ReagentStack reagentStack = info.reagent;
                    			if(reagentStack != null)
                    			{
                    				Reagent reagent = reagentStack.reagent;
                    				if(reagent != null)
                    				{
                    					reagentList.add(reagent);
                    				}
                    			}
                    		}
                    	}
                    	
                    	Reagent pastReagent = this.getReagent(itemStack);
                    	
                    	boolean goForNext = false;
                    	boolean hasFound = false;
                    	for(Reagent reagent : reagentList)
                    	{
                    		if(goForNext)
                    		{
                    			goForNext = false;
                    			break;
                    		}
                    		
                    		if(reagent == pastReagent)
                    		{
                    			goForNext = true;
                    			hasFound = true;
                    		}
                    	}
                    	
                    	if(hasFound)
                    	{
                    		if(goForNext)
                    		{
                        		this.setReagentWithNotification(itemStack, reagentList.get(0), player);
                    		}
                    	}else
                    	{
                    		if(reagentList.size() >= 1)
                    		{
                        		this.setReagentWithNotification(itemStack, reagentList.get(0), player);
                    		}
                    	}
                	}
                    
                }else
                {
                	Reagent reagent = this.getReagent(itemStack);
                    if(reagent == null)
                    {
                    	//TODO: Send message that "All are wiped"
                    	reagentHandler.getAttunedTankMap().clear();
                    	return itemStack;
                    }
                    int totalTankSize = reagentHandler.getNumberOfTanks();
                    int numberAssigned = reagentHandler.getTanksTunedToReagent(reagent) + 1;
                    
                    if(numberAssigned > totalTankSize)
                    {
                    	numberAssigned = 0;
                    }
                    
        			player.addChatComponentMessage(new ChatComponentText("Tank now has " + numberAssigned + " tank(s) set to: " + reagent.name));
                    
                    reagentHandler.setTanksTunedToReagent(reagent, numberAssigned);
                }     
            }else if(movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.MISS)
            {
            	this.setReagent(itemStack, null);
            }
        }
		
		return itemStack;
    }
	
	public Reagent getReagent(ItemStack stack)
	{
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		
		return ReagentRegistry.getReagentForKey(tag.getString("reagent"));
	}
	
	public void setReagent(ItemStack stack, Reagent reagent)
	{
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		
		tag.setString("reagent", ReagentRegistry.getKeyForReagent(reagent));
	}
	
	public void setReagentWithNotification(ItemStack stack, Reagent reagent, EntityPlayer player)
	{
		this.setReagent(stack, reagent);
		
		if(reagent != null)
		{
			player.addChatComponentMessage(new ChatComponentText("Tank Segmenter now set to: " + reagent.name));
		}
	}
}
