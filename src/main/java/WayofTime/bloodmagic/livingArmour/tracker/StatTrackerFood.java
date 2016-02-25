package WayofTime.bloodmagic.livingArmour.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeKnockbackResist;

public class StatTrackerFood extends StatTracker
{
    public static Map<EntityPlayer, Integer> lastFoodEatenMap = new HashMap<EntityPlayer, Integer>();

    public static int[] foodRequired = new int[] { 100, 200, 300, 5000, 1000 };

    public int foodEaten = 0;

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.foodEaten";
    }

    @Override
    public void resetTracker()
    {
        this.foodEaten = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        foodEaten = tag.getInteger(Constants.Mod.MODID + ".tracker.foodEaten");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger(Constants.Mod.MODID + ".tracker.foodEaten", foodEaten);

    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (!lastFoodEatenMap.containsKey(player))
        {
            lastFoodEatenMap.put(player, 20);
            return false;
        }

        int currentFood = player.getFoodStats().getFoodLevel();
        int prevFood = lastFoodEatenMap.get(player);
        lastFoodEatenMap.put(player, currentFood);

        if (currentFood > prevFood)
        {
            foodEaten += (currentFood - prevFood);

            markDirty();

            return true;
        }

        return false;
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < foodRequired.length; i++)
        {
            if (foodEaten >= foodRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeKnockbackResist(i));
            }
        }

        return upgradeList;
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.knockback");
    }
}
