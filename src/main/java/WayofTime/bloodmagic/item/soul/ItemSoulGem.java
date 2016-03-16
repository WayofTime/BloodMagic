package WayofTime.bloodmagic.item.soul;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillGem;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ItemSoulGem extends Item implements IDemonWillGem, IVariantProvider
{
    public static String[] names = { "petty", "lesser", "common", "greater", "grand" };

    public ItemSoulGem()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".soulGem.");
        setRegistryName(Constants.BloodMagicItem.SOUL_GEM.getRegName());
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
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        EnumDemonWillType type = this.getCurrentType(stack);
        double drain = Math.min(this.getWill(type, stack), this.getMaxWill(type, stack) / 10);

        double filled = PlayerDemonWillHandler.addDemonWill(type, player, drain, stack);
        this.drainWill(type, stack, filled);

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < names.length; i++)
        {
            ItemStack emptyStack = new ItemStack(this, 1, i);
            ItemStack fullStack = new ItemStack(this, 1, i);
            setWill(EnumDemonWillType.DEFAULT, fullStack, getMaxWill(EnumDemonWillType.DEFAULT, fullStack));
            list.add(emptyStack);
            list.add(fullStack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        EnumDemonWillType type = this.getCurrentType(stack);
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.soulGem." + names[stack.getItemDamage()]));
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.will", getWill(type, stack)));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        EnumDemonWillType type = this.getCurrentType(stack);
        double maxWill = getMaxWill(type, stack);
        if (maxWill <= 0)
        {
            return 1;
        }
        return 1.0 - (getWill(type, stack) / maxWill);
    }

    @Override
    public ItemStack fillDemonWillGem(ItemStack soulGemStack, ItemStack soulStack)
    {
        if (soulStack != null && soulStack.getItem() instanceof IDemonWill)
        {
            EnumDemonWillType thisType = this.getCurrentType(soulGemStack);
            IDemonWill soul = (IDemonWill) soulStack.getItem();
            double soulsLeft = getWill(thisType, soulGemStack);

            if (soulsLeft < getMaxWill(thisType, soulGemStack))
            {
                double newSoulsLeft = Math.min(soulsLeft + soul.getWill(soulStack), getMaxWill(thisType, soulGemStack));
                soul.drainWill(soulStack, newSoulsLeft - soulsLeft);

                setWill(thisType, soulGemStack, newSoulsLeft);
                if (soul.getWill(soulStack) <= 0)
                {
                    return null;
                }
            }
        }

        return soulStack;
    }

    @Override
    public double getWill(EnumDemonWillType type, ItemStack soulGemStack)
    {
        if (!type.equals(getCurrentType(soulGemStack)))
        {
            return 0;
        }

        NBTTagCompound tag = soulGemStack.getTagCompound();

        return tag.getDouble(Constants.NBT.SOULS);
    }

    @Override
    public void setWill(EnumDemonWillType type, ItemStack soulGemStack, double souls)
    {
        setCurrentType(type, soulGemStack);

        NBTTagCompound tag = soulGemStack.getTagCompound();

        tag.setDouble(Constants.NBT.SOULS, souls);
    }

    @Override
    public double drainWill(EnumDemonWillType type, ItemStack soulGemStack, double drainAmount)
    {
        EnumDemonWillType currentType = this.getCurrentType(soulGemStack);
        if (currentType != type)
        {
            return 0;
        }
        double souls = getWill(type, soulGemStack);

        double soulsDrained = Math.min(drainAmount, souls);
        setWill(type, soulGemStack, souls - soulsDrained);

        return soulsDrained;
    }

    @Override
    public int getMaxWill(EnumDemonWillType type, ItemStack soulGemStack)
    {
        if (!type.equals(getCurrentType(soulGemStack)))
        {
            return 0;
        }

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

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=petty"));
        ret.add(new ImmutablePair<Integer, String>(1, "type=lesser"));
        ret.add(new ImmutablePair<Integer, String>(2, "type=common"));
        ret.add(new ImmutablePair<Integer, String>(3, "type=greater"));
        ret.add(new ImmutablePair<Integer, String>(4, "type=grand"));
        return ret;
    }

    public EnumDemonWillType getCurrentType(ItemStack soulGemStack)
    {
        NBTHelper.checkNBT(soulGemStack);

        NBTTagCompound tag = soulGemStack.getTagCompound();

        if (!tag.hasKey(tag.getString(Constants.NBT.WILL_TYPE)))
        {
            return EnumDemonWillType.DEFAULT;
        }

        return EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE));
    }

    public void setCurrentType(EnumDemonWillType type, ItemStack soulGemStack)
    {
        NBTHelper.checkNBT(soulGemStack);

        NBTTagCompound tag = soulGemStack.getTagCompound();

        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
    }

    @Override
    public double fillWill(EnumDemonWillType type, ItemStack stack, double fillAmount)
    {
        if (!type.equals(getCurrentType(stack)))
        {
            return 0;
        }

        double current = this.getWill(type, stack);
        double maxWill = this.getMaxWill(type, stack);

        double filled = Math.min(fillAmount, maxWill - current);

        if (filled > 0)
        {
            this.setWill(type, stack, filled + current);
            return filled;
        }

        return 0;
    }
}
