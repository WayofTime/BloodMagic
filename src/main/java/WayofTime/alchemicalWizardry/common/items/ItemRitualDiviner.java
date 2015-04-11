package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.items.interfaces.IRitualDiviner;
import WayofTime.alchemicalWizardry.api.rituals.IRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRitualDiviner extends EnergyItems implements IRitualDiviner
{
    private int maxMetaData;

    public ItemRitualDiviner()
    {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(100);
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.maxMetaData = 4;
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:RitualDiviner");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.desc"));

        if (this.getMaxRuneDisplacement(par1ItemStack) == 1)
        {
            par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.canplace"));
        }else if (this.getMaxRuneDisplacement(par1ItemStack) >= 2)
        {
            par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.canplacedawn"));

        }else
        {
            par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.cannotplace"));
        }
        
        par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.ritualtunedto") + " " + this.getNameForDirection(this.getDirection(par1ItemStack)));

        boolean sneaking = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        
        if(sneaking)
        {
        	if (!(par1ItemStack.getTagCompound() == null))
            {
                String ritualID = this.getCurrentRitual(par1ItemStack);
                //TODO
                par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
                par3List.add(StatCollector.translateToLocal("tooltip.alchemy.ritualid") + " " + ritualID);
                List<RitualComponent> ritualList = Rituals.getRitualList(this.getCurrentRitual(par1ItemStack));
                if (ritualList == null)
                {
                    return;
                }

                int blankStones = 0;
                int airStones = 0;
                int waterStones = 0;
                int fireStones = 0;
                int earthStones = 0;
                int duskStones = 0;
                int dawnStones = 0;

                for (RitualComponent rc : ritualList)
                {
                    switch (rc.getStoneType())
                    {
                        case RitualComponent.BLANK:
                            blankStones++;
                            break;

                        case RitualComponent.AIR:
                            airStones++;
                            break;

                        case RitualComponent.WATER:
                            waterStones++;
                            break;

                        case RitualComponent.FIRE:
                            fireStones++;
                            break;

                        case RitualComponent.EARTH:
                            earthStones++;
                            break;

                        case RitualComponent.DUSK:
                            duskStones++;
                            break;
                        
                        case RitualComponent.DAWN:
                        	dawnStones++;
                        	break;
                    }
                }

                par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.blankstones") + " " + blankStones);
                par3List.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip.ritualdiviner.airstones") + " " + airStones);
                par3List.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.ritualdiviner.waterstones") + " " + waterStones);
                par3List.add(EnumChatFormatting.RED + StatCollector.translateToLocal("tooltip.ritualdiviner.firestones") + " " + fireStones);
                par3List.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocal("tooltip.ritualdiviner.earthstones") + " " + earthStones);
                par3List.add(EnumChatFormatting.BOLD + StatCollector.translateToLocal("tooltip.ritualdiviner.duskstones") + " " + duskStones);
                par3List.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("tooltip.ritualdiviner.dawnstones") + " " + dawnStones);
            }
        }else
        {
        	par3List.add(EnumChatFormatting.AQUA + "-" + StatCollector.translateToLocal("tooltip.ritualdiviner.moreinfo") + "-");
        }
        
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
        if (!(par1ItemStack.getTagCompound() == null))
        {
            String ritualID = this.getCurrentRitual(par1ItemStack);
            if (ritualID.equals(""))
            {
                return super.getItemStackDisplayName(par1ItemStack);
            }
            return "Ritual: " + Rituals.getNameOfRitual(ritualID);
        } else
        {
            return super.getItemStackDisplayName(par1ItemStack);
        }
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
    	int direction = this.getDirection(par1ItemStack);
    	
        if (!EnergyItems.checkAndSetItemOwner(par1ItemStack, par2EntityPlayer)) return false;
        ItemStack[] playerInventory = par2EntityPlayer.inventory.mainInventory;
        TileEntity tileEntity = par3World.getTileEntity(par4, par5, par6);

        if (tileEntity instanceof TEMasterStone)
        {
            TEMasterStone masterStone = (TEMasterStone) tileEntity;
            List<RitualComponent> ritualList = Rituals.getRitualList(this.getCurrentRitual(par1ItemStack));
            if (ritualList == null)
            {
                return false;
            }

            int playerInvRitualStoneLocation = -1;

            for (int i = 0; i < playerInventory.length; i++)
            {
                if (playerInventory[i] == null)
                {
                    continue;
                }

                if (new ItemStack(ModBlocks.ritualStone).isItemEqual(playerInventory[i]))
                {
                    playerInvRitualStoneLocation = i;
                    break;
                }
            }

            for (RitualComponent rc : ritualList)
            {
                if (par3World.isAirBlock(par4 + rc.getX(direction), par5 + rc.getY(), par6 + rc.getZ(direction)))
                {
                    if (playerInvRitualStoneLocation >= 0 || par2EntityPlayer.capabilities.isCreativeMode)
                    {
                        if (rc.getStoneType() > this.maxMetaData + this.getMaxRuneDisplacement(par1ItemStack))
                        {
                            par3World.playAuxSFX(200, par4, par5 + 1, par6, 0);
                            return true;
                        }

                        if (!par2EntityPlayer.capabilities.isCreativeMode)
                        {
                            par2EntityPlayer.inventory.decrStackSize(playerInvRitualStoneLocation, 1);
                        }

                        if(EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed()))
                        {
                        	par3World.setBlock(par4 + rc.getX(direction), par5 + rc.getY(), par6 + rc.getZ(direction), ModBlocks.ritualStone, rc.getStoneType(), 3);

                            if (par3World.isRemote)
                            {
                                par3World.playAuxSFX(2005, par4, par5 + 1, par6, 0);
                                
                                return true;
                            }
                        }

                        return true;
                    }
                } else
                {
                    Block block = par3World.getBlock(par4 + rc.getX(direction), par5 + rc.getY(), par6 + rc.getZ(direction));

                    if (block == ModBlocks.ritualStone)
                    {
                        int metadata = par3World.getBlockMetadata(par4 + rc.getX(direction), par5 + rc.getY(), par6 + rc.getZ(direction));

                        if (metadata != rc.getStoneType())
                        {
                        	if(EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed()))
                        	{
	                            if (rc.getStoneType() > this.maxMetaData + this.getMaxRuneDisplacement(par1ItemStack))
	                            {
	                                par3World.playAuxSFX(200, par4, par5 + 1, par6, 0);
	                                return true;
	                            }
	
	                            par3World.setBlockMetadataWithNotify(par4 + rc.getX(direction), par5 + rc.getY(), par6 + rc.getZ(direction), rc.getStoneType(), 3);
	                            return true;
                        	}
                        }
                    } else
                    {
                        par3World.playAuxSFX(0000, par4, par5 + 1, par6, 0);
                        return true;
                    }
                }
            }
        }else if(!(par3World.getBlock(par4, par5, par6) instanceof IRitualStone) && !par2EntityPlayer.isSneaking())
        {
        	if(par3World.isRemote)
        	{
        		return false;
        	}
        	this.cycleDirection(par1ItemStack);
        	par2EntityPlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tooltip.ritualdiviner.ritualtunedto") + " " + this.getNameForDirection(this.getDirection(par1ItemStack))));
            return true;
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) && par3EntityPlayer.isSneaking())
        {
            rotateRituals(par2World,par3EntityPlayer, par1ItemStack, true);
        }

        return par1ItemStack;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entityLiving;

            if (!EnergyItems.checkAndSetItemOwner(stack,player)) return true;

            if (!player.isSwingInProgress)
            {
                if (player.isSneaking())
                {
                    rotateRituals(player.worldObj, player, stack, false);
                }
//                else
//                {
//                    if (!player.worldObj.isRemote)
//                    {
//                        int direction = this.getDirection(stack) - 1;
//                        if (direction == 0) direction = 4;
//                        this.setDirection(stack, direction);
//                        player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tooltip.ritualdiviner.ritualtunedto") + " " + this.getNameForDirection(direction)));
//                    }
//                }
            }
        }

        return false;
    }

    public void rotateRituals(World world, EntityPlayer player, ItemStack stack, boolean next)
    {
        int maxRitualID = Rituals.getNumberOfRituals();
        String currentRitualID = this.getCurrentRitual(stack);

        this.setCurrentRitual(stack, next? Rituals.getNextRitualKey(currentRitualID):Rituals.getPreviousRitualKey(currentRitualID));

        if (world.isRemote)
        {
            IChatComponent chatmessagecomponent = new ChatComponentText(StatCollector.translateToLocal("message.ritual.currentritual") + " " + Rituals.getNameOfRitual(this.getCurrentRitual(stack)));
            player.addChatComponentMessage(chatmessagecomponent);
        }
    }

    @Override
    public String getCurrentRitual(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return par1ItemStack.getTagCompound().getString("ritualID");
    }

    @Override
    public void setCurrentRitual(ItemStack par1ItemStack, String ritualID)
    {
        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.getTagCompound().setString("ritualID", ritualID);
    }

    @Override
    public int getMaxRuneDisplacement(ItemStack par1ItemStack) //0 indicates the starting 4 runes, 1 indicates it can use Dusk runes
    {
        return par1ItemStack.getItemDamage();
    }

    @Override
    public void setMaxRuneDisplacement(ItemStack par1ItemStack, int displacement)
    {
        par1ItemStack.setItemDamage(displacement);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List list)
    {
        list.add(new ItemStack(id));
        
        ItemStack duskRitualDivinerStack = new ItemStack(id);
        this.setMaxRuneDisplacement(duskRitualDivinerStack, 1);
        list.add(duskRitualDivinerStack);
        
        ItemStack dawnRitualDivinerStack = new ItemStack(id);
        this.setMaxRuneDisplacement(dawnRitualDivinerStack, 2);
        list.add(dawnRitualDivinerStack);
    }
    
    @Override
    public int getDirection(ItemStack itemStack)
    {
    	if(itemStack.getTagCompound() == null)
    	{
    		itemStack.setTagCompound(new NBTTagCompound());
    	}
    	
    	return itemStack.getTagCompound().getInteger("direction");
    }
    
    @Override
    public void setDirection(ItemStack itemStack, int direction)
    {
    	if(itemStack.getTagCompound() == null)
    	{
    		itemStack.setTagCompound(new NBTTagCompound());
    	}
    	
    	itemStack.getTagCompound().setInteger("direction", direction);
    }
    
    @Override
    public int cycleDirection(ItemStack itemStack)
    {
    	int direction = this.getDirection(itemStack);
    	
    	if(direction < 4)
    	{
    		direction = Math.max(1, direction + 1);
    	}else
    	{
    		direction = 1;
    	}
    	
    	this.setDirection(itemStack, direction);
    	
    	return direction;
    }
    
    @Override
    public String getNameForDirection(int direction)
    {
    	String dir = "";
    	switch(direction)
    	{
    	case 0:
    	case 1:
    		dir = StatCollector.translateToLocal("message.ritual.side.north");
    		break;
    	case 2:
    		dir = StatCollector.translateToLocal("message.ritual.side.east");
    		break;
    	case 3:
    		dir = StatCollector.translateToLocal("message.ritual.side.south");
    		break;
    	case 4:
    		dir = StatCollector.translateToLocal("message.ritual.side.west");
    	}
    	
    	return dir;
    }
}