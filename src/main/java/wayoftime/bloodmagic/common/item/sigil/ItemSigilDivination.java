package wayoftime.bloodmagic.common.item.sigil;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import wayoftime.bloodmagic.altar.IBloodAltar;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.iface.IAltarReader;
import wayoftime.bloodmagic.iface.ISigil;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.helper.NetworkHelper;
import wayoftime.bloodmagic.util.helper.NumeralHelper;
import wayoftime.bloodmagic.util.helper.PlayerHelper;

public class ItemSigilDivination extends ItemSigilBase implements IAltarReader
{

	public ItemSigilDivination(boolean simple)
	{
		super(simple ? "divination" : "seer");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() instanceof ISigil.Holding)
			stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
		if (PlayerHelper.isFakePlayer(player))
			return ActionResult.resultFail(stack);

		if (!world.isRemote)
		{
			RayTraceResult position = Item.rayTrace(world, player, FluidMode.NONE);

			if (position == null || position.getType() == RayTraceResult.Type.MISS)
			{
				super.onItemRightClick(world, player, hand);

				Binding binding = getBinding(stack);
				if (binding != null)
				{
					int currentEssence = NetworkHelper.getSoulNetwork(binding).getCurrentEssence();
					List<ITextComponent> toSend = Lists.newArrayList();
					if (!binding.getOwnerId().equals(player.getGameProfile().getId()))
						toSend.add(new TranslationTextComponent(tooltipBase + "otherNetwork", binding.getOwnerName()));
					toSend.add(new TranslationTextComponent(tooltipBase + "currentEssence", currentEssence));
					ChatUtil.sendNoSpam(player, toSend.toArray(new ITextComponent[toSend.size()]));
				}
			} else
			{
				if (position.getType() == RayTraceResult.Type.BLOCK)
				{
					TileEntity tile = world.getTileEntity(new BlockPos(position.getHitVec()));

					if (tile != null && tile instanceof IBloodAltar)
					{
						IBloodAltar altar = (IBloodAltar) tile;
						int tier = altar.getTier().ordinal() + 1;
						int currentEssence = altar.getCurrentBlood();
						int capacity = altar.getCapacity();
						altar.checkTier();
						ChatUtil.sendNoSpam(player, new TranslationTextComponent(tooltipBase
								+ "currentAltarTier", NumeralHelper.toRoman(tier)), new TranslationTextComponent(tooltipBase
										+ "currentEssence", currentEssence), new TranslationTextComponent(tooltipBase
												+ "currentAltarCapacity", capacity));
					}
//                    else if (tile != null && tile instanceof TileIncenseAltar)
//                    {
//                        TileIncenseAltar altar = (TileIncenseAltar) tile;
//                        altar.recheckConstruction();
//                        double tranquility = altar.tranquility;
//                        ChatUtil.sendNoSpam(player, new TextComponentTranslation(tooltipBase + "currentTranquility", (int) ((100D * (int) (100 * tranquility)) / 100d)), new TextComponentTranslation(tooltipBase + "currentBonus", (int) (100 * altar.incenseAddition)));
//                    } else if (tile != null && tile instanceof TileInversionPillar)
//                    {
//                        TileInversionPillar pillar = (TileInversionPillar) tile;
//                        double inversion = pillar.getCurrentInversion();
//                        ChatUtil.sendNoSpam(player, new TextComponentTranslation(tooltipBase + "currentInversion", ((int) (10 * inversion)) / 10d));
//                    } 
					else
					{
						Binding binding = getBinding(stack);
						if (binding != null)
						{
							int currentEssence = NetworkHelper.getSoulNetwork(binding).getCurrentEssence();
							List<ITextComponent> toSend = Lists.newArrayList();
							if (!binding.getOwnerId().equals(player.getGameProfile().getId()))
								toSend.add(new TranslationTextComponent(tooltipBase
										+ "otherNetwork", binding.getOwnerName()));
							toSend.add(new TranslationTextComponent(tooltipBase + "currentEssence", currentEssence));
							ChatUtil.sendNoSpam(player, toSend.toArray(new ITextComponent[toSend.size()]));
						}
					}
				}
			}

		}

		return super.onItemRightClick(world, player, hand);
	}
}