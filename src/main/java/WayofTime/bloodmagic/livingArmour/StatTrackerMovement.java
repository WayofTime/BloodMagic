package WayofTime.bloodmagic.livingArmour;

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

public class StatTrackerMovement extends StatTracker
{
    public static Map<EntityPlayer, Double> lastPosX = new HashMap<EntityPlayer, Double>();
    public static Map<EntityPlayer, Double> lastPosZ = new HashMap<EntityPlayer, Double>();

    public double totalMovement = 0;

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".tracker.movement";
    }

    @Override
    public void resetTracker()
    {
        this.totalMovement = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        totalMovement = tag.getDouble(Constants.Mod.MODID + ".tracker.movement");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble(Constants.Mod.MODID + ".tracker.movement", totalMovement);

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

        if (player.isAirBorne)
        {
            return false;
        }

        double distanceTravelled = Math.sqrt(Math.pow(lastPosX.get(player) - player.posX, 2) + Math.pow(lastPosZ.get(player) - player.posZ, 2));

        if (distanceTravelled > 0.0001)
        {
            totalMovement += distanceTravelled;

            lastPosX.put(player, player.posX);
            lastPosZ.put(player, player.posZ);

            markDirty();
        }

//        System.out.println("Total movement since activated: " + totalMovement);
        return false;
    }

    @Override
    public List<LivingArmourUpgrade> getUpgrades()
    {
        // TODO Auto-generated method stub
        return new ArrayList<LivingArmourUpgrade>();
    }
}
