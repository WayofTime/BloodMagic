package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.registries.ForgeRegistries;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ItemBloodOrb extends ItemBindableBase implements IBloodOrb, IForgeItem
{
	private final Supplier<BloodOrb> sup;

	public ItemBloodOrb(Supplier<BloodOrb> sup)
	{
		this.sup = sup;
	}

	@Override
	public BloodOrb getOrb(ItemStack stack)
	{
		return sup.get();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		BloodOrb orb = getOrb(stack);

		if (orb == null)
			return InteractionResultHolder.fail(stack);

		if (world == null)
			return super.use(world, player, hand);

		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F
				+ (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

		if (PlayerHelper.isFakePlayer(player))
			return super.use(world, player, hand);

		if (!stack.hasTag())
			return super.use(world, player, hand);

		Binding binding = getBinding(stack);
		if (binding == null)
			return super.use(world, player, hand);

		if (world.isClientSide)
			return super.use(world, player, hand);

		SoulNetwork ownerNetwork = NetworkHelper.getSoulNetwork(binding);
		if (binding.getOwnerId().equals(player.getGameProfile().getId()))
			ownerNetwork.setOrbTier(orb.getTier());

		ownerNetwork.add(SoulTicket.item(stack, world, player, 200), orb.getCapacity()); // Add LP to owner's network
		ownerNetwork.hurtPlayer(player, 200); // Hurt whoever is using it
		return super.use(world, player, hand);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.orb.desc").withStyle(ChatFormatting.GRAY));

		BloodOrb orb = getOrb(stack);
		if (flag.isAdvanced() && orb != null)
			tooltip.add(Component.translatable("tooltip.bloodmagic.orb.owner", ForgeRegistries.ITEMS.getKey(stack.getItem())).withStyle(ChatFormatting.GRAY));

		super.appendHoverText(stack, world, tooltip, flag);
	}

//
	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack)
	{
		return stack.copy();
	}

	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack)
	{
		return true;
	}
}