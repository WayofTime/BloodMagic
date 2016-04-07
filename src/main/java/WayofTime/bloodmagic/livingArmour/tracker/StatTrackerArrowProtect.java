package WayofTime.bloodmagic.livingArmour.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeArrowProtect;

public class StatTrackerArrowProtect extends StatTracker
{
    public int totalDamage = 0;

    public static HashMap<LivingArmour, Double> changeMap = new HashMap<LivingArmour, Double>();
    public static int[] damageRequired = new int[] { 30, 200, 400, 800, 1500, 2500, 3500, 5000, 7000, 15000 };

    public static void incrementCounter(LivingArmour armour, double damage)
    {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + damage : damage);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.arrowProtect";
    }

    @Override
    public void resetTracker()
    {
        this.totalDamage = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalDamage = tag.getInteger(Constants.Mod.MODID + ".tracker.arrowProtect");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger(Constants.Mod.MODID + ".tracker.arrowProtect", totalDamage);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            double change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalDamage += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0d);

                this.markDirty();

                return true;
            }
        }

        return false;
    }

    @Override
    public void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            changeMap.remove(livingArmour);
        }
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 10; i++)
        {
            if (totalDamage >= damageRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeArrowProtect(i));
            }
        }

        return upgradeList;
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.arrowProtect");
    }
}
