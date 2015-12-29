package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class ItemSigilLava extends ItemSigilBase {

    public ItemSigilLava() {
        super("lava", 1000);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && !isUnusable(stack)) {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

            if (movingobjectposition != null) {
                ItemStack ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, world, stack, movingobjectposition);
                if (ret != null) return ret;

                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    BlockPos blockpos = movingobjectposition.getBlockPos();

                    if (!world.isBlockModifiable(player, blockpos)) {
                        return stack;
                    }

                    if (!player.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, stack)) {
                        return stack;
                    }


                    BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);

                    if (!player.canPlayerEdit(blockpos1, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }

                    if (this.canPlaceLava(world, blockpos1) && syphonBatteries(stack, player, getLPUsed()) && this.tryPlaceLava(world, blockpos1)) {
                        return stack;
                    }
                }
            }

            if (!player.capabilities.isCreativeMode)
                this.setUnusable(stack, !syphonBatteries(stack, player, getLPUsed()));
        }

        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote || !BindableHelper.checkAndSetItemOwner(stack, player) || player.isSneaking() || isUnusable(stack)) {
            return false;
        }
        if (!world.canMineBlockBody(player, blockPos)) {
            return false;
        }

        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof IFluidHandler) {
            FluidStack fluid = new FluidStack(FluidRegistry.LAVA, 1000);
            int amount = ((IFluidHandler) tile).fill(side, fluid, false);

            if (amount > 0 && syphonBatteries(stack, player, getLPUsed())) {
                ((IFluidHandler) tile).fill(side, fluid, true);
            }

            return false;
        }
//        else if (tile instanceof TESocket) {
//            return false;
//        }

        BlockPos newPos = blockPos.offset(side);

        if (!player.canPlayerEdit(newPos, side, stack)) {
            return false;
        }

        if (this.canPlaceLava(world, newPos) && syphonBatteries(stack, player, getLPUsed())) {
            return this.tryPlaceLava(world, newPos);
        }

        return false;
    }

    public boolean canPlaceLava(World world, BlockPos blockPos) {
        if (!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock().getMaterial().isSolid()) {
            return false;
        } else if ((world.getBlockState(blockPos).getBlock() == Blocks.lava || world.getBlockState(blockPos).getBlock() == Blocks.flowing_lava) && world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)) == 0) {
            return false;
        } else {
            world.setBlockState(blockPos, Blocks.lava.getBlockState().getBaseState(), 3);
            return true;
        }
    }

    public boolean tryPlaceLava(World worldIn, BlockPos pos) {
        Material material = worldIn.getBlockState(pos).getBlock().getMaterial();

        return worldIn.isAirBlock(pos) && !material.isSolid();
    }
}
