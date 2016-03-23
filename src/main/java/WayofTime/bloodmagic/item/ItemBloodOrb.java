package WayofTime.bloodmagic.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.orb.BloodOrb;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.registry.OrbRegistry;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Strings;

public class ItemBloodOrb extends ItemBindableBase implements IBloodOrb, IBindable
{
    public ItemBloodOrb()
    {
        setUnlocalizedName(Constants.Mod.MODID + ".orb.");
        setRegistryName(Constants.BloodMagicItem.BLOOD_ORB.getRegName());
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + OrbRegistry.getOrb(stack.getItemDamage()).getName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < OrbRegistry.getSize(); i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (world == null)
            return super.onItemRightClick(stack, null, player, hand);

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.block_fire_extinguish, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        // SpellHelper.sendIndexedParticleToAllAround(world, posX, posY, posZ,
        // 20, world.provider.getDimensionId(), 4, posX, posY, posZ);

        if (PlayerHelper.isFakePlayer(player))
            return super.onItemRightClick(stack, world, player, hand);

        if (!stack.hasTagCompound())
        {
            return super.onItemRightClick(stack, world, player, hand);
        }

        if (Strings.isNullOrEmpty(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID)))
            return super.onItemRightClick(stack, world, player, hand);

        if (world.isRemote)
            return super.onItemRightClick(stack, world, player, hand);

        if (stack.getTagCompound().getString(Constants.NBT.OWNER_UUID).equals(PlayerHelper.getUsernameFromPlayer(player)))
            NetworkHelper.setMaxOrb(NetworkHelper.getSoulNetwork(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID)), getOrbLevel(stack.getItemDamage()));

        NetworkHelper.getSoulNetwork(stack.getTagCompound().getString(Constants.NBT.OWNER_UUID)).addLifeEssence(200, getMaxEssence(stack.getItemDamage()));
        NetworkHelper.getSoulNetwork(player).hurtPlayer(player, 200);
        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.orb.desc"));

        if (advanced)
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.orb.owner", getOrb(stack.getItemDamage()).getOwner()));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        return stack;
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

    @Override
    public BloodOrb getOrb(int meta)
    {
        return OrbRegistry.getOrb(meta);
    }

    @Override
    public int getMaxEssence(int meta)
    {
        return OrbRegistry.getOrb(meta).getCapacity();
    }

    @Override
    public int getOrbLevel(int meta)
    {
        return OrbRegistry.getOrb(meta).getTier();
    }
}
