package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.BindableHelper;
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

public class ItemSigilVoid extends ItemSigilBase {

    public ItemSigilVoid() {
        super("void", 50);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && !isUnusable(stack)) {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

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

                    if (!player.canPlayerEdit(blockpos, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }

                    if (world.getBlockState(blockpos).getBlock().getMaterial().isLiquid() && syphonBatteries(stack, player, getEnergyUsed())) {
                        world.setBlockToAir(blockpos);
                        return stack;
                    }
                }
            }
            else {
                return stack;
            }

            if (!player.capabilities.isCreativeMode)
                this.setUnusable(stack, !syphonBatteries(stack, player, getEnergyUsed()));
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
            FluidStack amount = ((IFluidHandler) tile).drain(side, 1000, false);

            if (amount != null && amount.amount > 0 && syphonBatteries(stack, player, getEnergyUsed())) {
                ((IFluidHandler) tile).drain(side, 1000, true);
                return true;
            }

            return false;
        }

        {
            int x = blockPos.getX();
            int y = blockPos.getY();
            int z = blockPos.getZ();

            if (side.getIndex() == 0) --y;
            if (side.getIndex() == 1) ++y;
            if (side.getIndex() == 2) --z;
            if (side.getIndex() == 3) ++z;
            if (side.getIndex() == 4) --x;
            if (side.getIndex() == 5) ++x;

            if (!player.canPlayerEdit(new BlockPos(x, y, z), side, stack)) {
                return false;
            }

            if (world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof IFluidBlock && syphonBatteries(stack, player, getEnergyUsed())) {
                world.setBlockToAir(new BlockPos(x, y, z));
                return true;
            }
        }

        return false;
    }
}
