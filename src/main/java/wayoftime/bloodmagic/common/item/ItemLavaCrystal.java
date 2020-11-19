package wayoftime.bloodmagic.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import wayoftime.bloodmagic.api.data.Binding;
import wayoftime.bloodmagic.api.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

//TODO: Make some hook somewhere that attaches the pos to the ticket otherwise the tickets are basically useless lmao
public class ItemLavaCrystal extends ItemBindableBase
{
	public ItemLavaCrystal()
	{
		super();
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		Binding binding = getBinding(stack);
		if (binding != null)
			NetworkHelper.getSoulNetwork(binding.getOwnerId()).syphon(SoulTicket.item(stack, 50));

		ItemStack returnStack = new ItemStack(this);
		returnStack.setTag(stack.getTag());
		return returnStack;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getBurnTime(ItemStack stack)
	{
		Binding binding = getBinding(stack);
		if (binding == null)
			return -1;

//		if (NetworkHelper.syphonFromContainer(stack, SoulTicket.item(stack, 25)))
		if (NetworkHelper.canSyphonFromContainer(stack, 50))
			return 200;
		else
		{
			PlayerEntity player = PlayerHelper.getPlayerFromUUID(binding.getOwnerId());
			if (player != null)
				player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 99));
		}

		return -1;
	}

//	@Nullable
//	@Override
//	public Binding getBinding(ItemStack stack)
//	{
//		if (stack.getTag() == null) // hasTagCompound doesn't work on empty stacks with tags
//			return null;
//
//		NBTBase bindingTag = stack.getTag().get("binding");
//		if (bindingTag == null || bindingTag.getId() != 10 || bindingTag.isEmpty()) // Make sure it's both a tag
//																					// compound and that it has actual
//																					// data.
//			return null;
//
//		NBTTagCompound nbt = (NBTTagCompound) bindingTag;
//		return new Binding(NBTUtil.getUUIDFromTag(nbt.getCompoundTag("id")), nbt.getString("name"));
//	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		BlockPos pos = context.getPos();
		Direction facing = context.getFace();
		pos = pos.offset(facing);
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		ItemStack itemstack = player.getHeldItem(hand);

		Binding binding = getBinding(player.getHeldItem(hand));

		if (binding == null)
			return ActionResultType.FAIL;

		if (!player.canPlayerEdit(pos, facing, itemstack))
			return ActionResultType.FAIL;

		if (context.getWorld().isAirBlock(pos)
				&& NetworkHelper.getSoulNetwork(binding).syphonAndDamage(player, SoulTicket.item(player.getHeldItem(hand), 100)).isSuccess())
		{
			context.getWorld().playSound(player, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat()
					* 0.4F + 0.8F);
			context.getWorld().setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
		} else
			return ActionResultType.FAIL;

		if (player instanceof ServerPlayerEntity)
			CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, itemstack);

		return ActionResultType.SUCCESS;
	}
}
