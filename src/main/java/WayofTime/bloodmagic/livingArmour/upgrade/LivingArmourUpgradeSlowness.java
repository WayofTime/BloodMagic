package WayofTime.bloodmagic.livingArmour.upgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;

public class LivingArmourUpgradeSlowness extends LivingArmourUpgrade
{
    public static final int[] costs = new int[] { -50 };
    public static final int[] slownessDuration = new int[] { 30 * 20 };

    public LivingArmourUpgradeSlowness(int level)
    {
        super(level);
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour)
    {
        player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1, 0, true, false));
    }

    @Override
    public String getUniqueIdentifier()
    {
        return Constants.Mod.MODID + ".upgrade.slowness";
    }

    @Override
    public int getMaxTier()
    {
        return 1;
    }

    @Override
    public int getCostOfUpgrade()
    {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
    }

    @Override
    public String getUnlocalizedName()
    {
        return tooltipBase + "slowness";
    }
}