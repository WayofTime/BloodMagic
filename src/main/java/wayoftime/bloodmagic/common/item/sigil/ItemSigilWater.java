package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import wayoftime.bloodmagic.common.item.IAlchemyItem;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

import wayoftime.bloodmagic.common.item.sigil.ISigil.Holding;

public class ItemSigilWater extends ItemSigilFluidBase implements IAlchemyItem
{
	public ItemSigilWater()
	{
		super("water", 100, new FluidStack(Fluids.WATER, 10000));
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return ActionResult.fail(stack);

		if (!world.isClientSide && !isUnusable(stack))
		{
			RayTraceResult rayTrace = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.NONE);

			if (rayTrace == null || rayTrace.getType() != RayTraceResult.Type.BLOCK)
			{
				return ActionResult.fail(stack);
			}

			BlockRayTraceResult blockRayTrace = (BlockRayTraceResult) rayTrace;
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
						return ActionResult.success(stack);
				}
				// Do the same as above, but use sidedness to interact with the fluid handler.
				IFluidHandler destinationSide = getFluidHandler(world, blockPos, sideHit);
				if (destinationSide != null && tryInsertSigilFluid(destinationSide, false) && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
				{
					boolean result = tryInsertSigilFluid(destinationSide, true);
					if (result)
						return ActionResult.success(stack);
				}

				// Special vanilla cauldron handling, yay.
				if (world.getBlockState(blockPos).getBlock() == Blocks.CAULDRON && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
				{
					world.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState().setValue(CauldronBlock.LEVEL, 3));
					return ActionResult.success(stack);
				}

				// Case for if block at blockPos is not a tank
				// Place fluid in world
				if (destination == null && destinationSide == null)
				{
					BlockPos targetPos = blockPos.relative(sideHit);
					if (tryPlaceSigilFluid(player, world, targetPos) && NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.item(stack, world, player, getLpUsed())).isSuccess())
					{
						return ActionResult.success(stack);
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