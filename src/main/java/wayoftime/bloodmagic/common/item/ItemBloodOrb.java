package wayoftime.bloodmagic.common.item;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

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
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		BloodOrb orb = getOrb(stack);

		if (orb == null)
			return ActionResult.resultFail(stack);

		if (world == null)
			return super.onItemRightClick(world, player, hand);

		world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F
				+ (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

		if (PlayerHelper.isFakePlayer(player))
			return super.onItemRightClick(world, player, hand);

		if (!stack.hasTag())
			return super.onItemRightClick(world, player, hand);

		Binding binding = getBinding(stack);
		if (binding == null)
			return super.onItemRightClick(world, player, hand);

		if (world.isRemote)
			return super.onItemRightClick(world, player, hand);

		SoulNetwork ownerNetwork = NetworkHelper.getSoulNetwork(binding);
		if (binding.getOwnerId().equals(player.getGameProfile().getId()))
			ownerNetwork.setOrbTier(orb.getTier());

		ownerNetwork.add(SoulTicket.item(stack, world, player, 200), orb.getCapacity()); // Add LP to owner's network
		ownerNetwork.hurtPlayer(player, 200); // Hurt whoever is using it
		return super.onItemRightClick(world, player, hand);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.orb.desc").mergeStyle(TextFormatting.GRAY));

		BloodOrb orb = getOrb(stack);
		if (flag.isAdvanced() && orb != null)
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.orb.owner", stack.getItem().getRegistryName()).mergeStyle(TextFormatting.GRAY));

		super.addInformation(stack, world, tooltip, flag);
	}

//
	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		return stack.copy();
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}
}