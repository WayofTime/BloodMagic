package WayofTime.bloodmagic.item.sigil.sigil;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;

import javax.annotation.Nonnull;

public class SigilFluid implements ISigil {

    private final Fluid fluid;

    public SigilFluid(Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public int getCost() {
        return 1000;
    }

    @Override
    public EnumActionResult onInteract(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull EnumHand hand) {
        if (world.isRemote)
            return EnumActionResult.FAIL;

        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
            IFluidHandler fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            FluidStack fluid = new FluidStack(this.fluid, 1000);
            int amount = fluidHandler.fill(fluid, false);
            if (amount > 0 && NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).syphonAndDamage(player, getCost())) {
                fluidHandler.fill(fluid, true);
                return EnumActionResult.SUCCESS;
            }
        }

        if (fluid.getBlock() == null)
            return EnumActionResult.FAIL;

        IBlockState placeAtState = world.getBlockState(pos);
        if (!placeAtState.getBlock().isReplaceable(world, pos))
            placeAtState = world.getBlockState(pos = pos.offset(side));

        if (!placeAtState.getBlock().isReplaceable(world, pos))
            return EnumActionResult.FAIL;

        Block fluidBlock = fluid.getBlock();
        IFluidHandler fluidHandler = null;
        if (fluidBlock instanceof IFluidBlock)
            fluidHandler = new FluidBlockWrapper((IFluidBlock) fluidBlock, world, pos);
        else if (fluidBlock instanceof BlockLiquid)
            fluidHandler = new BlockLiquidWrapper((BlockLiquid) fluidBlock, world, pos);

        if (fluidHandler != null) {
            fluidHandler.fill(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true);
            NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).syphonAndDamage(player, getCost());
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
