package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import wayoftime.bloodmagic.common.item.IAlchemyItem;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilWater extends ItemSigilFluidBase implements IAlchemyItem
{
	public ItemSigilWater()
	{
		super("water", 100, new FluidStack(Fluids.WATER, 10000));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return InteractionResultHolder.fail(stack);

		if (!world.isClientSide && !isUnusable(stack))
		{
			HitResult rayTrace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);

			if (rayTrace == null || rayTrace.getType() != HitResult.Type.BLOCK)
			{
				return InteractionResultHolder.fail(stack);
			}

			BlockHitResult blockRayTrace = (BlockHitResult) rayTrace;
			BlockPos blockPos = blockRayTrace.getBlockPos();
			Direction sideHit = blockRayTrace.getDirection();
			BlockPos blockpos1 = blockPos.relative(sideHit);

			if (world.mayInteract(player, blockPos) && player.mayUseItemAt(blockpos1, sideHit, stack))
			{

				// Case for if block at blockPos is a fluid handler like a tank
				// Try to put fluid into tank
				IFluidHandler destination = getFluidHandler(world, blockPos, null);
				if (destination != null && tryInsertSigilFluid(destination, false) && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
				{
					boolean result = tryInsertSigilFluid(destination, true);
					if (result)
						return InteractionResultHolder.success(stack);
				}
				// Do the same as above, but use sidedness to interact with the fluid handler.
				IFluidHandler destinationSide = getFluidHandler(world, blockPos, sideHit);
				if (destinationSide != null && tryInsertSigilFluid(destinationSide, false) && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
				{
					boolean result = tryInsertSigilFluid(destinationSide, true);
					if (result)
						return InteractionResultHolder.success(stack);
				}

//				// Special vanilla cauldron handling, yay.
//				if (world.getBlockState(blockPos).getBlock() == Blocks.CAULDRON && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
//				{
//					world.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState().setValue(CauldronBlock., 3));
//					return InteractionResultHolder.success(stack);
//				}

				// Case for if block at blockPos is not a tank
				// Place fluid in world
				if (destination == null && destinationSide == null)
				{
					BlockPos targetPos = blockPos.relative(sideHit);
					if (tryPlaceSigilFluid(player, world, targetPos) && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
					{
						return InteractionResultHolder.success(stack);
					}
				}
			}
		}

		return super.use(world, player, hand);
	}

	@Override
	public ItemStack onConsumeInput(ItemStack stack)
	{
		return stack;
	}

	@Override
	public boolean isStackChangedOnUse(ItemStack stack)
	{
		return false;
	}
}