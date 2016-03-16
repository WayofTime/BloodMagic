package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.client.IVariantProvider;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDiscreteDemonWill;
import WayofTime.bloodmagic.registry.ModItems;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ItemDemonCrystal extends Item implements IDiscreteDemonWill, IVariantProvider
{
    @Getter
    private static ArrayList<String> names = new ArrayList<String>();

    public static final String CRYSTAL_DEFAULT = "crystalDefault";
    public static final String CRYSTAL_CORROSIVE = "crystalCorrosive";
    public static final String CRYSTAL_VENGEFUL = "crystalVengeful";
    public static final String CRYSTAL_DESTRUCTIVE = "crystalDestructive";
    public static final String CRYSTAL_STEADFAST = "crystalSteadfast";

    public ItemDemonCrystal()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".demonCrystal.");
        setRegistryName(Constants.BloodMagicItem.DEMON_CRYSTAL.getRegName());
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.tabBloodMagic);

        buildItemList();
    }

    private void buildItemList()
    {
        names.add(0, CRYSTAL_DEFAULT);
        names.add(1, CRYSTAL_CORROSIVE);
        names.add(2, CRYSTAL_DESTRUCTIVE);
        names.add(3, CRYSTAL_VENGEFUL);
        names.add(4, CRYSTAL_STEADFAST);
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
        return new ItemStack(ModItems.itemDemonCrystal, 1, names.indexOf(name));
    }

    @Override
    public double getWill(ItemStack willStack)
    {
        return getDiscretization(willStack) * willStack.stackSize;
    }

    @Override
    public double drainWill(ItemStack willStack, double drainAmount)
    {
        double discretization = getDiscretization(willStack);
        int drainedNumber = (int) Math.floor(Math.min(willStack.stackSize * discretization, drainAmount) / discretization);

        if (drainedNumber > 0)
        {
            willStack.stackSize -= drainedNumber;
            return drainedNumber * discretization;
        }

        return 0;
    }

    @Override
    public double getDiscretization(ItemStack willStack)
    {
        return 10;
    }

    @Override
    public EnumDemonWillType getType(ItemStack willStack)
    {
        return EnumDemonWillType.values()[MathHelper.clamp_int(willStack.getMetadata(), 0, EnumDemonWillType.values().length - 1)];
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (String name : names)
            ret.add(new ImmutablePair<Integer, String>(names.indexOf(name), "type=" + name));
        return ret;
    }
}
