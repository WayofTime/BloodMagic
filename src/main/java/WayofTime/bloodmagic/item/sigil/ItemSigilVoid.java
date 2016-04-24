package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.bloodmagic.api.Constants;

public class ItemSigilVoid extends ItemSigilBase
{
    public ItemSigilVoid()
    {
        super("void", 50);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote && !isUnusable(stack))
        {
            RayTraceResult rayTrace = this.rayTrace(world, player, true);

            if (rayTrace != null)
            {
                ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, stack, rayTrace);
                if (ret != null)
                    return ret;

                if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    BlockPos blockpos = rayTrace.getBlockPos();

                    if (!world.isBlockModifiable(player, blockpos))
                    {
                        return super.onItemRightClick(stack, world, player, hand);
                    }

                    if (!player.canPlayerEdit(blockpos.offset(rayTrace.sideHit), rayTrace.sideHit, stack))
                    {
                        return super.onItemRightClick(stack, world, player, hand);
                    }

                    if (!player.canPlayerEdit(blockpos, rayTrace.sideHit, stack))
                    {
                        return super.onItemRightClick(stack, world, player, hand);
                    }

                    if (world.getBlockState(blockpos).getBlock().getMaterial(world.getBlockState(blockpos)).isLiquid() && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
                    {
                        world.setBlockToAir(blockpos);
                        return super.onItemRightClick(stack, world, player, hand);
                    }
                }
            } else
            {
                return super.onItemRightClick(stack, world, player, hand);
            }

            if (!player.capabilities.isCreativeMode)
                this.setUnusable(stack, !NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()));
        }

        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || player.isSneaking() || isUnusable(stack))
        {
            return EnumActionResult.FAIL;
        }

        if (!world.canMineBlockBody(player, blockPos))
        {
            return EnumActionResult.FAIL;
        }

        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof IFluidHandler)
        {
            FluidStack amount = ((IFluidHandler) tile).drain(side, 1000, false);

            if (amount != null && amount.amount > 0 && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
            {
                ((IFluidHandler) tile).drain(side, 1000, true);
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.FAIL;
        }

        BlockPos newPos = blockPos.offset(side);

        if (!player.canPlayerEdit(newPos, side, stack))
        {
            return EnumActionResult.FAIL;
        }

        if (world.getBlockState(newPos).getBlock() instanceof IFluidBlock && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
        {
            world.setBlockToAir(newPos);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
