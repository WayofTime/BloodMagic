package WayofTime.bloodmagic.livingArmour.upgrade;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.ILivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class LivingArmourUpgradeElytra extends LivingArmourUpgrade {
    public static final int[] costs = new int[]{20};

    public LivingArmourUpgradeElytra(int level) {
        super(level);
    }

    @Override
    public void onTick(World world, PlayerEntity player, ILivingArmour livingArmour) {

    }

    @Override
    public String getUniqueIdentifier() {
        return BloodMagic.MODID + ".upgrade.elytra";
    }

    @Override
    public int getMaxTier() {
        return 1; // Set to here until I can add more upgrades to it.
    }

    @Override
    public int getCostOfUpgrade() {
        return costs[this.level];
    }

    @Override
    public void writeToNBT(CompoundNBT tag) {

    }

    @Override
    public void readFromNBT(CompoundNBT tag) {

    }

    @Override
    public String getTranslationKey() {
        return tooltipBase + "elytra";
    }
}