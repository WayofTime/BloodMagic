package wayoftime.bloodmagic.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulTicket;
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
	public ItemStack getCraftingRemainingItem(ItemStack stack)
	{
		Binding binding = getBinding(stack);
		if (binding != null)
			NetworkHelper.getSoulNetwork(binding.getOwnerId()).syphon(SoulTicket.item(stack, 50));

		ItemStack returnStack = new ItemStack(this);
		returnStack.setTag(stack.getTag());
		return returnStack;
	}

	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack)
	{
		return true;
	}

	public int getBurnTime(ItemStack stack)
	{
		Binding binding = getBinding(stack);
		if (binding == null)
			return -1;

		if (EffectiveSide.get().isServer())
		{
			if (NetworkHelper.canSyphonFromContainer(stack, 50))
				return 200;
			else
			{
				Player player = PlayerHelper.getPlayerFromUUID(binding.getOwnerId());
				if (player != null)
				{
					player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 99));
				}
			}
		} else
		{
			return 200;
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
	public InteractionResult useOn(UseOnContext context)
	{
		BlockPos pos = context.getClickedPos();
		Direction facing = context.getClickedFace();
		pos = pos.relative(facing);
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		ItemStack itemstack = player.getItemInHand(hand);

		Binding binding = getBinding(player.getItemInHand(hand));

		if (binding == null)
			return InteractionResult.FAIL;

		if (!player.mayUseItemAt(pos, facing, itemstack))
			return InteractionResult.FAIL;

		if (context.getLevel().isEmptyBlock(pos) && context.getLevel().isClientSide)
		{
			context.getLevel().playSound(player, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, context.getLevel().getRandom().nextFloat() * 0.4F + 0.8F);
			return InteractionResult.SUCCESS;
		}

		if (context.getLevel().isEmptyBlock(pos) && NetworkHelper.getSoulNetwork(binding).syphonAndDamage(player, SoulTicket.item(player.getItemInHand(hand), 100)).isSuccess())
		{
			context.getLevel().playSound(player, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, context.getLevel().getRandom().nextFloat() * 0.4F + 0.8F);
			context.getLevel().setBlock(pos, Blocks.FIRE.defaultBlockState(), 11);
		} else
			return InteractionResult.FAIL;

		if (player instanceof ServerPlayer)
			CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, itemstack);

		return InteractionResult.SUCCESS;
	}
}
