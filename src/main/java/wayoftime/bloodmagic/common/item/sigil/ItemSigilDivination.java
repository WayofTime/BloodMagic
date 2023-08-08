package wayoftime.bloodmagic.common.item.sigil;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.altar.IBloodAltar;
import wayoftime.bloodmagic.api.compat.IAltarReader;
import wayoftime.bloodmagic.client.hud.GuiEditHUD;
import wayoftime.bloodmagic.common.tile.TileIncenseAltar;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.NumeralHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

import java.util.List;

public class ItemSigilDivination extends ItemSigilBase implements IAltarReader
{
	private final boolean isSimple;

	public ItemSigilDivination(boolean simple)
	{
		super(simple ? "divination" : "seer");
		isSimple = simple;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return InteractionResultHolder.fail(stack);

		if (!world.isClientSide)
		{
			if (player.isShiftKeyDown())
			{
				return InteractionResultHolder.consume(stack);
			}

			HitResult position = Item.getPlayerPOVHitResult(world, player, Fluid.NONE);

			if (position == null || position.getType() == HitResult.Type.MISS)
			{
				super.use(world, player, hand);

				Binding binding = getBinding(stack);
				if (binding != null)
				{
					int currentEssence = NetworkHelper.getSoulNetwork(binding).getCurrentEssence();
					List<Component> toSend = Lists.newArrayList();
					if (!binding.getOwnerId().equals(player.getGameProfile().getId()))
						toSend.add(Component.translatable(tooltipBase + "otherNetwork", binding.getOwnerName()));
					toSend.add(Component.translatable(tooltipBase + "currentEssence", currentEssence));
					ChatUtil.sendNoSpam(player, toSend.toArray(new Component[toSend.size()]));
				}
			} else
			{
				if (position.getType() == HitResult.Type.BLOCK)
				{
					BlockEntity tile = world.getBlockEntity(((BlockHitResult) position).getBlockPos());

					if (tile != null && tile instanceof IBloodAltar)
					{
						IBloodAltar altar = (IBloodAltar) tile;
						int tier = altar.getTier();
						int currentEssence = altar.getCurrentBlood();
						int capacity = altar.getCapacity();
						altar.checkTier();
						if (isSimple)
						{
							ChatUtil.sendNoSpam(player, Component.translatable(tooltipBase + "currentAltarTier", NumeralHelper.toRoman(tier)), Component.translatable(tooltipBase + "currentEssence", currentEssence), Component.translatable(tooltipBase + "currentAltarCapacity", capacity));
						} else
						{
							ChatUtil.sendNoSpam(player, Component.translatable(tooltipBase + "currentAltarTier", NumeralHelper.toRoman(tier)), Component.translatable(tooltipBase + "currentEssence", currentEssence), Component.translatable(tooltipBase + "currentAltarCapacity", capacity));
						}
					} else if (tile != null && tile instanceof TileIncenseAltar)
					{
						TileIncenseAltar altar = (TileIncenseAltar) tile;
						altar.recheckConstruction();
						double tranquility = altar.tranquility;
						ChatUtil.sendNoSpam(player, Component.translatable(tooltipBase + "currentTranquility", (int) ((100D * (int) (100 * tranquility)) / 100d)), Component.translatable(tooltipBase + "currentBonus", (int) (100 * altar.incenseAddition)));
					}
//                    else if (tile != null && tile instanceof TileInversionPillar)
//                    {
//                        TileInversionPillar pillar = (TileInversionPillar) tile;
//                        double inversion = pillar.getCurrentInversion();
//                        ChatUtil.sendNoSpam(player, Component.literalTranslation(tooltipBase + "currentInversion", ((int) (10 * inversion)) / 10d));
//                    } 
					else
					{
						Binding binding = getBinding(stack);
						if (binding != null)
						{
							int currentEssence = NetworkHelper.getSoulNetwork(binding).getCurrentEssence();
							List<Component> toSend = Lists.newArrayList();
							if (!binding.getOwnerId().equals(player.getGameProfile().getId()))
								toSend.add(Component.translatable(tooltipBase + "otherNetwork", binding.getOwnerName()));
							toSend.add(Component.translatable(tooltipBase + "currentEssence", currentEssence));
							ChatUtil.sendNoSpam(player, toSend.toArray(new Component[toSend.size()]));
						}
					}
				}
			}

		} else
		{
			if (player.isShiftKeyDown())
			{
				openEditHUDScreen();
				return InteractionResultHolder.consume(stack);
			}
		}

		return super.use(world, player, hand);
	}

	@OnlyIn(Dist.CLIENT)
	public void openEditHUDScreen()
	{
		Minecraft.getInstance().setScreen(new GuiEditHUD(null));
	}
}