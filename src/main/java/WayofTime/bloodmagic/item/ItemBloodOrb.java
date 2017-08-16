package WayofTime.bloodmagic.item;

import java.util.List;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Strings;

import javax.annotation.Nullable;

public class ItemBloodOrb extends ItemBindableBase implements IBloodOrb, IBindable
{
    public ItemBloodOrb()
    {
        setUnlocalizedName(BloodMagic.MODID + ".orb");
        this.setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        BloodOrb orb = getOrb(stack);
        if (orb == null)
            return super.getUnlocalizedName(stack);

        return super.getUnlocalizedName(stack) + "." + orb.getName();
    }

    @Override
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list)
    {
        if (!isInCreativeTab(creativeTab))
            return;

        for (BloodOrb orb : RegistrarBloodMagic.BLOOD_ORBS) {
            ItemStack orbStack = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("orb", orb.getRegistryName().toString());
            orbStack.setTagCompound(tag);
            list.add(orbStack);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        BloodOrb orb = getOrb(stack);

        if (orb == null)
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (world == null)
            return super.onItemRightClick(world, player, hand);

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        if (PlayerHelper.isFakePlayer(player))
            return super.onItemRightClick(world, player, hand);

        if (!stack.hasTagCompound())
        {
            return super.onItemRightClick(world, player, hand);
        }

        if (Strings.isNullOrEmpty(getOwnerUUID(stack)))
            return super.onItemRightClick(world, player, hand);

        if (world.isRemote)
            return super.onItemRightClick(world, player, hand);

        if (getOwnerUUID(stack).equals(PlayerHelper.getUsernameFromPlayer(player)))
            NetworkHelper.setMaxOrb(NetworkHelper.getSoulNetwork(getOwnerUUID(stack)), orb.getTier());

        NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).add(200, orb.getCapacity());
        NetworkHelper.getSoulNetwork(player).hurtPlayer(player, 200);
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.orb.desc"));

        BloodOrb orb = getOrb(stack);
        if (flag.isAdvanced() && orb != null)
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.orb.owner", orb.getRegistryName()));

        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        return stack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return true;
    }

    // IBindable

    @Override
    public boolean onBind(EntityPlayer player, ItemStack stack)
    {
        return true;
    }

    // IBloodOrb

    @Nullable
    @Override
    public BloodOrb getOrb(ItemStack stack) {
        if (!stack.hasTagCompound())
            return null;

        ResourceLocation id = new ResourceLocation(stack.getTagCompound().getString("orb"));
        return RegistrarBloodMagic.BLOOD_ORBS.getValue(id);
    }
}
