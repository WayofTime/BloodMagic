package WayofTime.bloodmagic.item.sigil;

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
        setRegistryName(Constants.BloodMagicItem.SIGIL_VOID.getRegName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote && !isUnusable(stack))
        {
            RayTraceResult movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

            if (movingobjectposition != null)
            {
                ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, stack, movingobjectposition);
                if (ret != null)
                    return ret;

                if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    BlockPos blockpos = movingobjectposition.getBlockPos();

                    if (!world.isBlockModifiable(player, blockpos))
                    {
                        return super.onItemRightClick(stack, world, player, hand);
                    }

                    if (!player.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, stack))
                    {
                        return super.onItemRightClick(stack, world, player, hand);
                    }

                    if (!player.canPlayerEdit(blockpos, movingobjectposition.sideHit, stack))
                    {
                        return super.onItemRightClick(stack, world, player, hand);
                    }

                    if (world.getBlockState(blockpos).getBlock().getMaterial(world.getBlockState(blockpos)).isLiquid() && syphonNetwork(stack, player, getLPUsed()))
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
                this.setUnusable(stack, !syphonNetwork(stack, player, getLPUsed()));
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

            if (amount != null && amount.amount > 0 && syphonNetwork(stack, player, getLPUsed()))
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

        if (world.getBlockState(newPos).getBlock() instanceof IFluidBlock && syphonNetwork(stack, player, getLPUsed()))
        {
            world.setBlockToAir(newPos);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
