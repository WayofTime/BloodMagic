package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
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
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ItemSigilVoid extends ItemSigilBase implements IFluidHandlerItem {
    public ItemSigilVoid() {
        super("void", 50);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (!world.isRemote && !isUnusable(stack)) {
            RayTraceResult rayTrace = this.rayTrace(world, player, true);
			
            ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, stack, rayTrace);
            if (ret != null) return ret;
			
			if (rayTrace == null || rayTrace.typeOfHit != RayTraceResult.Type.BLOCK) {
				return ActionResult.newResult(EnumActionResult.PASS, stack);
			}
			
			BlockPos blockPos = rayTrace.getBlockPos();
			
			if(world.isBlockModifiable(player, blockPos) && player.canPlayerEdit(blockPos, rayTrace.sideHit, stack)){
				/* Case for if block at blockPos is a fluid handler like a tank
  				 * Put fluid into tank
				 */
				IFluidHandler destination = FluidUtil.getFluidHandler(world, blockPos, null);
				if(destination != null && FluidUtil.tryFluidTransfer(this, destination, 1000, false) != null && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess()) {
					//Attempt to put fluid in sidelessly first
					FluidStack result = FluidUtil.tryFluidTransfer(this, destination, 1000, true);
					if (result != null) return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
				}
				//Do the same as above, but use sidedness to interact with the fluid handler.
				IFluidHandler destinationSide = FluidUtil.getFluidHandler(world, blockPos, rayTrace.sideHit);
				if(destinationSide != null && FluidUtil.tryFluidTransfer(this, destinationSide, 1000, false) != null && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess()) {
					//Attempt to put fluid in sidelessly first
					FluidStack result = FluidUtil.tryFluidTransfer(this, destinationSide, 1000, true);
					if (result != null) return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
				}
				/*
				//Case for if block at blockPos is not a tank
				//Remove fluid from the world
				BlockPos targetPos = blockPos.offset(rayTrace.sideHit);
				IFluidHandler destination = FluidUtil.getFluidHandler(world, targetPos, rayTrace.sideHit);
				if(destination != null && FluidUtil.tryFluidTransfer(this, destination, 1000, false) != null && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess()) {
					//Attempt to put fluid in sidelessly first
					FluidStack result = FluidUtil.tryFluidTransfer(this, destination, 1000, true);
					if (result != null) return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
				}*/
			}
        }

        return super.onItemRightClick(world, player, hand);
    }
	
	public ItemStack getContainer() {
		return this.getDefaultInstance();
	}
	public FluidStack getFluid() {
		return null;
	}
	public IFluidTankProperties[] getTankProperties() {
		return new FluidTankProperties[] { new FluidTankProperties(null,Integer.MAX_VALUE, true, false) };
	}
	public int fill(FluidStack resource, boolean doFill) {
		return 1000;
	}
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return null;
	}
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return null;
	}
}
