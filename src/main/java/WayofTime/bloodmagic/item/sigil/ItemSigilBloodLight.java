package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.registry.ModBlocks;

public class ItemSigilBloodLight extends ItemSigilBase
{
    public ItemSigilBloodLight()
    {
        super("bloodLight", 10);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (getCooldownRemainder(stack) > 0)
            reduceCooldown(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        RayTraceResult mop = this.rayTrace(world, player, false);

        if (getCooldownRemainder(stack) > 0)
            return super.onItemRightClick(world, player, hand);

        if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockPos = mop.getBlockPos().offset(mop.sideHit);

            if (world.isAirBlock(blockPos))
            {
                world.setBlockState(blockPos, ModBlocks.BLOOD_LIGHT.getDefaultState());
                if (!world.isRemote)
                    NetworkHelper.syphonAndDamage(NetworkHelper.getSoulNetwork(player), player, getLpUsed());
                resetCooldown(stack);
                player.swingArm(hand);
                return super.onItemRightClick(world, player, hand);
            }
        } else
        {
            if (!world.isRemote)
            {
                world.spawnEntity(new EntityBloodLight(world, player));
                NetworkHelper.syphonAndDamage(NetworkHelper.getSoulNetwork(player), player, getLpUsed());
            }
            resetCooldown(stack);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return oldStack.getItem() != newStack.getItem();
    }

    public int getCooldownRemainder(ItemStack stack)
    {
        return NBTHelper.checkNBT(stack).getTagCompound().getInteger(Constants.NBT.TICKS_REMAINING);
    }

    public void reduceCooldown(ItemStack stack)
    {
        NBTHelper.checkNBT(stack).getTagCompound().setInteger(Constants.NBT.TICKS_REMAINING, getCooldownRemainder(stack) - 1);
    }

    public void resetCooldown(ItemStack stack)
    {
        NBTHelper.checkNBT(stack).getTagCompound().setInteger(Constants.NBT.TICKS_REMAINING, 10);
    }
}
