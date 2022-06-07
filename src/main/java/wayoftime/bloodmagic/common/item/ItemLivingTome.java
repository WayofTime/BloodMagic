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

public class ItemLivingTome extends Item implements ILivingContainer, ILivingUpgradePointsProvider
{

	public ItemLivingTome()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		boolean oneLevel = !player.isSneaking();

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
			if (oneLevel)
			{
				double curExp = armorStats.getUpgrades().getOrDefault(k, 0d);
				double expToNextLevel = k.getNextRequirement((int) curExp) - curExp;

				v = Math.min(expToNextLevel, v);
			}

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
					ResourceLocation key = ((Entry<LivingUpgrade, Double>) upgradeArray[j]).getKey().getKey();
//					if (((Entry<LivingUpgrade, Double>) upgradeArray[j]).getValue() < expUsed)
//					{
//						tomeStats.resetExperience(registryName);
//					} else
					{
						tomeStats.addExperience(key, -expUsed);
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
		ILivingContainer.appendLivingTooltip(stack, getLivingStats(stack), tooltip, false);
	}

	@Override
	public int getAvailableUpgradePoints(ItemStack stack, int drain)
	{
		return getTotalUpgradePoints(stack);
	}

	@Override
	public ItemStack getResultingStack(ItemStack stack, int drain)
	{
		// TODO Auto-generated method stub
		return ItemStack.EMPTY;
	}

	@Override
	public int getExcessUpgradePoints(ItemStack stack, int drain)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalUpgradePoints(ItemStack stack)
	{
		LivingStats tomeStats = getLivingStats(stack);
		if (tomeStats == null)
		{
			return 0;
		}

		int containedPoints = 0;

		Map<LivingUpgrade, Double> upgradeMap = tomeStats.getUpgrades();
		for (Entry<LivingUpgrade, Double> entry : upgradeMap.entrySet())
		{
			if (entry.getKey().isNegative())
			{
				containedPoints += entry.getValue().intValue();
			} else
			{
				containedPoints += entry.getKey().getLevelCost(entry.getKey().getLevel(entry.getValue().intValue()));
			}
		}

		return containedPoints;
	}

	@Override
	public boolean canSyphonPoints(ItemStack stack, int drain)
	{
		return true;
	}

	@Override
	public int getPriority(ItemStack stack)
	{
		return 1;
	}
}
