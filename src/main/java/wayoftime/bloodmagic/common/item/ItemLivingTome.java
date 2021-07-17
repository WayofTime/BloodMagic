package wayoftime.bloodmagic.common.item;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.core.LivingArmorRegistrar;
import wayoftime.bloodmagic.core.living.ILivingContainer;
import wayoftime.bloodmagic.core.living.LivingStats;
import wayoftime.bloodmagic.core.living.LivingUpgrade;
import wayoftime.bloodmagic.core.living.LivingUtil;

public class ItemLivingTome extends Item implements ILivingContainer
{

	public ItemLivingTome()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack held = player.getHeldItem(hand);

		LivingStats armorStats = LivingStats.fromPlayer(player, true);
		if (armorStats == null)
			return ActionResult.resultPass(held);

		LivingStats tomeStats = getLivingStats(held);
		if (tomeStats == null)
			return ActionResult.resultPass(held);

		Map<LivingUpgrade, Double> upgradeMap = tomeStats.getUpgrades();

		boolean[] flag = new boolean[] { false };
		double[] expUsedArray = new double[upgradeMap.size()];
		int[] i = new int[] { 0 };
		upgradeMap.forEach((k, v) -> {
//			if (armorStats.getLevel(k.getKey()) >= tomeStats.getLevel(k.getKey()))
//				return;

			Pair<LivingStats, Double> upgraded = LivingUtil.applyExperienceToUpgradeCap(player, k, v);
			// applyExperienceToUpgradeCap
			flag[0] = flag[0] || upgraded.getRight() > 0;
			expUsedArray[i[0]] = upgraded.getRight();
			i[0] = i[0] + 1;
		});
//        LivingStats.toPlayer(player, armorStats);
		if (flag[0])
		{
			Object[] upgradeArray = upgradeMap.entrySet().toArray();
//			tomeStats.addExperience(key, experience)
			for (int j = 0; j < expUsedArray.length; j++)
			{
				double expUsed = expUsedArray[j];
				if (expUsed > 0)
				{
					ResourceLocation registryName = ((Entry<LivingUpgrade, Double>) upgradeArray[j]).getKey().getRegistryName();
//					if (((Entry<LivingUpgrade, Double>) upgradeArray[j]).getValue() < expUsed)
//					{
//						tomeStats.resetExperience(registryName);
//					} else
					{
						tomeStats.addExperience(registryName, -expUsed);
					}

					updateLivingStats(held, tomeStats);
				}
			}

			if (!player.isCreative())
			{
				boolean doShrink = true;
				for (Entry<LivingUpgrade, Double> entry : tomeStats.getUpgrades().entrySet())
				{
					LivingUpgrade upgrade = entry.getKey();
					int level = upgrade.getLevel(entry.getValue().intValue());
					if (level > 0)
					{
						doShrink = false;
					}
				}

				// Shrink the stack if the tome is empty
				if (doShrink)
				{
					held.shrink(1);
				}
			}

//			held.shrink(1);
			return ActionResult.resultSuccess(held);
		} else
			return ActionResult.resultPass(held);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if (!isInGroup(group))
			return;

		for (Entry<ResourceLocation, LivingUpgrade> entry : LivingArmorRegistrar.UPGRADE_MAP.entrySet())
		{
			LivingUpgrade upgrade = entry.getValue();
			int exp = 0;

			while ((exp = upgrade.getNextRequirement(exp)) != 0)
			{
				ItemStack tome = new ItemStack(this);
				updateLivingStats(tome, new LivingStats().setMaxPoints(upgrade.getLevelCost(exp)).addExperience(upgrade.getKey(), exp));
				items.add(tome);
			}
		}

//		LivingArmorRegistrar.UPGRADE_MAP.forEach(upgrade -> {
//			int exp = 0;
//
//			while ((exp = upgrade.getNextRequirement(exp)) != 0)
//			{
//				ItemStack tome = new ItemStack(this);
//				updateLivingStats(tome, new LivingStats().setMaxPoints(upgrade.getLevelCost(exp)).addExperience(upgrade.getKey(), exp));
//				display.add(tome);
//			}
//		});
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		ILivingContainer.appendLivingTooltip(getLivingStats(stack), tooltip, false);
	}
}
