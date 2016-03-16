package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.registry.ModBlocks;

public class ItemSigilBloodLight extends ItemSigilBase
{
    public ItemSigilBloodLight()
    {
        super("bloodLight", 10);
        setRegistryName(Constants.BloodMagicItem.SIGIL_BLOOD_LIGHT.getRegName());
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (getCooldownRemainder(stack) > 0)
            reduceCooldown(stack);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (getCooldownRemainder(stack) > 0)
            return stack;

        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            BlockPos blockPos = mop.getBlockPos().offset(mop.sideHit);

            if (world.isAirBlock(blockPos))
            {
                world.setBlockState(blockPos, ModBlocks.bloodLight.getDefaultState());
                if (!world.isRemote)
                    NetworkHelper.syphonAndDamage(NetworkHelper.getSoulNetwork(player), player, getLPUsed());
                resetCooldown(stack);
                player.swingItem();
                return stack;
            }
        } else
        {
            if (!world.isRemote)
            {
                world.spawnEntityInWorld(new EntityBloodLight(world, player));
                NetworkHelper.syphonAndDamage(NetworkHelper.getSoulNetwork(player), player, getLPUsed());
            }
            resetCooldown(stack);
        }

        return stack;
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
