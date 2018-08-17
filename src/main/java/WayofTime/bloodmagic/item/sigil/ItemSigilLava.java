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
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidHandler;


public class ItemSigilLava extends ItemSigilBase implements IFluidHandlerItem {
    public ItemSigilLava() {
        super("lava", 1000);
    }
	
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return ActionResult.newResult(EnumActionResult.FAIL, stack);

        if (!world.isRemote && !isUnusable(stack)) {
            RayTraceResult rayTrace = this.rayTrace(world, player, false);
			
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
				if(destination != null && FluidUtil.tryFluidTransfer(destination, this, getFluid(), false) != null && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess()) {
					//Attempt to put fluid in sidelessly first
					FluidStack result = FluidUtil.tryFluidTransfer(destination, this, this.getFluid(), true);
					if (result != null) return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
				}
				//Do the same as above, but use sidedness to interact with the fluid handler.
				IFluidHandler destinationSide = FluidUtil.getFluidHandler(world, blockPos, rayTrace.sideHit);
				if(destinationSide != null && FluidUtil.tryFluidTransfer(destinationSide, this, getFluid(), false) != null && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess()) {
					//Attempt to put fluid in sidelessly first
					FluidStack result = FluidUtil.tryFluidTransfer(destinationSide, this, this.getFluid(), true);
					if (result != null) return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
				}
				//Case for if block at blockPos is not a tank
				//Place fluid in world
				if (destination == null && destinationSide == null){
					BlockPos targetPos = blockPos.offset(rayTrace.sideHit);
					if (FluidUtil.tryPlaceFluid(player, world, targetPos, this, this.getFluid()) && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess()){
						return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
					}
				}
			}
        }

        return super.onItemRightClick(world, player, hand);
    }
	
	//Required interface functions for IFluidHandlerItem
	public ItemStack getContainer() {
		return this.getDefaultInstance();
	}
	public FluidStack getFluid() {
		return new FluidStack(FluidRegistry.LAVA, 1000);
	}
	public IFluidTankProperties[] getTankProperties() {
		return new FluidTankProperties[] { new FluidTankProperties(this.getFluid(),1000) };
	}
	public int fill(FluidStack resource, boolean doFill) {
		return 0;
	}
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return this.getFluid();
	}
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return this.getFluid();
	}
	/*
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidSigilHandler(stack, (IFluidHandlerItem) this, false, true);
	}
	*/
}
