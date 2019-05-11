package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.entity.projectile.EntityBloodLight;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemSigilBloodLight extends ItemSigilBase {
    public ItemSigilBloodLight() {
        super("blood_light", 10);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (getCooldownRemainder(stack) > 0)
            reduceCooldown(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        RayTraceResult mop = this.rayTrace(world, player, false);

        if (getCooldownRemainder(stack) > 0)
            return super.onItemRightClick(world, player, hand);

        if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = mop.getBlockPos().offset(mop.sideHit);

            if (world.isAirBlock(blockPos)) {
                world.setBlockState(blockPos, RegistrarBloodMagicBlocks.BLOOD_LIGHT.getDefaultState());
                if (!world.isRemote) {
                    SoulNetwork network = NetworkHelper.getSoulNetwork(getBinding(stack));
                    network.syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed()));
                }
                resetCooldown(stack);
                player.swingArm(hand);
                return super.onItemRightClick(world, player, hand);
            }
        } else {
            if (!world.isRemote) {
                SoulNetwork network = NetworkHelper.getSoulNetwork(getBinding(stack));
                world.spawnEntity(new EntityBloodLight(world, player));
                network.syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed()));
            }
            resetCooldown(stack);
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    public int getCooldownRemainder(ItemStack stack) {
        return NBTHelper.checkNBT(stack).getTagCompound().getInteger(Constants.NBT.TICKS_REMAINING);
    }

    public void reduceCooldown(ItemStack stack) {
        NBTHelper.checkNBT(stack).getTagCompound().setInteger(Constants.NBT.TICKS_REMAINING, getCooldownRemainder(stack) - 1);
    }

    public void resetCooldown(ItemStack stack) {
        NBTHelper.checkNBT(stack).getTagCompound().setInteger(Constants.NBT.TICKS_REMAINING, 10);
    }
}
