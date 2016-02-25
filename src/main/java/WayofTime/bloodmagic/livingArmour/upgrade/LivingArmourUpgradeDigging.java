package WayofTime.bloodmagic.livingArmour.upgrade;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.LivingArmour;

public class LivingArmourUpgradeDigging extends LivingArmourUpgrade
{
    public static HashMap<ILivingArmour, Boolean> changeMap = new HashMap<ILivingArmour, Boolean>();

    public static final int[] costs = new int[] { 5, 10, 18, 35, 65, 100, 160 };
    public static final int[] digHasteTime = new int[] { 20, 40, 60, 100, 100, 100 };
    public static final int[] digHasteLevel = new int[] { 0, 0, 1, 1, 2, 2, 2 };
    public static final int[] digSpeedTime = new int[] { 0, 60, 60, 100, 100, 100, 100 };
    public static final int[] digSpeedLevel = new int[] { 0, 0, 0, 1, 1, 1, 1 };

    public static void hasDug(LivingArmour armour)
    {
        changeMap.put(armour, true);
    }

    public LivingArmourUpgradeDigging(int level)
    {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour) && changeMap.get(livingArmour))
        {
            changeMap.put(livingArmour, false);

            player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, digHasteTime[this.level], digHasteLevel[this.level], false, false));
            if (digSpeedTime[this.level] > 0)
            {
                player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, digSpeedTime[this.level], digSpeedLevel[this.level], false, false));
            }
        }
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.digging";
    }

    @Override
    public int getMaxTier()
    {
        return 5; // Set to here until I can add more upgrades to it.
    }

    @Override
    public int getCostOfUpgrade()
    {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        // EMPTY
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        // EMPTY
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "digging";
    }
}
