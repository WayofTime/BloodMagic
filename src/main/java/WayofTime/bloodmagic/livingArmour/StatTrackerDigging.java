package WayofTime.bloodmagic.livingArmour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;

public class StatTrackerDigging extends StatTracker
{
    public double totalBlocksDug = 0;

    public static HashMap<LivingArmour, Integer> changeMap = new HashMap<LivingArmour, Integer>();

    public static void incrementCounter(LivingArmour armour)
    {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + 1 : 1);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.digging";
    }

    @Override
    public void resetTracker()
    {
        this.totalBlocksDug = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalBlocksDug = tag.getDouble(Constants.Mod.MODID + ".tracker.digging");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble(Constants.Mod.MODID + ".tracker.digging", totalBlocksDug);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            int change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalBlocksDug += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0);

                System.out.println("Blocks dug: " + totalBlocksDug);
                this.markDirty();

                return true;
            }
        }

        return false;
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        // TODO Auto-generated method stub
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        if (totalBlocksDug >= 10)
        {
            upgradeList.add(new LivingArmourUpgradeDigging(0));
        }
//        for (int i = 0; i < 5; i++)
//        {
//            if (totalMovement > (i + 1) * (i + 1) * (i + 1) * 100)
//            {
//                upgradeList.add(new LivingArmourUpgradeSpeed(i));
//            }
//        }

        return upgradeList;
    }
}
