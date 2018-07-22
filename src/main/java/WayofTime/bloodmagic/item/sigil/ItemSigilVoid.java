package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ItemSigilVoid extends ItemSigilBase {
    public ItemSigilVoid() {
        super("void", 50);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (!world.isRemote && !isUnusable(stack)) {
            RayTraceResult rayTrace = this.rayTrace(world, player, true);

            if (rayTrace != null) {
                ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, stack, rayTrace);
                if (ret != null)
                    return ret;

                if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
                    BlockPos blockpos = rayTrace.getBlockPos();

                    if (!world.isBlockModifiable(player, blockpos)) {
                        return super.onItemRightClick(world, player, hand);
                    }

                    if (!player.canPlayerEdit(blockpos.offset(rayTrace.sideHit), rayTrace.sideHit, stack)) {
                        return super.onItemRightClick(world, player, hand);
                    }

                    if (!player.canPlayerEdit(blockpos, rayTrace.sideHit, stack)) {
                        return super.onItemRightClick(world, player, hand);
                    }

                    if (world.getBlockState(blockpos).getBlock().getMaterial(world.getBlockState(blockpos)).isLiquid() && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, SoulTicket.ITEM(stack, world, player, getLpUsed()))) {
                        world.setBlockToAir(blockpos);
                        return super.onItemRightClick(world, player, hand);
                    }
                }
            } else {
                return super.onItemRightClick(world, player, hand);
            }

            if (!player.capabilities.isCreativeMode)
                setUnusable(stack, !NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, SoulTicket.ITEM(stack, world, player, getLpUsed())));
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (PlayerHelper.isFakePlayer(player))
            return EnumActionResult.FAIL;

        if (world.isRemote || player.isSneaking() || isUnusable(stack)) {
            return EnumActionResult.FAIL;
        }

        if (!world.canMineBlockBody(player, blockPos)) {
            return EnumActionResult.FAIL;
        }

        SoulNetwork network = NetworkHelper.getSoulNetwork(getBinding(stack));
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
            IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            FluidStack amount = handler.drain(1000, false);

            if (amount != null && amount.amount > 0 && network.syphonAndDamage(player, SoulTicket.ITEM(stack, world, player, getLpUsed()))) {
                handler.drain(1000, true);
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.FAIL;
        }

        BlockPos newPos = blockPos.offset(side);

        if (!player.canPlayerEdit(newPos, side, stack)) {
            return EnumActionResult.FAIL;
        }

        if (world.getBlockState(newPos).getBlock() instanceof IFluidBlock && network.syphonAndDamage(player, SoulTicket.ITEM(stack, world, player, getLpUsed()))) {
            world.setBlockToAir(newPos);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
