package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.common.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemRitualDiviner extends EnergyItems
{
    private int maxMetaData;

    public ItemRitualDiviner(int id)
    {
        super(id);
        this.maxStackSize = 1;
        setEnergyUsed(100);
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.maxMetaData = 4;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:RitualDiviner");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Used to explore new types of rituals");

        if (this.getMaxRuneDisplacement(par1ItemStack) == 1)
        {
            par3List.add("Can place Dusk runes");
        } else
        {
            par3List.add("Can not place Dusk runes");
        }

        if (!(par1ItemStack.stackTagCompound == null))
        {
            int ritualID = this.getCurrentRitual(par1ItemStack);
            //TODO
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
            par3List.add("RitualID: " + (ritualID + 1));
            List<RitualComponent> ritualList = Rituals.getRitualList(this.getCurrentRitual(par1ItemStack) + 1);
            int blankStones = 0;
            int airStones = 0;
            int waterStones = 0;
            int fireStones = 0;
            int earthStones = 0;
            int duskStones = 0;

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
                }
            }

            par3List.add("Blank stones: " + blankStones);
            par3List.add(EnumChatFormatting.AQUA + "Air stones: " + airStones);
            par3List.add(EnumChatFormatting.BLUE + "Water stones: " + waterStones);
            par3List.add(EnumChatFormatting.RED + "Fire stones: " + fireStones);
            par3List.add(EnumChatFormatting.DARK_GREEN + "Earth stones: " + earthStones);
            par3List.add(EnumChatFormatting.BOLD + "Dusk stones: " + duskStones);
            //par3List.add("Ritual Name: " + Rituals.getNameOfRitual(ritualID));
        }
    }

    @Override
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
        if (!(par1ItemStack.stackTagCompound == null))
        {
            int ritualID = this.getCurrentRitual(par1ItemStack);
            return "Ritual: " + Rituals.getNameOfRitual(ritualID);
            //par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        } else
        {
            return super.getItemStackDisplayName(par1ItemStack);
        }
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par2EntityPlayer);
        
        if(SpellHelper.isFakePlayer(par2EntityPlayer.worldObj, par2EntityPlayer))
        {
        	return false;
        }
        
        ItemStack[] playerInventory = par2EntityPlayer.inventory.mainInventory;
        TileEntity tileEntity = par3World.getBlockTileEntity(par4, par5, par6);

        if (tileEntity instanceof TEMasterStone)
        {
            TEMasterStone masterStone = (TEMasterStone) tileEntity;
            List<RitualComponent> ritualList = Rituals.getRitualList(this.getCurrentRitual(par1ItemStack) + 1);
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
                if (par3World.isAirBlock(par4 + rc.getX(), par5 + rc.getY(), par6 + rc.getZ()))
                {
                    if (playerInvRitualStoneLocation >= 0)
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

                        par3World.setBlock(par4 + rc.getX(), par5 + rc.getY(), par6 + rc.getZ(), ModBlocks.ritualStone.blockID, rc.getStoneType(), 3);

                        if (par3World.isRemote)
                        {
                            par3World.playAuxSFX(2005, par4, par5 + 1, par6, 0);
                            EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed());
                            return true;
                        }

                        return true;
                    }
                } else
                {
                    int blockID = par3World.getBlockId(par4 + rc.getX(), par5 + rc.getY(), par6 + rc.getZ());

                    if (blockID == ModBlocks.ritualStone.blockID)
                    {
                        int metadata = par3World.getBlockMetadata(par4 + rc.getX(), par5 + rc.getY(), par6 + rc.getZ());

                        if (metadata != rc.getStoneType())
                        {
                            if (rc.getStoneType() > this.maxMetaData + this.getMaxRuneDisplacement(par1ItemStack))
                            {
                                par3World.playAuxSFX(200, par4, par5 + 1, par6, 0);
                                return true;
                            }

                            par3World.setBlockMetadataWithNotify(par4 + rc.getX(), par5 + rc.getY(), par6 + rc.getZ(), rc.getStoneType(), 3);
                            EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed());
                            return true;
                        }
                    } else
                    {
                        par3World.playAuxSFX(0000, par4, par5 + 1, par6, 0);
                        return true;
                    }
                }
            }

//			if (par3World.isRemote)
//            {
//                par3World.playAuxSFX(2005, par4, par5, par6, 0);
//                EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed());
//                return true;
//            }
//			return true;
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            int maxRitualID = Rituals.getNumberOfRituals();
            int currentRitualID = this.getCurrentRitual(par1ItemStack);

            if (currentRitualID + 1 >= maxRitualID)
            {
                this.setCurrentRitual(par1ItemStack, 0);
            } else
            {
                this.setCurrentRitual(par1ItemStack, currentRitualID + 1);
            }

            if (par2World.isRemote)
            {
                ChatMessageComponent chatmessagecomponent = new ChatMessageComponent();
                //chatmessagecomponent.func_111072_b("Current Essence: " + data.currentEssence + "LP");
                chatmessagecomponent.addText("Current Ritual: " + Rituals.getNameOfRitual(this.getCurrentRitual(par1ItemStack)));
                par3EntityPlayer.sendChatToPlayer(chatmessagecomponent);
            }
        }

        return par1ItemStack;
    }

    public int getCurrentRitual(ItemStack par1ItemStack)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return par1ItemStack.stackTagCompound.getInteger("ritualID");
    }

    public void setCurrentRitual(ItemStack par1ItemStack, int ritualID)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.stackTagCompound.setInteger("ritualID", ritualID);
    }

    public int getMaxRuneDisplacement(ItemStack par1ItemStack) //0 indicates the starting 4 runes, 1 indicates it can use Dusk runes
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return par1ItemStack.stackTagCompound.getInteger("maxRuneDisplacement");
    }

    public void setMaxRuneDisplacement(ItemStack par1ItemStack, int displacement)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.stackTagCompound.setInteger("maxRuneDisplacement", displacement);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs creativeTab, List list)
    {
        list.add(new ItemStack(ModItems.itemRitualDiviner));
        ItemStack duskRitualDivinerStack = new ItemStack(ModItems.itemRitualDiviner);
        ((ItemRitualDiviner) duskRitualDivinerStack.getItem()).setMaxRuneDisplacement(duskRitualDivinerStack, 1);
        list.add(duskRitualDivinerStack);
    }
}
