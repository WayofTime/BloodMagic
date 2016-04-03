package WayofTime.bloodmagic.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class ItemExperienceBook extends Item implements IVariantProvider
{
    public ItemExperienceBook()
    {
        setUnlocalizedName(Constants.Mod.MODID + ".experienceTome");
        setRegistryName(Constants.BloodMagicItem.EXPERIENCE_TOME.getRegName());
        setMaxStackSize(1);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.experienceTome"));

        double storedExp = getStoredExperience(stack);

        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.experienceTome.exp", (int) storedExp));

        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.experienceTome.expLevel", (int) getLevelForExperience(storedExp)));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote)
        {
            if (player.isSneaking())
                absorbOneLevelExpFromPlayer(stack, player);
            else
                giveOneLevelExpToPlayer(stack, player);
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=experiencetome"));
        return ret;
    }

    public void giveOneLevelExpToPlayer(ItemStack stack, EntityPlayer player)
    {
        float progress = player.experience;
        int expToNext = getExperienceForNextLevel(player.experienceLevel);

        float neededExp = (1 - progress) * expToNext;
        float containedExp = (float) getStoredExperience(stack);

        if (containedExp >= neededExp)
        {
            setStoredExperience(stack, containedExp - neededExp);
            player.experience = 0;
            player.experienceTotal = Math.round(player.experienceTotal + neededExp);
            player.experienceLevel++;

            if (player.experienceLevel % 5 == 0)
            {
                float f = player.experienceLevel > 30 ? 1.0F : (float) player.experienceLevel / 30.0F;
                player.worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.entity_player_levelup, player.getSoundCategory(), f * 0.75F, 1.0F);
            }
        } else
        {
            setStoredExperience(stack, 0);
            progress += containedExp / expToNext;
            player.experience = progress;
            player.experienceTotal = Math.round(player.experienceTotal + containedExp);
        }
    }

    public void absorbOneLevelExpFromPlayer(ItemStack stack, EntityPlayer player)
    {
        float progress = player.experience;
        if (progress > 0)
        {
            double expDeduction = getExperienceAcquiredToNext(player.experienceLevel, player.experience);
            player.experience = 0;
            player.experienceTotal -= (int) (expDeduction);

            addExperience(stack, expDeduction);
        } else if (player.experienceLevel > 0)
        {
            player.experienceLevel--;
            int expDeduction = getExperienceForNextLevel(player.experienceLevel);
            player.experienceTotal -= expDeduction;

            addExperience(stack, expDeduction);
        }
    }

    public static void setStoredExperience(ItemStack stack, double exp)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble("experience", exp);
    }

    public static double getStoredExperience(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        return tag.getDouble("experience");
    }

    public static void addExperience(ItemStack stack, double exp)
    {
        setStoredExperience(stack, getStoredExperience(stack) + exp);
    }

    public static int getExperienceForNextLevel(int currentLevel)
    {
        if (currentLevel <= 16)
        {
            return 2 * currentLevel + 7;
        } else if (currentLevel <= 31)
        {
            return 5 * currentLevel - 38;
        } else
        {
            return 9 * currentLevel - 158;
        }
    }

    public static double getExperienceAcquiredToNext(int currentLevel, double progress)
    {
        return progress * getExperienceForNextLevel(currentLevel);
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
