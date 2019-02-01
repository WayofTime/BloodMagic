package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class LivingArmourUpgradeSolarPowered extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{5, 12, 20, 35, 49, 78, 110, 160, 215, 320};
    public static final int[] regenCooldown = new int[]{200, 180, 160, 120, 100, 80, 40, 20, 10, 10};
    public static final int[] fireResistCooldown = new int[]{1, 1, 60 * 60, 50 * 60, 40 * 60, 35 * 60, 30 * 60, 25 * 60, 20 * 60, 10 * 60};
    public static final int[] fireResistTime = new int[]{0, 0, 15 * 60, 20 * 60, 30 * 60, 35 * 60, 40 * 60, 50 * 60, 60 * 60, 100 * 60};
    public static final double[] protectionLevel = new double[]{0.02, 0.04, 0.06, 0.08, 0.10, 0.13, 0.16, 0.19, 0.22, 0.25};

    public int counter = 0;

    public LivingArmourUpgradeSolarPowered(int level) {
        super(level);
    }

    @Override
    public double getArmourProtection(EntityLivingBase wearer, DamageSource source) {
        if (wearer.getEntityWorld().canSeeSky(wearer.getPosition()) && wearer.getEntityWorld().provider.isDaytime()) {
            return protectionLevel[this.level];
        }

        return 0;
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {
        counter++;
        if (world.canSeeSky(player.getPosition()) && world.provider.isDaytime()) {
            if (counter % regenCooldown[this.level] == 0 && player.getHealth() < player.getMaxHealth()) {
                player.heal(1);
            }

            if (fireResistTime[this.level] != 0 && counter % fireResistCooldown[this.level] == 0) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, fireResistTime[this.level], 0, false, false));
            }
        }
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.solarPowered";
    }

    @Override
    public int getMaxTier() {
        return 10; // Set to here until I can add more upgrades to it.
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger(BloodMagic.MODID + ".tracker.solarPowered", counter);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        counter = tag.getInteger(BloodMagic.MODID + ".tracker.solarPowered");
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "solarPowered";
    }
}