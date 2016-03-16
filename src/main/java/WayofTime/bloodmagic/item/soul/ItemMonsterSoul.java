package WayofTime.bloodmagic.item.soul;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.client.IVariantProvider;
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
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ItemMonsterSoul extends Item implements IDemonWill, IVariantProvider
{
    public static String[] names = { "base" };

    public ItemMonsterSoul()
    {
        super();

        setUnlocalizedName(Constants.Mod.MODID + ".monsterSoul.");
        setRegistryName(Constants.BloodMagicItem.MONSTER_SOUL.getRegName());
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
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.will", getWill(stack)));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public double getWill(ItemStack soulStack)
    {
        NBTHelper.checkNBT(soulStack);

        NBTTagCompound tag = soulStack.getTagCompound();

        return tag.getDouble(Constants.NBT.SOULS);
    }

    @Override
    public void setWill(ItemStack soulStack, double souls)
    {
        NBTHelper.checkNBT(soulStack);

        NBTTagCompound tag = soulStack.getTagCompound();

        tag.setDouble(Constants.NBT.SOULS, souls);
    }

    @Override
    public double drainWill(ItemStack soulStack, double drainAmount)
    {
        double souls = getWill(soulStack);

        double soulsDrained = Math.min(drainAmount, souls);
        setWill(soulStack, souls - soulsDrained);

        return soulsDrained;
    }

    @Override
    public ItemStack createWill(int meta, double number)
    {
        ItemStack soulStack = new ItemStack(this, 1, meta);
        setWill(soulStack, number);
        return soulStack;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=monstersoul"));
        return ret;
    }
}
