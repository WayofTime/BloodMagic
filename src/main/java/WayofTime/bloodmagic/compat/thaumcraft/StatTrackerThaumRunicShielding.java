package WayofTime.bloodmagic.compat.thaumcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;

public class StatTrackerThaumRunicShielding extends StatTracker
{
    public float totalShieldDamage = 0;

    public static HashMap<LivingArmour, Float> changeMap = new HashMap<LivingArmour, Float>();
    public static int[] healthedRequired = new int[] { 80, 200, 340, 540, 800, 1600, 2800, 5000, 7600, 10000 };

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.thaumRunicShielding";
    }

    @Override
    public void resetTracker()
    {
        this.totalShieldDamage = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalShieldDamage = tag.getFloat(Constants.Mod.MODID + ".tracker.thaumRunicShielding");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setFloat(Constants.Mod.MODID + ".tracker.thaumRunicShielding", totalShieldDamage);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour))
        {
            float lastCharge = Math.abs(changeMap.get(livingArmour));
            float currentCharge = player.getAbsorptionAmount();
            if (currentCharge < lastCharge)
            {
                totalShieldDamage += lastCharge - currentCharge;

                this.markDirty();

                changeMap.put(livingArmour, currentCharge);
                return true;
            }

            if (currentCharge != lastCharge && !player.isPotionActive(Potion.absorption)) //Charge is only updated if the "shielding" isn't caused by Absorption.
            {
                changeMap.put(livingArmour, currentCharge);
            }
        } else
        {
            changeMap.put(livingArmour, 0f);
        }

        return false;
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 10; i++)
        {
            if (totalShieldDamage >= healthedRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeThaumRunicShielding(i));
            }
        }

        return upgradeList;
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.thaumRunicShielding");
    }
}
