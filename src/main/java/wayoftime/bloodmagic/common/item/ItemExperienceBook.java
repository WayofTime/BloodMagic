package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.util.BMLog;
import wayoftime.bloodmagic.util.helper.NBTHelper;

import java.util.List;

public class ItemExperienceBook extends Item
{
	public ItemExperienceBook()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack stack)
	{
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		tooltip.add(Component.translatable("tooltip.bloodmagic.experienceTome").withStyle(ChatFormatting.GRAY));

		if (!stack.hasTag())
			return;

		double storedExp = getStoredExperience(stack);

		tooltip.add(Component.translatable("tooltip.bloodmagic.experienceTome.exp", (int) storedExp).withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("tooltip.bloodmagic.experienceTome.expLevel", getLevelForExperience(storedExp)).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide)
		{
			if (player.isShiftKeyDown())
				absorbOneLevelExpFromPlayer(stack, player);
			else
				giveOneLevelExpToPlayer(stack, player);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	public void giveOneLevelExpToPlayer(ItemStack stack, Player player)
	{
		float progress = player.experienceProgress;
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
				player.getCommandSenderWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, player.getSoundSource(), f * 0.75F, 1.0F);
			}
		} else
		{
			setStoredExperience(stack, 0);
			addPlayerXP(player, (int) containedExp);
		}
	}

	public void absorbOneLevelExpFromPlayer(ItemStack stack, Player player)
	{
		float progress = player.experienceProgress;

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
	public static int getPlayerXP(Player player)
	{
		return (int) (getExperienceForLevel(player.experienceLevel) + (player.experienceProgress * player.getXpNeededForNextLevel()));
	}

	public static void addPlayerXP(Player player, int amount)
	{
		int experience = Math.max(0, getPlayerXP(player) + amount);
		player.totalExperience = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experienceProgress = (float) (experience - expForLevel) / (float) player.getXpNeededForNextLevel();
	}

	public static void setStoredExperience(ItemStack stack, double exp)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putDouble("experience", exp);
	}

	public static double getStoredExperience(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

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

	public static double getExperienceAcquiredToNext(Player player)
	{
		return player.experienceProgress * player.getXpNeededForNextLevel();
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