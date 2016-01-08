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
import WayofTime.bloodmagic.api.soul.ISoul;
import WayofTime.bloodmagic.api.soul.ISoulGem;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemSoulGem extends Item implements ISoulGem
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
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.soulGem." + names[stack.getItemDamage()]));
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.souls", getSouls(stack)));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public ItemStack fillSoulGem(ItemStack soulGemStack, ItemStack soulStack)
    {
        if (soulStack != null && soulStack.getItem() instanceof ISoul)
        {
            ISoul soul = (ISoul) soulStack.getItem();
            double soulsLeft = getSouls(soulGemStack);

            if (soulsLeft < getMaxSouls(soulGemStack))
            {
                double newSoulsLeft = Math.min(soulsLeft + soul.getSouls(soulStack), getMaxSouls(soulGemStack));
                soul.drainSouls(soulStack, newSoulsLeft - soulsLeft);

                setSouls(soulGemStack, newSoulsLeft);
                if (soul.getSouls(soulStack) <= 0)
                {
                    return null;
                }
            }
        }

        return soulStack;
    }

    @Override
    public double getSouls(ItemStack soulGemStack)
    {
        NBTHelper.checkNBT(soulGemStack);

        NBTTagCompound tag = soulGemStack.getTagCompound();

        return tag.getDouble(Constants.NBT.SOULS);
    }

    public void setSouls(ItemStack soulGemStack, double souls)
    {
        NBTHelper.checkNBT(soulGemStack);

        NBTTagCompound tag = soulGemStack.getTagCompound();

        tag.setDouble(Constants.NBT.SOULS, souls);
    }

    @Override
    public double drainSouls(ItemStack soulGemStack, double drainAmount)
    {
        double souls = getSouls(soulGemStack);

        double soulsDrained = Math.min(drainAmount, souls);
        setSouls(soulGemStack, souls - soulsDrained);

        return soulsDrained;
    }

    @Override
    public int getMaxSouls(ItemStack soulGemStack)
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
