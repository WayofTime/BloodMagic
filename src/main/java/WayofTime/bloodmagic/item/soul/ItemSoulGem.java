package WayofTime.bloodmagic.item.soul;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillGem;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemSoulGem extends Item implements IDemonWillGem
{
    public static String[] names = { "petty", "lesser", "common", "greater", "grand" };

    public ItemSoulGem()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".soulGem.");
        setHasSubtypes(true);
        setMaxStackSize(1);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < names.length; i++)
        {
            ItemStack stack = new ItemStack(this, 1, i);
            list.add(stack);
        }

        for (int i = 0; i < names.length; i++)
        {
            ItemStack stack = new ItemStack(this, 1, i);
            this.setWill(stack, this.getMaxWill(stack));
            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.soulGem." + names[stack.getItemDamage()]));
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.will", getWill(stack)));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public ItemStack fillDemonWillGem(ItemStack soulGemStack, ItemStack soulStack)
    {
        if (soulStack != null && soulStack.getItem() instanceof IDemonWill)
        {
            IDemonWill soul = (IDemonWill) soulStack.getItem();
            double soulsLeft = getWill(soulGemStack);

            if (soulsLeft < getMaxWill(soulGemStack))
            {
                double newSoulsLeft = Math.min(soulsLeft + soul.getWill(soulStack), getMaxWill(soulGemStack));
                soul.drainWill(soulStack, newSoulsLeft - soulsLeft);

                setWill(soulGemStack, newSoulsLeft);
                if (soul.getWill(soulStack) <= 0)
                {
                    return null;
                }
            }
        }

        return soulStack;
    }

    @Override
    public double getWill(ItemStack soulGemStack)
    {
        NBTHelper.checkNBT(soulGemStack);

        NBTTagCompound tag = soulGemStack.getTagCompound();

        return tag.getDouble(Constants.NBT.SOULS);
    }

    @Override
    public void setWill(ItemStack soulGemStack, double souls)
    {
        NBTHelper.checkNBT(soulGemStack);

        NBTTagCompound tag = soulGemStack.getTagCompound();

        tag.setDouble(Constants.NBT.SOULS, souls);
    }

    @Override
    public double drainWill(ItemStack soulGemStack, double drainAmount)
    {
        double souls = getWill(soulGemStack);

        double soulsDrained = Math.min(drainAmount, souls);
        setWill(soulGemStack, souls - soulsDrained);

        return soulsDrained;
    }

    @Override
    public int getMaxWill(ItemStack soulGemStack)
    {
        switch (soulGemStack.getMetadata())
        {
        case 0:
            return 64;
        case 1:
            return 256;
        case 2:
            return 1024;
        case 3:
            return 4096;
        case 4:
            return 16384;
        }
        return 64;
    }
}
