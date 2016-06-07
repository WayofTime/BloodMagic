package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.item.inventory.InventoryHolding;
import WayofTime.bloodmagic.util.handler.BMKeyBinding;
import WayofTime.bloodmagic.util.handler.IKeybindable;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSigilHolding extends ItemSigilBase implements IKeybindable
{
    public static int inventorySize;

    public ItemSigilHolding()
    {
        super("holding");

        inventorySize = 5;
    }

    @Override
    public void onKeyPressed(ItemStack stack, EntityPlayer player, BMKeyBinding.Key key, boolean showInChat)
    {
        if (stack == player.getHeldItemMainhand() && stack.getItem() instanceof ItemSigilHolding && key.equals(BMKeyBinding.Key.OPEN_SIGIL_HOLDING))
        {
            InventoryHolding.setUUID(stack);
            player.openGui(BloodMagic.instance, Constants.Gui.SIGIL_HOLDING_GUI, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }

    @Override
    public String getHighlightTip(ItemStack stack, String displayName)
    {
        ItemStack[] inv = getInternalInventory(stack);

        if (inv == null)
            return displayName;

        int currentSlot = getCurrentItemOrdinal(stack);
        ItemStack item = inv[currentSlot];

        if (item == null)
            return displayName;
        else
            return TextHelper.localizeEffect("item.BloodMagic.sigil.holding.display", displayName, item.getDisplayName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);

        ItemStack[] inv = getInternalInventory(stack);

        if (inv == null)
            return;

        int currentSlot = getCurrentItemOrdinal(stack);
        ItemStack item = inv[currentSlot];

        for (int i = 0; i < inventorySize; i++)
        {
            if (inv[i] != null)
                if (item != null && inv[i] == item)
                    tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.sigil.holding.sigilInSlot", i + 1, "&l&n" + inv[i].getDisplayName() + "&r"));
                else
                    tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.sigil.holding.sigilInSlot", i + 1, inv[i].getDisplayName()));
        }
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        int currentSlot = getCurrentItemOrdinal(stack);
        ItemStack[] inv = getInternalInventory(stack);

        if (inv == null)
            return EnumActionResult.PASS;

        ItemStack itemUsing = inv[currentSlot];

        if (itemUsing == null)
            return EnumActionResult.PASS;

        EnumActionResult result = itemUsing.getItem().onItemUseFirst(itemUsing, player, world, pos, side, hitX, hitY, hitZ, hand);
        saveInventory(stack, inv);

        return result;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        int currentSlot = getCurrentItemOrdinal(stack);
        ItemStack[] inv = getInternalInventory(stack);

        if (inv == null)
            return EnumActionResult.PASS;

        ItemStack itemUsing = inv[currentSlot];

        if (itemUsing == null)
            return EnumActionResult.PASS;

        EnumActionResult result = itemUsing.getItem().onItemUse(itemUsing, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        saveInventory(stack, inv);

        return result;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        int currentSlot = getCurrentItemOrdinal(stack);
        ItemStack[] inv = getInternalInventory(stack);

        if (inv == null)
            return ActionResult.newResult(EnumActionResult.PASS, stack);

        ItemStack itemUsing = inv[currentSlot];

        if (itemUsing == null)
            return ActionResult.newResult(EnumActionResult.PASS, stack);

        itemUsing.getItem().onItemRightClick(itemUsing, world, player, hand);

        saveInventory(stack, inv);

        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    public void saveInventory(ItemStack itemStack, ItemStack[] inventory)
    {
        NBTTagCompound itemTag = itemStack.getTagCompound();

        if (itemTag == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inventorySize; i++)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte(Constants.NBT.SLOT, (byte) i);
                inventory[i].writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        itemTag.setTag(Constants.NBT.ITEMS, itemList);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        if (itemStack.getTagCompound() != null)
        {
            this.tickInternalInventory(itemStack, world, entity, itemSlot, isSelected);
        }
    }

    public void tickInternalInventory(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        ItemStack[] inv = getInternalInventory(itemStack);

        if (inv == null)
        {
            return;
        }

        for (int i = 0; i < inventorySize; i++)
        {
            if (inv[i] == null)
            {
                continue;
            }

            inv[i].getItem().onUpdate(inv[i], world, entity, itemSlot, isSelected);
        }
    }

    public static int next(int mode)
    {
        int index = mode + 1;

        if (index >= inventorySize)
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
            index = inventorySize;
        }

        return index;
    }

    private static void initModeTag(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack = NBTHelper.checkNBT(itemStack);
            itemStack.getTagCompound().setInteger(Constants.NBT.CURRENT_SIGIL, inventorySize);
        }
    }

    public static ItemStack getCurrentSigil(ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof ItemSigilHolding)
        {
            ItemStack[] itemStacks = getInternalInventory(itemStack);
            if (itemStacks != null)
            {
                return itemStacks[getCurrentItemOrdinal(itemStack)];
            }
        }

        return null;
    }

    public static int getCurrentItemOrdinal(ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof ItemSigilHolding)
        {
            initModeTag(itemStack);
            int currentSigil = itemStack.getTagCompound().getInteger(Constants.NBT.CURRENT_SIGIL);
            currentSigil = MathHelper.clamp_int(currentSigil, 0, inventorySize - 1);
            return currentSigil;
        }

        return 0;
    }

    public static ItemStack[] getInternalInventory(ItemStack itemStack)
    {
        initModeTag(itemStack);
        NBTTagCompound tagCompound = itemStack.getTagCompound();

        if (tagCompound == null)
        {
            return null;
        }

        NBTTagList tagList = tagCompound.getTagList(Constants.NBT.ITEMS, 10);

        if (tagList == null)
        {
            return null;
        }

        ItemStack[] inv = new ItemStack[inventorySize];

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound data = tagList.getCompoundTagAt(i);
            byte j = data.getByte(Constants.NBT.SLOT);

            if (j >= 0 && j < inv.length)
            {
                inv[j] = ItemStack.loadItemStackFromNBT(data);
            }
        }

        return inv;
    }

    public static void cycleSigil(ItemStack itemStack, int mode)
    {
        if (itemStack.getItem() instanceof ItemSigilHolding)
        {
            initModeTag(itemStack);
            itemStack.getTagCompound().setInteger(Constants.NBT.CURRENT_SIGIL, mode);
        }
    }
}
