package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.bloodmagic.api.Constants;

public class ItemSigilLava extends ItemSigilBase
{
    public ItemSigilLava()
    {
        super("lava", 1000);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote && !isUnusable(stack))
        {
            RayTraceResult movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

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

                    BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);

                    if (!player.canPlayerEdit(blockpos1, movingobjectposition.sideHit, stack))
                    {
                        return super.onItemRightClick(stack, world, player, hand);
                    }

                    if (this.canPlaceLava(world, blockpos1) && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()) && this.tryPlaceLava(world, blockpos1))
                    {
                        return super.onItemRightClick(stack, world, player, hand);
                    }
                }
            }
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
            FluidStack fluid = new FluidStack(FluidRegistry.LAVA, 1000);
            int amount = ((IFluidHandler) tile).fill(side, fluid, false);

            if (amount > 0 && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
            {
                ((IFluidHandler) tile).fill(side, fluid, true);
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.FAIL;
        }

        return EnumActionResult.FAIL;
    }

    public boolean canPlaceLava(World world, BlockPos blockPos)
    {
        if (!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock().getMaterial(world.getBlockState(blockPos)).isSolid())
        {
            return false;
        } else if ((world.getBlockState(blockPos).getBlock() == Blocks.lava || world.getBlockState(blockPos).getBlock() == Blocks.flowing_lava) && world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)) == 0)
        {
            return false;
        } else
        {
            world.setBlockState(blockPos, Blocks.flowing_lava.getBlockState().getBaseState(), 3);
            return true;
        }
    }

    public boolean tryPlaceLava(World world, BlockPos pos)
    {
        Material material = world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos));

        return world.isAirBlock(pos) && !material.isSolid();
    }
}
