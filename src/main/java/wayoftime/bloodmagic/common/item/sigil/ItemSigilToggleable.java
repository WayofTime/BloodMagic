package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import wayoftime.bloodmagic.common.item.ItemSigil;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.common.item.IActivatable;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

import net.minecraft.item.Item.Properties;
import wayoftime.bloodmagic.common.item.sigil.ISigil.Holding;

/**
 * Base class for all toggleable sigils.
 */
public class ItemSigilToggleable extends ItemSigil implements IActivatable
{

	public ItemSigilToggleable(Properties property, int lpUsed)
	{
		super(property, lpUsed);
	}

	@Override
	public boolean getActivated(ItemStack stack)
	{
		return !stack.isEmpty() && NBTHelper.checkNBT(stack).getTag().getBoolean(Constants.NBT.ACTIVATED);
	}

	@Override
	public ItemStack setActivatedState(ItemStack stack, boolean activated)
	{
		if (!stack.isEmpty())
		{
			NBTHelper.checkNBT(stack).getTag().putBoolean(Constants.NBT.ACTIVATED, activated);
			return stack;
		}

		return stack;
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
			if (player.isShiftKeyDown())
				setActivatedState(stack, !getActivated(stack));
			if (getActivated(stack))
				return super.use(world, player, hand);
		}

		return super.use(world, player, hand);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		World world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();

		PlayerEntity player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);

		Binding binding = getBinding(stack);
		if (binding == null || player.isShiftKeyDown()) // Make sure Sigils are bound before handling. Also ignores while
													// toggling state
			return ActionResultType.PASS;

		return onSigilUse(stack, player, world, blockpos, context.getClickedFace(), context.getClickLocation())
				? ActionResultType.SUCCESS
				: ActionResultType.FAIL;
	}

	public boolean onSigilUse(ItemStack itemStack, PlayerEntity player, World world, BlockPos blockPos, Direction side, Vector3d hitVec)
	{
		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (!worldIn.isClientSide && entityIn instanceof PlayerEntity && getActivated(stack))
		{
			if (entityIn.tickCount % 100 == 0)
			{
				if (!NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage((PlayerEntity) entityIn, SoulTicket.item(stack, worldIn, entityIn, getLpUsed())).isSuccess())
				{
					setActivatedState(stack, false);
				}
			}

			onSigilUpdate(stack, worldIn, (PlayerEntity) entityIn, itemSlot, isSelected);
		}
	}

	public void onSigilUpdate(ItemStack stack, World world, PlayerEntity player, int itemSlot, boolean isSelected)
	{
	}
}