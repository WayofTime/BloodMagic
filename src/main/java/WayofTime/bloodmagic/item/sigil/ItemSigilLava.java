package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ItemSigilLava extends ItemSigilBase
{
    public ItemSigilLava()
    {
        super("lava", 1000);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (!world.isRemote && !isUnusable(stack))
        {
            RayTraceResult rayTrace = this.rayTrace(world, player, false);

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
                        return super.onItemRightClick(world, player, hand);
                    }

                    if (!player.canPlayerEdit(blockpos.offset(rayTrace.sideHit), rayTrace.sideHit, stack))
                    {
                        return super.onItemRightClick(world, player, hand);
                    }

                    BlockPos blockpos1 = blockpos.offset(rayTrace.sideHit);

                    if (!player.canPlayerEdit(blockpos1, rayTrace.sideHit, stack))
                    {
                        return super.onItemRightClick(world, player, hand);
                    }

                    if (this.canPlaceLava(world, blockpos1) && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()) && this.tryPlaceLava(world, blockpos1))
                    {
                        return super.onItemRightClick(world, player, hand);
                    }
                }
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote || player.isSneaking() || isUnusable(stack))
        {
            return EnumActionResult.FAIL;
        }
        if (!world.canMineBlockBody(player, blockPos))
        {
            return EnumActionResult.FAIL;
        }

        TileEntity tile = world.getTileEntity(blockPos);
        if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
        {
            IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            FluidStack fluid = new FluidStack(FluidRegistry.LAVA, 1000);
            int amount = handler.fill(fluid, false);

            if (amount > 0 && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
            {
                handler.fill(fluid, true);
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
        } else if ((world.getBlockState(blockPos).getBlock() == Blocks.LAVA || world.getBlockState(blockPos).getBlock() == Blocks.FLOWING_LAVA) && world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)) == 0)
        {
            return false;
        } else
        {
            world.setBlockState(blockPos, Blocks.FLOWING_LAVA.getBlockState().getBaseState(), 3);
            return true;
        }
    }

    public boolean tryPlaceLava(World world, BlockPos pos)
    {
        Material material = world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos));

        return world.isAirBlock(pos) && !material.isSolid();
    }
}
