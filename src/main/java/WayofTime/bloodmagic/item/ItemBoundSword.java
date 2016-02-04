package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Strings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBoundSword extends ItemSword implements IBindable
{
    public ItemBoundSword()
    {
        super(ModItems.boundToolMaterial);

        setUnlocalizedName(Constants.Mod.MODID + ".bound.sword");
        setRegistryName(Constants.BloodMagicItem.BOUND_SWORD.getRegName());
        setHasSubtypes(true);
        setNoRepair();
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!player.isSneaking() && getActivated(stack))
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));

        if (player.isSneaking())
            setActivated(stack, !getActivated(stack));

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        NBTHelper.checkNBT(stack);

        if (StatCollector.canTranslate("tooltip.BloodMagic.bound.sword.desc"))
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.bound.sword.desc"));

        tooltip.add(TextHelper.localize("tooltip.BloodMagic." + (getActivated(stack) ? "activated" : "deactivated")));

        if (!Strings.isNullOrEmpty(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID)))
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.currentOwner", PlayerHelper.getUsernameFromStack(stack)));
    }

    public boolean getActivated(ItemStack stack)
    {
        return stack.getItemDamage() > 0;
    }

    private ItemStack setActivated(ItemStack stack, boolean activated)
    {
        stack.setItemDamage(activated ? 1 : 0);

        return stack;
    }

    // IBindable

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack)
    {
        return true;
    }
}
