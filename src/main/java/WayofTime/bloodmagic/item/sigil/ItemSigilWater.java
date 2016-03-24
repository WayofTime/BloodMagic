package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
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
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.bloodmagic.api.Constants;

public class ItemSigilWater extends ItemSigilBase
{
    public ItemSigilWater()
    {
        super("water", 100);
        setRegistryName(Constants.BloodMagicItem.SIGIL_WATER.getRegName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote && !isUnusable(stack))
        {
            RayTraceResult movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

            if (movingobjectposition != null)
            {
                ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, world, stack, movingobjectposition);
                if (ret != null)
                    return ret;

                if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    BlockPos blockpos = movingobjectposition.getBlockPos();

                    if (!world.isBlockModifiable(player, blockpos))
                        return super.onItemRightClick(stack, world, player, hand);

                    if (!player.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, stack))
                        return super.onItemRightClick(stack, world, player, hand);

                    BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);

                    if (!player.canPlayerEdit(blockpos1, movingobjectposition.sideHit, stack))
                        return super.onItemRightClick(stack, world, player, hand);

                    if (this.canPlaceWater(world, blockpos1) && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()) && this.tryPlaceWater(world, blockpos1))
                        return super.onItemRightClick(stack, world, player, hand);
                }
            }
        }

        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || player.isSneaking() || isUnusable(stack))
            return EnumActionResult.FAIL;

        if (!world.canMineBlockBody(player, blockPos))
            return EnumActionResult.FAIL;

        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof IFluidHandler)
        {
            FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
            int amount = ((IFluidHandler) tile).fill(side, fluid, false);

            if (amount > 0 && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
                ((IFluidHandler) tile).fill(side, fluid, true);
            return EnumActionResult.FAIL;
        }

        if (world.getBlockState(blockPos).getBlock() == Blocks.cauldron && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()))
        {
            world.setBlockState(blockPos, Blocks.cauldron.getDefaultState().withProperty(BlockCauldron.LEVEL, 3));
            return EnumActionResult.SUCCESS;
        }

        BlockPos newPos = blockPos.offset(side);
        return (player.canPlayerEdit(newPos, side, stack) && this.canPlaceWater(world, newPos) && NetworkHelper.getSoulNetwork(player).syphonAndDamage(player, getLpUsed()) && this.tryPlaceWater(world, newPos)) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
    }

    public boolean canPlaceWater(World world, BlockPos blockPos)
    {
        if (!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock().getMaterial(world.getBlockState(blockPos)).isSolid())
            return false;
        else if ((world.getBlockState(blockPos).getBlock() == Blocks.water || world.getBlockState(blockPos).getBlock() == Blocks.flowing_water) && world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)) == 0)
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
                worldIn.playSound((EntityPlayer) null, i, j, k, SoundEvents.block_fire_extinguish, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l)
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D, 0);
            } else
            {
                if (!worldIn.isRemote && notSolid && !material.isLiquid())
                    worldIn.destroyBlock(pos, true);

                worldIn.setBlockState(pos, Blocks.flowing_water.getDefaultState(), 3);
            }

            return true;
        }
    }
}
