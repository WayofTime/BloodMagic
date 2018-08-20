package WayofTime.bloodmagic.item.sigil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class ItemSigilFluidBase extends ItemSigilBase {
    //Class for sigils that interact with fluids, either creating or deleting them.
    //Sigils still have to define their own onRightClick behavior, but the actual fluid-interacting code is largely limited to here.
    public final FluidStack sigilFluid;
    
    public ItemSigilFluidBase(String name, int lpUsed, FluidStack fluid){
        super(name, lpUsed);
        sigilFluid = fluid;
    }
    public ItemSigilFluidBase(String name){
        super(name);
        sigilFluid = null;
    }
    
    //The following are handler functions for fluids, all genericized.
    //They're all based off of the Forge FluidUtil methods, but directly taking the sigilFluid constant instead of getting an argument.
    
    /* Gets a fluid handler for the targeted block and siding.
     * Works for both tile entity liquid containers and fluid blocks.
     * This one is literally identical to the FluidUtil method of the same signature.
     */
    protected IFluidHandler getFluidHandler(World world, BlockPos blockPos, EnumFacing side) {
        IBlockState state = world.getBlockState(blockPos);
        Block block = state.getBlock();
        
        if (block.hasTileEntity(state)) {
            TileEntity tile = world.getTileEntity(blockPos);
            if(tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
                return tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
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
        if(destination == null)
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
        if (!fluid.canBePlacedInWorld()){
            return false;
        }
        
        //Check if the block is an air block or otherwise replaceable
        IBlockState state = world.getBlockState(blockPos);
        Material mat = state.getMaterial();
        boolean isDestSolid = mat.isSolid();
        boolean isDestReplaceable = state.getBlock().isReplaceable(world, blockPos);
        if(!world.isAirBlock(blockPos) && isDestSolid && !isDestReplaceable){
            return false;
        }
        
        //If the fluid vaporizes, this exists here in the lava sigil solely so the code is usable for other fluids
        if(world.provider.doesWaterVaporize() && fluid.doesVaporize(sigilFluid)) {
            fluid.vaporize(player, world, blockPos, sigilFluid);
            return true;
        }
        
        //Finally we've done enough checking to make sure everything at the end is safe, let's place some fluid.
        IFluidHandler handler;
        Block block = fluid.getBlock();
        if (block instanceof IFluidBlock)
            handler = new FluidBlockWrapper((IFluidBlock) block, world, blockPos);
        else if(block instanceof BlockLiquid)
            handler = new BlockLiquidWrapper((BlockLiquid) block, world, blockPos);
        else
            handler = new BlockWrapper(block, world, blockPos);
        System.out.print(handler);
        return tryInsertSigilFluid(handler, true);
    }
}