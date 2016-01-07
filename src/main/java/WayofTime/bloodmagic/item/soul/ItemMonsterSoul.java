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
import WayofTime.bloodmagic.api.iface.ISoul;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemMonsterSoul extends Item implements ISoul
{
    public static String[] names = { "base" };

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
        tooltip.add(TextHelper.localize("tooltip.BloodMagic.souls", getSouls(stack)));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public double getSouls(ItemStack soulStack)
    {
        NBTHelper.checkNBT(soulStack);

        NBTTagCompound tag = soulStack.getTagCompound();

        return tag.getDouble(Constants.NBT.SOULS);
    }

    @Override
    public void setSouls(ItemStack soulStack, double souls)
    {
        NBTHelper.checkNBT(soulStack);

        NBTTagCompound tag = soulStack.getTagCompound();

        tag.setDouble(Constants.NBT.SOULS, souls);
    }

    @Override
    public double drainSouls(ItemStack soulStack, double drainAmount)
    {
        double souls = getSouls(soulStack);

        double soulsDrained = Math.min(drainAmount, souls);
        setSouls(soulStack, souls - soulsDrained);

        return soulsDrained;
    }

    @Override
    public ItemStack createSoul(int meta, double number)
    {
        ItemStack soulStack = new ItemStack(this, 1, meta);
        setSouls(soulStack, number);
        return soulStack;
    }
}
