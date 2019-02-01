package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.ISigilFluidItem;
import WayofTime.bloodmagic.util.SigilFluidWrapper;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;

import javax.annotation.Nullable;

public abstract class ItemSigilFluidBase extends ItemSigilBase implements ISigilFluidItem {
    //Class for sigils that interact with fluids, either creating or deleting them.
    //Sigils still have to define their own onRightClick behavior, but the actual fluid-interacting code is largely limited to here.
    public final FluidStack sigilFluid;

    public ItemSigilFluidBase(String name, int lpUsed, FluidStack fluid) {
        super(name, lpUsed);
        sigilFluid = fluid;
    }

    public ItemSigilFluidBase(String name, FluidStack fluid) {
        super(name);
        sigilFluid = fluid;
    }

    public ItemSigilFluidBase(String name) {
        super(name);
        sigilFluid = null;
    }

    //The following are handler functions for fluids, all genericized.
    //They're all based off of the Forge FluidUtil methods, but directly taking the sigilFluid constant instead of getting an argument.

    /* Gets a fluid handler for the targeted block and siding.
     * Works for both tile entity liquid containers and fluid blocks.
     * This one is literally identical to the FluidUtil method of the same signature.
     */
    @Nullable
    protected IFluidHandler getFluidHandler(World world, BlockPos blockPos, @Nullable EnumFacing side) {
        IBlockState state = world.getBlockState(blockPos);
        Block block = state.getBlock();
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile != null) {
            IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            if (handler != null)
                return handler;
        }
        if (block instanceof IFluidBlock)
            return new FluidBlockWrapper((IFluidBlock) block, world, blockPos);
        else if (block instanceof BlockLiquid)
            return new BlockLiquidWrapper((BlockLiquid) block, world, blockPos);
        return null;
    }

    /* Tries to insert fluid into a fluid handler.
     * If doTransfer is false, only simulate the transfer. If true, actually do so.
     * Returns true if the transfer is successful, false otherwise.
     */
    protected boolean tryInsertSigilFluid(IFluidHandler destination, boolean doTransfer) {
        if (destination == null)
            return false;
        return destination.fill(sigilFluid, doTransfer) > 0;
    }

    /* Tries basically the oppostive of the above, removing fluids instead of adding them
     */
    protected boolean tryRemoveFluid(IFluidHandler source, int amount, boolean doTransfer) {
        if (source == null)
            return false;
        return source.drain(amount, doTransfer) != null;
    }

    /* Tries to place a fluid block in the world.
     * Returns true if successful, otherwise false.
     * This is the big troublesome one, oddly enough.
     * It's genericized in case anyone wants to create variant sigils with weird fluids.
     */
    protected boolean tryPlaceSigilFluid(EntityPlayer player, World world, BlockPos blockPos) {

        //Make sure world coordinants are valid
        if (world == null || blockPos == null) {
            return false;
        }
        //Make sure fluid is placeable
        Fluid fluid = sigilFluid.getFluid();
        if (!fluid.canBePlacedInWorld()) {
            return false;
        }

        //Check if the block is an air block or otherwise replaceable
        IBlockState state = world.getBlockState(blockPos);
        Material mat = state.getMaterial();
        boolean isDestSolid = mat.isSolid();
        boolean isDestReplaceable = state.getBlock().isReplaceable(world, blockPos);
        if (!world.isAirBlock(blockPos) && isDestSolid && !isDestReplaceable) {
            return false;
        }

        //If the fluid vaporizes, this exists here in the lava sigil solely so the code is usable for other fluids
        if (world.provider.doesWaterVaporize() && fluid.doesVaporize(sigilFluid)) {
            fluid.vaporize(player, world, blockPos, sigilFluid);
            return true;
        }

        //Finally we've done enough checking to make sure everything at the end is safe, let's place some fluid.
        IFluidHandler handler;
        Block block = fluid.getBlock();
        if (block instanceof IFluidBlock)
            handler = new FluidBlockWrapper((IFluidBlock) block, world, blockPos);
        else if (block instanceof BlockLiquid)
            handler = new BlockLiquidWrapper((BlockLiquid) block, world, blockPos);
        else
            handler = new BlockWrapper(block, world, blockPos);
        return tryInsertSigilFluid(handler, true);
    }

    @Override
    public FluidStack getFluid(ItemStack sigil) {
        return sigilFluid;
    }

    @Override
    public int getCapacity(ItemStack sigil) {
        return 0;
    }

    @Override
    public FluidStack drain(ItemStack sigil, int maxDrain, boolean doDrain) {
        Binding binding = getBinding(sigil);

        if (binding == null)
            return null;

        SoulNetwork network = NetworkHelper.getSoulNetwork(binding);

        if (network.getCurrentEssence() < getLpUsed()) {
            network.causeNausea();
            return null;
        }

        if (doDrain)
            network.syphon(SoulTicket.item(sigil, getLpUsed()));

        return sigilFluid;
    }

    @Override
    public int fill(ItemStack sigil, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new SigilFluidWrapper(stack, this);
    }

}