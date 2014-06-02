package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.interfaces.IHolding;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilOfHolding extends EnergyItems
{
    private int invSize = 4;

    public static List<ItemStack> allowedSigils = new ArrayList();

    public SigilOfHolding()
    {
        super();
        this.maxStackSize = 1;
        //setEnergyUsed(100);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfHolding");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        if (!(stack.stackTagCompound == null))
        {
            ItemStack[] inv = getInternalInventory(stack);

            if (inv == null)
            {
                return this.itemIcon;
            }

            ItemStack item = inv[stack.stackTagCompound.getInteger("selectedSlot")];

            if (item != null)
            {
                return item.getIconIndex();
            }
        }

        return this.itemIcon;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Used to hold several Sigils!");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
//            par3List.add("Current slot: " + par1ItemStack.stackTagCompound.getInteger("selectedSlot"));
            ItemStack[] inv = getInternalInventory(par1ItemStack);

            if (inv == null)
            {
                return;
            }

            ItemStack item = inv[par1ItemStack.stackTagCompound.getInteger("selectedSlot")];

            if (item != null)
            {
                par3List.add("Current item: " + item.getDisplayName());
            }

            for (int i = 0; i < invSize; i++)
            {
                if (inv[i] != null)
                {
                    par3List.add("Item in slot " + i + ": " + inv[i].getDisplayName());
                }
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        //TODO Might be a good idea to have this item need to be in the player's first slot
        //for it to search and consume sigils on right click. Might avoid confusion? At least
        //will avoid the need to add a button just for it...
        this.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            if (this.addSigilToInventory(par1ItemStack, par3EntityPlayer))
            {
                return par1ItemStack;
            }

            selectNextSlot(par1ItemStack);
            return par1ItemStack;
        }

        int currentSlot = this.getSelectedSlot(par1ItemStack);
        ItemStack[] inv = getInternalInventory(par1ItemStack);

        if (inv == null)
        {
            return par1ItemStack;
        }

        ItemStack itemUsed = inv[currentSlot];

        if (itemUsed == null)
        {
            return par1ItemStack;
        }

        itemUsed.getItem().onItemRightClick(itemUsed, par2World, par3EntityPlayer);
        saveInternalInventory(par1ItemStack, inv);
        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par1ItemStack.stackTagCompound == null))
        {
            this.tickInternalInventory(par1ItemStack, par2World, par3Entity, par4, par5);
        }
    }

    public ItemStack[] getInternalInventory(ItemStack itemStack)
    {
        NBTTagCompound itemTag = itemStack.stackTagCompound;

        if (itemTag == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
            return null;
        }

        ItemStack[] inv = new ItemStack[9];
        NBTTagList tagList = itemTag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

        if (tagList == null)
        {
            return null;
        }

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
            int slot = tag.getByte("Slot");

            if (slot >= 0 && slot < invSize)
            {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        return inv;
    }

    public void saveInternalInventory(ItemStack itemStack, ItemStack[] inventory)
    {
        NBTTagCompound itemTag = itemStack.stackTagCompound;

        if (itemTag == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < invSize; i++)
        {
            ItemStack stack = inventory[i];

            if (inventory[i] != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        itemTag.setTag("Inventory", itemList);
    }

    public void tickInternalInventory(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        ItemStack[] inv = getInternalInventory(par1ItemStack);

        if (inv == null)
        {
            return;
        }

        for (int i = 0; i < invSize; i++)
        {
            if (inv[i] == null)
            {
                continue;
            }

            inv[i].getItem().onUpdate(inv[i], par2World, par3Entity, par4, par5);
        }
    }

    public int getSelectedSlot(ItemStack itemStack)
    {
        NBTTagCompound itemTag = itemStack.stackTagCompound;

        if (itemTag == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemTag.getInteger("selectedSlot");
    }

    public void selectNextSlot(ItemStack itemStack)
    {
        ItemStack[] inv = getInternalInventory(itemStack);
        int filledSlots = 0;

        for (int i = 0; i < invSize; i++)
        {
            if (inv[i] != null)
            {
                filledSlots++;
            } else
            {
                break;
            }
        }

        NBTTagCompound itemTag = itemStack.stackTagCompound;

        if (itemTag == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        if (getSelectedSlot(itemStack) + 1 < filledSlots)
        {
            itemTag.setInteger("selectedSlot", itemTag.getInteger("selectedSlot") + 1);
        } else
        {
            itemTag.setInteger("selectedSlot", 0);
        }
    }

    public boolean hasAddedToInventory(ItemStack sigilItemStack, ItemStack addedItemStack)
    {
        ItemStack[] inv = getInternalInventory(sigilItemStack);

        if (inv == null)
        {
            return false;
        }

        if (addedItemStack == null)
        {
            return false;
        }

        Item item = addedItemStack.getItem();
        int candidateSlot = -1;

        for (int i = invSize - 1; i >= 0; i--)
        {
            ItemStack nextItem = inv[i];

            if (nextItem == null)
            {
                candidateSlot = i;
                continue;
            }

            if (item == nextItem.getItem())
            {
                return false;
            }
        }

        if (candidateSlot == -1)
        {
            return false;
        }

        if(addedItemStack.getItem() instanceof IHolding)
        {
        	inv[candidateSlot] = addedItemStack;
            saveInternalInventory(sigilItemStack, inv);
            return true;
        }
        
        for (ItemStack i : allowedSigils)
        {
            if (i != null && i.getItem() == item)
            {
                inv[candidateSlot] = addedItemStack;
                saveInternalInventory(sigilItemStack, inv);
                return true;
            }
        }

        return false;
    }

    public boolean addSigilToInventory(ItemStack sigilItemStack, EntityPlayer player)
    {
        ItemStack[] playerInventory = player.inventory.mainInventory;

        for (int i = 0; i < 36; i++)
        {
            if (this.hasAddedToInventory(sigilItemStack, playerInventory[i]))
            {
                player.inventory.consumeInventoryItem(playerInventory[i].getItem());
                //playerInventory[i].stackSize--;
                return true;
            }
        }

        return false;
    }

    public static void initiateSigilOfHolding()
    {
        allowedSigils.add(new ItemStack(ModItems.waterSigil));
        allowedSigils.add(new ItemStack(ModItems.lavaSigil));
        allowedSigils.add(new ItemStack(ModItems.voidSigil));
        allowedSigils.add(new ItemStack(ModItems.airSigil));
        allowedSigils.add(new ItemStack(ModItems.sigilOfTheFastMiner));
        allowedSigils.add(new ItemStack(ModItems.divinationSigil));
        allowedSigils.add(new ItemStack(ModItems.sigilOfElementalAffinity));
        allowedSigils.add(new ItemStack(ModItems.growthSigil));
        allowedSigils.add(new ItemStack(ModItems.sigilOfHaste));
        allowedSigils.add(new ItemStack(ModItems.sigilOfWind));
    }

    public ItemStack getCurrentItem(ItemStack sigilItemStack)
    {
        ItemStack[] items = this.getInternalInventory(sigilItemStack);

        if (items == null)
        {
            return null;
        }

        return items[this.getSelectedSlot(sigilItemStack)];
    }
}
