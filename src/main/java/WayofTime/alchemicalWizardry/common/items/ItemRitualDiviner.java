package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.items.interfaces.IRitualDiviner;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
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
    public void addInformation(ItemStack stack, EntityPlayer player, List par3List, boolean x)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.desc"));

        if (this.getMaxRuneDisplacement(stack) == 1)
        {
            par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.canplace"));
        }else if (this.getMaxRuneDisplacement(stack) >= 2)
        {
            par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.canplacedawn"));

        }else
        {
            par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.cannotplace"));
        }
        
        par3List.add(StatCollector.translateToLocal("tooltip.ritualdiviner.ritualtunedto") + " " + this.getNameForDirection(this.getDirection(stack)));

        boolean sneaking = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        
        if(sneaking)
        {
        	if (!(stack.getTagCompound() == null))
            {
                String ritualID = this.getCurrentRitual(stack);
                //TODO
                par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
                par3List.add(StatCollector.translateToLocal("tooltip.alchemy.ritualid") + " " + ritualID);
                List<RitualComponent> ritualList = Rituals.getRitualList(this.getCurrentRitual(stack));
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

                int totalStones = blankStones + airStones + waterStones + fireStones + earthStones + duskStones + dawnStones;

                par3List.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("tooltip.ritualdiviner.blankstones") + " " + blankStones);
                par3List.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip.ritualdiviner.airstones") + " " + airStones);
                par3List.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.ritualdiviner.waterstones") + " " + waterStones);
                par3List.add(EnumChatFormatting.RED + StatCollector.translateToLocal("tooltip.ritualdiviner.firestones") + " " + fireStones);
                par3List.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocal("tooltip.ritualdiviner.earthstones") + " " + earthStones);
                par3List.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tooltip.ritualdiviner.duskstones") + " " + duskStones);
                par3List.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("tooltip.ritualdiviner.dawnstones") + " " + dawnStones);
                par3List.add(EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("tooltip.ritualdiviner.totalStones") + " " + totalStones);
            }
        }else
        {
        	par3List.add(EnumChatFormatting.AQUA + "-" + StatCollector.translateToLocal("tooltip.ritualdiviner.moreinfo") + "-");
        }
        
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (!(stack.getTagCompound() == null))
        {
            String ritualID = this.getCurrentRitual(stack);
            if (ritualID.equals(""))
            {
                return super.getItemStackDisplayName(stack);
            }
            return "Ritual: " + Rituals.getNameOfRitual(ritualID);
        } else
        {
            return super.getItemStackDisplayName(stack);
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (!EnergyItems.checkAndSetItemOwner(stack, player)) return false;

        if(placeRitualStoneAtMasterStone(stack, player, world, x, y, z))
        {
        	this.setStoredLocation(stack, new Int3(x, y, z));
        	return true;
        }else if(!(world.getBlock(x, y, z) instanceof IRitualStone || world.getBlock(x, y, z) instanceof IMasterRitualStone) && !player.isSneaking())
        {
        	if(world.isRemote)
        	{
        		return false;
        	}
        	this.cycleDirection(stack);
        	player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tooltip.ritualdiviner.ritualtunedto") + " " + this.getNameForDirection(this.getDirection(stack))));
            return true;
        }
        
        return false;
    }
    
    public boolean placeRitualStoneAtMasterStone(ItemStack stack, EntityPlayer player, World world, int x, int y, int z)
    {
    	int direction = this.getDirection(stack);

    	ItemStack[] playerInventory = player.inventory.mainInventory;
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TEMasterStone)
        {
            TEMasterStone masterStone = (TEMasterStone) tileEntity;
            List<RitualComponent> ritualList = Rituals.getRitualList(this.getCurrentRitual(stack));
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
                if (world.isAirBlock(x + rc.getX(direction), y + rc.getY(), z + rc.getZ(direction)))
                {
                    if (playerInvRitualStoneLocation >= 0 || player.capabilities.isCreativeMode)
                    {
                        if (rc.getStoneType() > this.maxMetaData + this.getMaxRuneDisplacement(stack))
                        {
                            world.playAuxSFX(200, x, y + 1, z, 0);
                            return true;
                        }

                        if (!player.capabilities.isCreativeMode)
                        {
                            player.inventory.decrStackSize(playerInvRitualStoneLocation, 1);
                        }

                        if(EnergyItems.syphonBatteries(stack, player, getEnergyUsed()))
                        {
                        	world.setBlock(x + rc.getX(direction), y + rc.getY(), z + rc.getZ(direction), ModBlocks.ritualStone, rc.getStoneType(), 3);

                            if (world.isRemote)
                            {
                                world.playAuxSFX(2005, x, y + 1, z, 0);
                                
                                return true;
                            }
                        }

                        return true;
                    }
                } else
                {
                    Block block = world.getBlock(x + rc.getX(direction), y + rc.getY(), z + rc.getZ(direction));

                    if (block == ModBlocks.ritualStone)
                    {
                        int metadata = world.getBlockMetadata(x + rc.getX(direction), y + rc.getY(), z + rc.getZ(direction));

                        if (metadata != rc.getStoneType())
                        {
                        	if(EnergyItems.syphonBatteries(stack, player, getEnergyUsed()))
                        	{
	                            if (rc.getStoneType() > this.maxMetaData + this.getMaxRuneDisplacement(stack))
	                            {
	                                world.playAuxSFX(200, x, y + 1, z, 0);
	                                return true;
	                            }
	
	                            world.setBlockMetadataWithNotify(x + rc.getX(direction), y + rc.getY(), z + rc.getZ(direction), rc.getStoneType(), 3);
	                            return true;
                        	}
                        }
                    } else
                    {
                        world.playAuxSFX(0000, x, y + 1, z, 0);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
    {
    	if(entity instanceof EntityPlayer && hasStoredLocation(stack) && world.getWorldTime() % 5 == 0)
    	{
			Int3 loc = getStoredLocation(stack);
			
			int x = loc.xCoord;
			int y = loc.yCoord;
			int z = loc.zCoord;
			
			if(!this.placeRitualStoneAtMasterStone(stack, (EntityPlayer)entity, world, x, y, z))
			{
				this.voidStoredLocation(stack);
			}
    	}
    }
    
    public void setStoredLocation(ItemStack stack, Int3 location)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null)
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	NBTTagCompound locTag = location.writeToNBT(new NBTTagCompound());
    	locTag.setBoolean("isStored", true);
    	
    	tag.setTag("location", locTag);
    }
    
    public void voidStoredLocation(ItemStack stack)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null || !tag.hasKey("location"))
    	{
    		tag = new NBTTagCompound();
    		stack.setTagCompound(tag);
    	}
    	
    	NBTTagCompound locTag = (NBTTagCompound)tag.getTag("location");
    	locTag.setBoolean("isStored", false);
    }
    
    public Int3 getStoredLocation(ItemStack stack)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null || !tag.hasKey("location"))
    	{
    		return new Int3(0, 0, 0);
    	}
    	
    	NBTTagCompound locTag = (NBTTagCompound)tag.getTag("location");
    	
    	return Int3.readFromNBT(locTag);
    }
    
    public boolean hasStoredLocation(ItemStack stack)
    {
    	NBTTagCompound tag = stack.getTagCompound();
    	if(tag == null || !tag.hasKey("location"))
    	{
    		return false;
    	}
    	
    	NBTTagCompound locTag = (NBTTagCompound)tag.getTag("location");
    	
    	return locTag.getBoolean("isStored");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (EnergyItems.checkAndSetItemOwner(stack, par3EntityPlayer) && par3EntityPlayer.isSneaking())
        {
            rotateRituals(par2World,par3EntityPlayer, stack, true);
        }

        return stack;
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
    public String getCurrentRitual(ItemStack stack)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack.getTagCompound().getString("ritualID");
    }

    @Override
    public void setCurrentRitual(ItemStack stack, String ritualID)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setString("ritualID", ritualID);
    }

    @Override
    public int getMaxRuneDisplacement(ItemStack stack) //0 indicates the starting 4 runes, 1 indicates it can use Dusk runes
    {
        return stack.getItemDamage();
    }

    @Override
    public void setMaxRuneDisplacement(ItemStack stack, int displacement)
    {
        stack.setItemDamage(displacement);
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