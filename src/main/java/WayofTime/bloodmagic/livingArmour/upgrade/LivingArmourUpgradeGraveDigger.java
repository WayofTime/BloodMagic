package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class LivingArmourUpgradeGraveDigger extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{5, 12, 20, 35, 49, 78, 110, 160, 215, 320};
    public static final double[] damageBoost = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    public LivingArmourUpgradeGraveDigger(int level) {
        super(level);
    }

    @Override
    public double getAdditionalDamageOnHit(double damage, PlayerEntity wearer, LivingEntity hitEntity, ItemStack weapon) {
        if (!weapon.isEmpty() && weapon.getItem() instanceof ShovelItem) {
            return getDamageModifier();
        }

        return 0;
    }

    public double getDamageModifier() {
        return damageBoost[this.level];
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.graveDigger";
    }

    @Override
    public int getMaxTier() {
        return 10;
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(CompoundNBT tag) {
        // EMPTY
    }

    @Override
    public void readFromNBT(CompoundNBT tag) {
        // EMPTY
    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "graveDigger";
    }
}