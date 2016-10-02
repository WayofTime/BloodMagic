package WayofTime.bloodmagic.livingArmour.tracker.downgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeSlowness;
import WayofTime.bloodmagic.util.Utils;

public class StatTrackerSlowness extends StatTracker
{
    public static Map<EntityPlayer, Double> lastPosX = new HashMap<EntityPlayer, Double>();
    public static Map<EntityPlayer, Double> lastPosZ = new HashMap<EntityPlayer, Double>();

    public static int[] blocksRequired = new int[] { 200, 1000, 2000, 4000, 7000, 15000, 25000, 35000, 50000, 70000 };

    public double totalMovement = 0;

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.slowness";
    }

    @Override
    public void resetTracker()
    {
        this.totalMovement = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalMovement = tag.getDouble(Constants.Mod.MODID + ".tracker.slowness");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble(Constants.Mod.MODID + ".tracker.slowness", totalMovement);

    }

    @Override
    public boolean onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (!lastPosX.containsKey(player))
        {
            lastPosX.put(player, player.posX);
            lastPosZ.put(player, player.posZ);
            return false;
        }

        if (!player.onGround || !player.isPotionActive(MobEffects.SLOWNESS))
        {
            return false;
        }

        double distanceTravelled = Math.sqrt(Math.pow(lastPosX.get(player) - player.posX, 2) + Math.pow(lastPosZ.get(player) - player.posZ, 2));

        if (distanceTravelled > 0.0001 && distanceTravelled < 2)
        {
            totalMovement += distanceTravelled;

            lastPosX.put(player, player.posX);
            lastPosZ.put(player, player.posZ);

            markDirty();

            return true;
        }

        lastPosX.put(player, player.posX);
        lastPosZ.put(player, player.posZ);

        return false;
    }

    @Override
    public void onDeactivatedTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {

    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        List<LivingArmourUpgrade> upgradeList = new ArrayList<LivingArmourUpgrade>();

        for (int i = 0; i < 10; i++)
        {
            if (totalMovement >= blocksRequired[i])
            {
                upgradeList.add(new LivingArmourUpgradeSlowness(i));
            }
        }

        return upgradeList;
    }

    @Override
    public double getProgress(LivingArmour livingArmour, int currentLevel)
    {
        return Utils.calculateStandardProgress(totalMovement, blocksRequired, currentLevel);
    }

    @Override
    public boolean providesUpgrade(String key)
    {
        return key.equals(Constants.Mod.MODID + ".upgrade.slowness");
    }

    @Override
    public void onArmourUpgradeAdded(LivingArmourUpgrade upgrade)
    {
        if (upgrade instanceof LivingArmourUpgradeSlowness)
        {
            int level = upgrade.getUpgradeLevel();
            if (level < blocksRequired.length)
            {
                totalMovement = Math.max(totalMovement, blocksRequired[level]);
                this.markDirty();
            }
        }
    }

    @Override
    public boolean isTrackerDowngrade()
    {
        return true;
    }
}
