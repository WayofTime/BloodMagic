package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ItemSigilVoid extends ItemSigilFluidBase {
    public ItemSigilVoid() {
        super("void", 50, null);
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

            if (rayTrace == null || rayTrace.typeOfHit != RayTraceResult.Type.BLOCK) {
                return ActionResult.newResult(EnumActionResult.PASS, stack);
            }

            BlockPos blockPos = rayTrace.getBlockPos();

            if (world.isBlockModifiable(player, blockPos) && player.canPlayerEdit(blockPos, rayTrace.sideHit, stack)) {
                //Void is simpler than the other fluid sigils, because getFluidHandler grabs fluid blocks just fine
                //So extract from fluid tanks with a null side; or drain fluid blocks.
                IFluidHandler destination = getFluidHandler(world, blockPos, null);
                if (destination != null && tryRemoveFluid(destination, 1000, false) && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess()) {
                    if (tryRemoveFluid(destination, 1000, true))
                        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
                }
                //Do the same as above, but use sidedness to interact with the fluid handler.
                IFluidHandler destinationSide = getFluidHandler(world, blockPos, rayTrace.sideHit);
                if (destinationSide != null && tryRemoveFluid(destinationSide, 1000, false) && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess()) {
                    if (tryRemoveFluid(destinationSide, 1000, true))
                        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
                }
            }
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public FluidStack getFluid(ItemStack sigil) {
        return null;
    }

    @Override
    public int getCapacity(ItemStack sigil) {
        return 10000;
    }

    @Override
    public int fill(ItemStack sigil, FluidStack resource, boolean doFill) {
        if (resource == null || resource.amount <= 0)
            return 0;

        Binding binding = getBinding(sigil);

        if (binding == null)
            return 0;

        int capacity = getCapacity(sigil);

        if (!doFill)
            return Math.min(capacity, resource.amount);

        SoulNetwork network = NetworkHelper.getSoulNetwork(binding);

        if (network.getCurrentEssence() < getLpUsed()) {
            network.causeNausea();
            return 0;
        }

        network.syphon(SoulTicket.item(sigil, getLpUsed()));
        return Math.min(capacity, resource.amount);
    }
}

