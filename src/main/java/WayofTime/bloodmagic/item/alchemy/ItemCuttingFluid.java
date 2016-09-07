package WayofTime.bloodmagic.item.alchemy;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.ICustomAlchemyConsumable;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemCuttingFluid extends Item implements IVariantProvider, ICustomAlchemyConsumable
{
    @Getter
    private static ArrayList<String> names = new ArrayList<String>();

    public static final String BASIC = "basicCuttingFluid";
    public static final String EXPLOSIVE = "explosive";

    public ItemCuttingFluid()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".cuttingFluid.");
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.tabBloodMagic);
        setMaxStackSize(1);

        buildItemList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        if (!stack.hasTagCompound())
            return;
        int max = getMaxUsesForFluid(stack);
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.cuttingFluidRatio", max - getDamageOfFluid(stack), max));
    }

    private void buildItemList()
    {
        names.add(0, BASIC);
        names.add(1, EXPLOSIVE);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names.get(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(id, 1, i));
    }

    public static ItemStack getStack(String name)
    {
        return new ItemStack(ModItems.cuttingFluid, 1, names.indexOf(name));
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (String name : names)
            ret.add(new ImmutablePair<Integer, String>(names.indexOf(name), "type=" + name));
        return ret;
    }

    public int getDamageOfFluid(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        return tag.getInteger("used");
    }

    public void applyDamageToFluid(ItemStack stack)
    {
        int damage = Math.min(getDamageOfFluid(stack) + 1, getMaxUsesForFluid(stack));
        NBTTagCompound tag = stack.getTagCompound();

        tag.setInteger("used", damage);
    }

    public int getMaxUsesForFluid(ItemStack stack)
    {
        switch (stack.getMetadata())
        {
        case 0:
            return 16;
        case 1:
            return 64;
        default:
            return 1;
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return (double) (getDamageOfFluid(stack)) / (double) (getMaxUsesForFluid(stack));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return getDamageOfFluid(stack) > 0;
    }

    @Override
    public ItemStack drainUseOnAlchemyCraft(ItemStack stack)
    {
        applyDamageToFluid(stack);
        if (getDamageOfFluid(stack) >= getMaxUsesForFluid(stack))
        {
            return null;
        }

        return stack;
    }
}
