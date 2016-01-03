package WayofTime.bloodmagic.livingArmour;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeDigging extends LivingArmourUpgrade
{
    public static HashMap<LivingArmour, Boolean> changeMap = new HashMap<LivingArmour, Boolean>();

    public static void hasDug(LivingArmour armour)
    {
        changeMap.put(armour, true);
    }

    public LivingArmourUpgradeDigging(int level)
    {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, LivingArmour livingArmour)
    {
        if (changeMap.containsKey(livingArmour) && changeMap.get(livingArmour))
        {
            changeMap.put(livingArmour, false);

            player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 100, 0, false, false));
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
        return 1;
    }

    @Override
    public int getCostOfUpgrade()
    {
        return 0;
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
}
