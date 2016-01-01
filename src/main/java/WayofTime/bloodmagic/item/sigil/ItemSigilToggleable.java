package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.item.ItemBindable;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSigilToggleable extends ItemSigilBase
{
    public ItemSigilToggleable(String name, int lpUsed)
    {
        super(name, lpUsed);
        setToggleable();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, player, tooltip, advanced);
        if (getActivated(stack))
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.activated"));
        else
            tooltip.add(TextHelper.localize("tooltip.BloodMagic.deactivated"));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote && !isUnusable(stack))
        {
            if (player.isSneaking())
                setActivated(stack, !getActivated(stack));
            if (getActivated(stack) && ItemBindable.syphonNetwork(stack, player, getLPUsed()))
                return stack;
        }

        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (BindableHelper.checkAndSetItemOwner(stack, player) && ItemBindable.syphonNetwork(stack, player, getLPUsed()))
            return onSigilUseFirst(stack, player, world, blockPos, side, hitX, hitY, hitZ);

        return false;
    }

    public boolean onSigilUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote && entityIn instanceof EntityPlayerMP && getActivated(stack))
        {
            if (worldIn.getWorldTime() % 100 == 0)
            {
                if (!ItemBindable.syphonNetwork(stack, (EntityPlayer) entityIn, getLPUsed()))
                {
                    setActivated(stack, false);
                }
            }

            onSigilUpdate(stack, worldIn, (EntityPlayer) entityIn, itemSlot, isSelected);
        }
    }

    public void onSigilUpdate(ItemStack stack, World world, EntityPlayer player, int itemSlot, boolean isSelected)
    {
    }
}
