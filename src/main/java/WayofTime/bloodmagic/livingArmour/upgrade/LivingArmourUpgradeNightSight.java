package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class LivingArmourUpgradeNightSight extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{5, 8, 15, 20, 34, 45, 70, 100, 150, 200};
    public static final double[] meleeDamage = new double[]{0, 0.5, 1, 1.5, 2, 2.5, 3, 4, 5, 6};

    public boolean isActive = false;

    public LivingArmourUpgradeNightSight(int level) {
        super(level);
    }

    @Override
    public double getAdditionalDamageOnHit(double damage, EntityPlayer wearer, EntityLivingBase hitEntity, ItemStack weapon) {
        return isActive ? meleeDamage[this.level] : 0;
    }

    @Override
    public void onTick(World world, EntityPlayer player, ILivingArmour livingArmour) {
        if (world.getLight(player.getPosition(), false) <= 9) {
            isActive = true;
            if (player.isPotionActive(MobEffects.NIGHT_VISION)) {
                int dur = player.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
                if (dur > 100 && dur < 20 * 60 * 20) {
                    //Don't override the potion effect if the other potion effect is sufficiently long.
                    return;
                }
            }

            player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, Constants.Misc.NIGHT_VISION_CONSTANT_BEGIN, 0, false, false));
        } else {
            isActive = false;
        }
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.nightSight";
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

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public String getUnlocalizedName() {
        return tooltipBase + "nightSight";
    }
}