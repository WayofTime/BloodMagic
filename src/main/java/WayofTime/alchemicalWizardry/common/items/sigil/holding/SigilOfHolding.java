package WayofTime.alchemicalWizardry.common.items.sigil.holding;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public class SigilOfHolding extends EnergyItems
{
    private static int invSize = 4;

    private static final String NBT_CURRENT_SIGIL = "CurrentSigil";

    public SigilOfHolding()
    {
        super();
        this.maxStackSize = 1;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofholding.desc"));

        if (!(stack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
            ItemStack[] inv = getInternalInventory(stack);

            if (inv == null)
            {
                return;
            }

            int currentSlot = getCurrentItem(stack);
            ItemStack item = inv[currentSlot];

            if (item != null)
            {
                par3List.add(StatCollector.translateToLocal("tooltip.item.currentitem") + " " + item.getDisplayName());
            }

            for (int i = 0; i <= invSize; i++)
            {
                if (inv[i] != null)
                {
                    par3List.add(StatCollector.translateToLocal("tooltip.item.iteminslot") + " " + (i + 1) + ": " + inv[i].getDisplayName());
                }
            }
        }
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (checkAndSetItemOwner(stack, player))
        {
        	int currentSlot = getCurrentItem(stack);
            ItemStack[] inv = getInternalInventory(stack);

            if (inv == null)
            {
                return false;
            }

            ItemStack itemUsed = inv[currentSlot];

            if (itemUsed == null)
            {
                return false;
            }

            boolean bool = itemUsed.getItem().onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);

            saveInventory(stack, inv);

            return bool;
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (checkAndSetItemOwner(stack, par3EntityPlayer))
        {
            if (par3EntityPlayer.isSneaking())
            {
                InventoryHolding.setUUID(stack);
                par3EntityPlayer.openGui(AlchemicalWizardry.instance, 3, par3EntityPlayer.worldObj, (int) par3EntityPlayer.posX, (int) par3EntityPlayer.posY, (int) par3EntityPlayer.posZ);
                return stack;
            }

            int currentSlot = getCurrentItem(stack);
            ItemStack[] inv = getInternalInventory(stack);

            if (inv == null)
            {
                return stack;
            }

            ItemStack itemUsed = inv[currentSlot];

            if (itemUsed == null)
            {
                return stack;
            }

            itemUsed.getItem().onItemRightClick(itemUsed, par2World, par3EntityPlayer);
            saveInventory(stack, inv);
        }
        return stack;
    }

    public static int next(int mode)
    {
        int index = mode + 1;

        if (index >= invSize + 1)
        {
            index = 0;
        }

        return index;
    }

    public static int prev(int mode)
    {
        int index = mode - 1;

        if (index < 0)
        {
            index = invSize;
        }

        return index;
    }

    private static void initModeTag(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
            itemStack.getTagCompound().setInteger(NBT_CURRENT_SIGIL, invSize);
        }
    }

    public static ItemStack getCurrentSigil(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() instanceof SigilOfHolding)
        {
            ItemStack[] itemStacks = getInternalInventory(itemStack);
            int currentSlot = getCurrentItem(itemStack);
            if (itemStacks != null)
            {
                return itemStacks[currentSlot];
            }
        }

        return null;
    }

    public static int getCurrentItem(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() instanceof SigilOfHolding)
        {
            initModeTag(itemStack);
            int currentSigil = itemStack.getTagCompound().getInteger(NBT_CURRENT_SIGIL);
            currentSigil = MathHelper.clamp_int(currentSigil, 0, invSize);
            return currentSigil;
        }

        return 4;
    }

    public static ItemStack[] getInternalInventory(ItemStack itemStack)
    {
        initModeTag(itemStack);
        NBTTagCompound tagCompound = itemStack.getTagCompound();

        if (tagCompound == null)
        {
            return null;
        }

        ItemStack[] inv = new ItemStack[9];
        NBTTagList tagList = tagCompound.getTagList(InventoryHolding.NBT_ITEMS, 10);

        if (tagList == null)
        {
            return null;
        }

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            int slot = tag.getByte("Slot");

            if (slot >= 0 && slot <= invSize)
            {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        return inv;
    }

    public void saveInventory(ItemStack itemStack, ItemStack[] inventory)
    {
        NBTTagCompound itemTag = itemStack.getTagCompound();

        if (itemTag == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i <= invSize; i++)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        itemTag.setTag(InventoryHolding.NBT_ITEMS, itemList);
    }

    public static void cycleSigil(ItemStack itemStack, int mode)
    {
        if (itemStack != null && itemStack.getItem() instanceof SigilOfHolding)
        {
            initModeTag(itemStack);
            itemStack.getTagCompound().setInteger(NBT_CURRENT_SIGIL, mode);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(stack.getTagCompound() == null))
        {
            this.tickInternalInventory(stack, par2World, par3Entity, par4, par5);
        }
    }

    public void tickInternalInventory(ItemStack stack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        ItemStack[] inv = getInternalInventory(stack);

        if (inv == null)
        {
            return;
        }

        for (int i = 0; i <= invSize; i++)
        {
            if (inv[i] == null)
            {
                continue;
            }

            inv[i].getItem().onUpdate(inv[i], par2World, par3Entity, par4, par5);
        }
    }
}
