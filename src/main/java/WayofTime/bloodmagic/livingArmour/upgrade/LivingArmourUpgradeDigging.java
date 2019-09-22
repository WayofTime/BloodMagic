package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import java.util.HashMap;

public class LivingArmourUpgradeDigging extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{5, 10, 18, 32, 60, 90, 140, 180, 240, 300};
    public static final int[] digSpeedTime = new int[]{0, 50, 60, 100, 100, 100, 100, 150, 150, 150};
    public static final int[] digSpeedLevel = new int[]{0, 0, 0, 1, 1, 1, 1, 1, 2, 2};
    public static final double[] digSpeedModifier = new double[]{1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.8, 2, 2.2, 2.5};
    public static HashMap<ILivingArmour, Boolean> changeMap = new HashMap<>();

    public LivingArmourUpgradeDigging(int level) {
        super(level);
    }

    @Override
    public double getMiningSpeedModifier(PlayerEntity player) {
        return digSpeedModifier[this.level];
    }

    @Override
    public void onTick(World world, PlayerEntity player, ILivingArmour livingArmour) {
        if (changeMap.containsKey(livingArmour) && changeMap.get(livingArmour)) {
            changeMap.put(livingArmour, false);

            if (digSpeedTime[this.level] > 0) {
                player.addPotionEffect(new EffectInstance(Effects.SPEED, digSpeedTime[this.level], digSpeedLevel[this.level], false, false));
            }
        }
    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.digging";
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
        return tooltipBase + "digging";
    }

    public static void hasDug(LivingArmour armour) {
        changeMap.put(armour, true);
    }
}
