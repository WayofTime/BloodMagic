package WayofTime.bloodmagic.livingArmour.tracker;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradePoisonResist;

public class StatTrackerPoison extends StatTracker
{
    public int totalPoisonTicks = 0;

    public static int[] poisonTicksRequired = new int[] { 60 * 20, 3 * 60 * 20, 10 * 60 * 20, 20 * 60 * 20, 25 * 60 * 20 };

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.poison";
    }

    @Override
    public void resetTracker()
    {
        this.totalPoisonTicks = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalPoisonTicks = tag.getInteger(Constants.Mod.MODID + ".tracker.poison");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger(Constants.Mod.MODID + ".tracker.poison", totalPoisonTicks);
    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (player.isPotionActive(Potion.poison))
        {
            totalPoisonTicks++;
            this.markDirty();
            return true;
        }

        return false;
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 3; i++)
        {
            if (totalPoisonTicks >= poisonTicksRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradePoisonResist(i));
            }
        }

        return upgradeList;
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.poisonResist");
    }
}
