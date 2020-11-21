package wayoftime.bloodmagic.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.util.helper.NBTHelper;

public class ItemExperienceBook extends Item
{
	public ItemExperienceBook()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.experienceTome"));

		if (!stack.hasTag())
			return;

		double storedExp = getStoredExperience(stack);

		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.experienceTome.exp", (int) storedExp));
		tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.experienceTome.expLevel", (int) getLevelForExperience(storedExp)));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote)
		{
			if (player.isSneaking())
				absorbOneLevelExpFromPlayer(stack, player);
			else
				giveOneLevelExpToPlayer(stack, player);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	public void giveOneLevelExpToPlayer(ItemStack stack, PlayerEntity player)
	{
		float progress = player.experience;
		int expToNext = getExperienceForNextLevel(player.experienceLevel);

		int neededExp = (int) Math.ceil((1 - progress) * expToNext);
		float containedExp = (float) getStoredExperience(stack);

		BMLog.DEBUG.info("Needed: " + neededExp + ", contained: " + containedExp + ", exp to next: " + expToNext);

		if (containedExp >= neededExp)
		{
			setStoredExperience(stack, containedExp - neededExp);
			addPlayerXP(player, neededExp);

			if (player.experienceLevel % 5 == 0)
			{
				float f = player.experienceLevel > 30 ? 1.0F : (float) player.experienceLevel / 30.0F;
				player.getEntityWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, player.getSoundCategory(), f * 0.75F, 1.0F);
			}
		} else
		{
			setStoredExperience(stack, 0);
			addPlayerXP(player, (int) containedExp);
		}
	}

	public void absorbOneLevelExpFromPlayer(ItemStack stack, PlayerEntity player)
	{
		float progress = player.experience;

		if (progress > 0)
		{
			int expDeduction = (int) getExperienceAcquiredToNext(player);
			if (expDeduction > 0)
			{
				addPlayerXP(player, -expDeduction);
				addExperience(stack, expDeduction);
			}
		} else if (progress == 0 && player.experienceLevel > 0)
		{
			int expDeduction = getExperienceForNextLevel(player.experienceLevel - 1);
			addPlayerXP(player, -expDeduction);
			addExperience(stack, expDeduction);
		}
	}

	// Credits to Ender IO for some of the experience code, although now modified
	// slightly for my convenience.
	public static int getPlayerXP(PlayerEntity player)
	{
		return (int) (getExperienceForLevel(player.experienceLevel) + (player.experience * player.xpBarCap()));
	}

	public static void addPlayerXP(PlayerEntity player, int amount)
	{
		int experience = Math.max(0, getPlayerXP(player) + amount);
		player.experienceTotal = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
	}

	public static void setStoredExperience(ItemStack stack, double exp)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putDouble("experience", exp);
	}

	public static double getStoredExperience(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		return tag.getDouble("experience");
	}

	public static void addExperience(ItemStack stack, double exp)
	{
		setStoredExperience(stack, getStoredExperience(stack) + exp);
	}

	public static int getExperienceForNextLevel(int currentLevel)
	{
		if (currentLevel < 16)
		{
			return 2 * currentLevel + 7;
		} else if (currentLevel < 31)
		{
			return 5 * currentLevel - 38;
		} else
		{
			return 9 * currentLevel - 158;
		}
	}

	// TODO: Change to calculation form.
	public static int getExperienceForLevel(int level)
	{
		if (level >= 21863)
		{
			return Integer.MAX_VALUE;
		}
		if (level == 0)
		{
			return 0;
		}
		int res = 0;
		for (int i = 0; i < level; i++)
		{
			res += getExperienceForNextLevel(i);
		}
		return res;
	}

	public static double getExperienceAcquiredToNext(PlayerEntity player)
	{
		return player.experience * player.xpBarCap();
	}

	public static int getLevelForExperience(double exp)
	{
		if (exp <= 352)
		{
			return (int) Math.floor(solveParabola(1, 6, -exp));
		} else if (exp <= 1507)
		{
			return (int) Math.floor(solveParabola(2.5, -40.5, 360 - exp));
		} else
		{
			return (int) Math.floor(solveParabola(4.5, -162.5, 2220 - exp));
		}
	}

	public static double solveParabola(double a, double b, double c)
	{
		return (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
	}
}