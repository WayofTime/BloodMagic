package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ItemSigilWater extends ItemSigilBase
{
    public ItemSigilWater()
    {
        super("water", 100);
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
                ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, world, stack, rayTrace);
                if (ret != null)
                    return ret;

                if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    BlockPos blockpos = rayTrace.getBlockPos();

                    if (!world.isBlockModifiable(player, blockpos))
                        return super.onItemRightClick(world, player, hand);

                    if (!player.canPlayerEdit(blockpos.offset(rayTrace.sideHit), rayTrace.sideHit, stack))
                        return super.onItemRightClick(world, player, hand);

                    BlockPos blockpos1 = blockpos.offset(rayTrace.sideHit);

                    if (!player.canPlayerEdit(blockpos1, rayTrace.sideHit, stack))
                        return super.onItemRightClick(world, player, hand);

                    if (this.canPlaceWater(world, blockpos1) && NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).syphonAndDamage(player, getLpUsed()) && this.tryPlaceWater(world, blockpos1))
                        return super.onItemRightClick(world, player, hand);
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
            return EnumActionResult.FAIL;

        if (!world.canMineBlockBody(player, blockPos))
            return EnumActionResult.FAIL;

        TileEntity tile = world.getTileEntity(blockPos);
        if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
        {
            IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
            int amount = handler.fill(fluid, false);

            if (amount > 0 && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
            {
                handler.fill(fluid, true);
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.FAIL;
        }

        if (world.getBlockState(blockPos).getBlock() == Blocks.CAULDRON && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
        {
            world.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, 3));
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    public boolean canPlaceWater(World world, BlockPos blockPos)
    {
        if (!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock().getMaterial(world.getBlockState(blockPos)).isSolid())
            return false;
        else if ((world.getBlockState(blockPos).getBlock() == Blocks.WATER || world.getBlockState(blockPos).getBlock() == Blocks.FLOWING_WATER) && world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)) == 0)
            return false;
        else
            return true;
    }

    public boolean tryPlaceWater(World worldIn, BlockPos pos)
    {

        Material material = worldIn.getBlockState(pos).getBlock().getMaterial(worldIn.getBlockState(pos));
        boolean notSolid = !material.isSolid();

        if (!worldIn.isAirBlock(pos) && !notSolid)
        {
            return false;
        } else
        {
            if (worldIn.provider.doesWaterVaporize())
            {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                worldIn.playSound(null, i, j, k, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l)
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D, 0);
            } else
            {
                if (!worldIn.isRemote && notSolid && !material.isLiquid())
                    worldIn.destroyBlock(pos, true);

                worldIn.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 3);
            }

            return true;
        }
    }
}
