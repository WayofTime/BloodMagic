package WayofTime.bloodmagic.item.sigil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
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
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote && !isUnusable(stack))
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

            if (movingobjectposition != null)
            {
                ItemStack ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, world, stack, movingobjectposition);
                if (ret != null)
                    return ret;

                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    BlockPos blockpos = movingobjectposition.getBlockPos();

                    if (!world.isBlockModifiable(player, blockpos))
                    {
                        return stack;
                    }

                    if (!player.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, stack))
                    {
                        return stack;
                    }

                    if (!player.canPlayerEdit(blockpos, movingobjectposition.sideHit, stack))
                    {
                        return stack;
                    }

                    if (world.getBlockState(blockpos).getBlock().getMaterial().isLiquid() && syphonNetwork(stack, player, getLPUsed()))
                    {
                        world.setBlockToAir(blockpos);
                        return stack;
                    }
                }
            } else
            {
                return stack;
            }

            if (!player.capabilities.isCreativeMode)
                this.setUnusable(stack, !syphonNetwork(stack, player, getLPUsed()));
        }

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        super.onItemUse(stack, player, world, blockPos, side, hitX, hitY, hitZ);

        if (world.isRemote || player.isSneaking() || isUnusable(stack))
        {
            return false;
        }

        if (!world.canMineBlockBody(player, blockPos))
        {
            return false;
        }

        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof IFluidHandler)
        {
            FluidStack amount = ((IFluidHandler) tile).drain(side, 1000, false);

            if (amount != null && amount.amount > 0 && syphonNetwork(stack, player, getLPUsed()))
            {
                ((IFluidHandler) tile).drain(side, 1000, true);
                return true;
            }

            return false;
        }

        BlockPos newPos = blockPos.offset(side);

        if (!player.canPlayerEdit(newPos, side, stack))
        {
            return false;
        }

        if (world.getBlockState(newPos).getBlock() instanceof IFluidBlock && syphonNetwork(stack, player, getLPUsed()))
        {
            world.setBlockToAir(newPos);
            return true;
        }

        return false;
    }
}
