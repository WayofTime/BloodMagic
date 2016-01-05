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

public class StatTrackerSelfSacrifice extends StatTracker
{
    public static HashMap<LivingArmour, Integer> changeMap = new HashMap<LivingArmour, Integer>();
    public static int[] sacrificesRequired = new int[] { 50, 200, 400, 600, 800 }; //testing

    public int totalSacrifices = 0;

    public static void incrementCounter(LivingArmour armour)
    {
        changeMap.put(armour, changeMap.containsKey(armour) ? changeMap.get(armour) + 1 : 1);
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.selfSacrifice";
    }

    @Override
    public void resetTracker()
    {
        this.totalSacrifices = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalSacrifices = tag.getInteger(Constants.Mod.MODID + ".tracker.selfSacrifice");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger(Constants.Mod.MODID + ".tracker.selfSacrifice", totalSacrifices);

    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            int change = Math.abs(changeMap.get(livingArmour));
            if (change > 0)
            {
                totalSacrifices += Math.abs(changeMap.get(livingArmour));

                changeMap.put(livingArmour, 0);

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

        for (int i = 0; i < 5; i++)
        {
            if (totalSacrifices > sacrificesRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeSelfSacrifice(i));
            }
        }

        return upgradeList;
    }
}
