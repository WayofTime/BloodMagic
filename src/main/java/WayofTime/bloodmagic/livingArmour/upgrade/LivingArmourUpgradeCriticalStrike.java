package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LivingArmourUpgradeCriticalStrike extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{5, 12, 22, 35, 49};
    public static final double[] damageBoost = new double[]{0.1, 0.2, 0.3, 0.4, 0.5};

    public LivingArmourUpgradeCriticalStrike(int level) {
        super(level);
    }

    @Override
    public double getAdditionalDamageOnHit(double damage, EntityPlayer wearer, EntityLivingBase hitEntity, ItemStack weapon) {
        boolean flag = wearer.fallDistance > 0.0F && !wearer.onGround && !wearer.isOnLadder() && !wearer.isInWater() && !wearer.isPotionActive(MobEffects.BLINDNESS) && !wearer.isRiding() && !wearer.isSprinting();

        if (flag) {
            return getDamageModifier() * damage;
        }

        return 0;
    }

    public double getDamageModifier() {
        return damageBoost[this.level];
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.criticalStrike";
    }

    @Override
    public int getMaxTier() {
        return 5;
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        // EMPTY
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        // EMPTY
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "criticalStrike";
    }
}