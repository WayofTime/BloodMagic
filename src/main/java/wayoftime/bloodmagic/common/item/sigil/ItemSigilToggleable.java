package wayoftime.bloodmagic.common.item.sigil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.common.item.ItemSigil;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.common.item.IActivatable;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

import net.minecraft.world.item.Item.Properties;
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
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return InteractionResultHolder.fail(stack);

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
	public InteractionResult useOn(UseOnContext context)
	{
		Level world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();

		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);

		Binding binding = getBinding(stack);
		if (binding == null || player.isShiftKeyDown()) // Make sure Sigils are bound before handling. Also ignores while
													// toggling state
			return InteractionResult.PASS;

		return onSigilUse(stack, player, world, blockpos, context.getClickedFace(), context.getClickLocation())
				? InteractionResult.SUCCESS
				: InteractionResult.FAIL;
	}

	public boolean onSigilUse(ItemStack itemStack, Player player, Level world, BlockPos blockPos, Direction side, Vec3 hitVec)
	{
		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (!worldIn.isClientSide && entityIn instanceof Player && getActivated(stack))
		{
			if (entityIn.tickCount % 100 == 0)
			{
				if (!NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage((Player) entityIn, SoulTicket.item(stack, worldIn, entityIn, getLpUsed())).isSuccess())
				{
					setActivatedState(stack, false);
				}
			}

			onSigilUpdate(stack, worldIn, (Player) entityIn, itemSlot, isSelected);
		}
	}

	public void onSigilUpdate(ItemStack stack, Level world, Player player, int itemSlot, boolean isSelected)
	{
	}
}