package WayofTime.bloodmagic.item.soul;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ItemMonsterSoul extends Item implements IDemonWill, IVariantProvider
{
    public static String[] names = { "base", "corrosive", "destructive", "vengeful", "steadfast" };

    public ItemMonsterSoul()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".monsterSoul.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHasSubtypes(true);
        setMaxStackSize(1);
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
        if (!stack.hasTagCompound())
            return;
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.will", getWill(getType(stack), stack)));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public EnumDemonWillType getType(ItemStack stack)
    {
        return EnumDemonWillType.values()[stack.getItemDamage() % 5];
    }

    @Override
    public double getWill(EnumDemonWillType type, ItemStack soulStack)
    {
        if (type != this.getType(soulStack))
        {
            return 0;
        }

        NBTHelper.checkNBT(soulStack);

        NBTTagCompound tag = soulStack.getTagCompound();

        return tag.getDouble(Constants.NBT.SOULS);
    }

    @Override
    public void setWill(EnumDemonWillType type, ItemStack soulStack, double souls)
    {
        NBTHelper.checkNBT(soulStack);

        NBTTagCompound tag = soulStack.getTagCompound();

        soulStack.setItemDamage(type.ordinal());

        tag.setDouble(Constants.NBT.SOULS, souls);
    }

    @Override
    public double drainWill(EnumDemonWillType type, ItemStack soulStack, double drainAmount)
    {
        double souls = getWill(type, soulStack);

        double soulsDrained = Math.min(drainAmount, souls);
        setWill(type, soulStack, souls - soulsDrained);

        return soulsDrained;
    }

    @Override
    public ItemStack createWill(int meta, double number)
    {
        ItemStack soulStack = new ItemStack(this, 1, meta % 5);
        setWill(getType(soulStack), soulStack, number);
        return soulStack;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < names.length; i++)
        {
            String name = names[i];
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + name));
        }
        return ret;
    }

    @Override
    public double getWill(ItemStack willStack)
    {
        return this.getWill(EnumDemonWillType.DEFAULT, willStack);
    }

    @Override
    public void setWill(ItemStack willStack, double will)
    {
        this.setWill(EnumDemonWillType.DEFAULT, willStack, will);
    }

    @Override
    public double drainWill(ItemStack willStack, double drainAmount)
    {
        return this.drainWill(EnumDemonWillType.DEFAULT, willStack, drainAmount);
    }
}
